package io.dwak.holohackernews.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

/**
 * A RelativeLayout that can be animated vertically or horizontally using Facebook's Rebound library
 * Created by vishnu on 8/5/14.
 * @see android.widget.RelativeLayout
 */
public class ReboundRevealRelativeLayout extends RelativeLayout {
    private static final SpringConfig SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(6, 6);

    public static enum TranslateDirection {
        TRANSLATE_DIRECTION_VERTICAL,
        TRANSLATE_DIRECTION_HORIZONTAL
    }

    private int mRevealPixel;
    private int mStashPixel;
    private Spring mSpring;
    private boolean mOpen;
    private TranslateDirection mTranslateDirection;
    private RevealListener mRevealListener;

    public ReboundRevealRelativeLayout(Context context) {
        this(context, null);
    }

    public ReboundRevealRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundRevealRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        SpringSystem springSystem = SpringSystem.create();
        mSpring = springSystem.createSpring();
        mSpring.setSpringConfig(SPRING_CONFIG);
        LinkSpringListener linkSpringListener = new LinkSpringListener();
        mSpring.setCurrentValue(0)
                .setEndValue(1)
                .addListener(linkSpringListener);
    }

    /**
     * Set whether the view visible or not
     *
     * @param open true if visible
     */
    public void setOpen(boolean open) {
        mOpen = open;
        togglePosition(open);
    }

    private void togglePosition(boolean open) {
        mSpring.setEndValue(open
                ? 0
                : 1);
    }

    public boolean isOpen() {
        return mOpen;
    }

    /**
     * Sets the direction in which to reveal and stash the view
     *
     * @param translateDirection {@link io.dwak.holohackernews.app.widget.ReboundRevealRelativeLayout.TranslateDirection} describing the direction to animate
     */
    public void setTranslateDirection(TranslateDirection translateDirection) {
        mTranslateDirection = translateDirection;
    }

    private class LinkSpringListener implements SpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float val = (float) spring.getCurrentValue();
            float maxTranslate = mRevealPixel;
            float minTranslate = mStashPixel;
            float range = maxTranslate - minTranslate;
            float translate = (val * range) + minTranslate;

            switch (mTranslateDirection) {
                case TRANSLATE_DIRECTION_HORIZONTAL:
                    setTranslationX(translate);
                    break;
                case TRANSLATE_DIRECTION_VERTICAL:
                    setTranslationY(translate);
                    break;
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {
            if (mRevealListener != null) {
                mRevealListener.onVisibilityChange(spring.getCurrentValue() == 0.0);
            }
        }

        @Override
        public void onSpringActivate(Spring spring) {

        }

        @Override
        public void onSpringEndStateChange(Spring spring) {

        }
    }

    public int getRevealPixel() {
        return mRevealPixel;
    }

    public int getStashPixel() {
        return mStashPixel;
    }

    /**
     * Sets the pixel to reveal the view to
     *
     * @param revealPixel Integer value to set the view to when revealing
     */
    public void setRevealPixel(int revealPixel) {
        mRevealPixel = revealPixel;
    }

    /**
     * Sets the pixel to stash the view to
     *
     * @param stashPixel Integer value to set the view to when stashing
     */
    public void setStashPixel(int stashPixel) {
        mStashPixel = stashPixel;
    }

    /**
     * Set a listener callback for when visibility animations are complete
     *
     * @param listener {@link io.dwak.holohackernews.app.widget.ReboundRevealRelativeLayout.RevealListener}  listener for when animations complete
     */
    public void setRevealListener(RevealListener listener) {
        mRevealListener = listener;
    }

    /**
     * Interface to implement if you want to subscribe to visibility changes
     */
    public interface RevealListener {
        void onVisibilityChange(boolean visible);
    }
}
