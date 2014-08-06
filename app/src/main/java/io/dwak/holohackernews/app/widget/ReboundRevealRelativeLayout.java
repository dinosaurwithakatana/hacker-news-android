package io.dwak.holohackernews.app.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import static com.facebook.rebound.ui.Util.dpToPx;

/**
 * Created by vishnu on 8/5/14.
 */
public class ReboundRevealRelativeLayout extends RelativeLayout {
    private static final SpringConfig SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(6, 6);
    public static final String VERTICAL = "vertical";
    public static final String HORIZONTAL = "horizontal";
    private int mRevealPx;
    private int mStashPx;
    private Spring mSpring1;
    private boolean mOpen;
    private String mTranslateDirection;
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
        mSpring1 = springSystem.createSpring();
        mSpring1.setSpringConfig(SPRING_CONFIG);
        LinkSpringListener linkSpringListener = new LinkSpringListener();
        mSpring1.setCurrentValue(0)
                .setEndValue(1)
                .addListener(linkSpringListener);
        Resources resources = getResources();
        mRevealPx = dpToPx(0, resources);
        mStashPx = dpToPx(480, resources);
        setOpen(true);
        setTranslateDirection(VERTICAL);
    }

    public void setOpen(boolean open) {
        mOpen = open;
        togglePosition(open);
    }

    private void togglePosition(boolean open) {
        mSpring1.setEndValue(open
                ? 0
                : 1);
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setTranslateDirection(String translateDirection) {
        mTranslateDirection = translateDirection;
    }

    private class LinkSpringListener implements SpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float val = (float) spring.getCurrentValue();
            float maxTranslate = mRevealPx;
            float minTranslate = mStashPx;
            float range = maxTranslate - minTranslate;
            float translate = (val * range) + minTranslate;

            if(mTranslateDirection.equals(VERTICAL)) {
                setTranslationY(translate);
            }
            else {
                setTranslationX(translate);
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {
            if(mRevealListener!=null){
                mRevealListener.onVisibilityChange(spring.getCurrentValue()==0.0);
            }
        }

        @Override
        public void onSpringActivate(Spring spring) {

        }

        @Override
        public void onSpringEndStateChange(Spring spring) {

        }
    }

    public int getRevealPx() {
        return mRevealPx;
    }

    public int getStashPx() {
        return mStashPx;
    }

    public void setRevealPx(int revealPx) {
        mRevealPx = revealPx;
    }

    public void setStashPx(int stashPx) {
        mStashPx = stashPx;
    }

    public void setRevealListener(RevealListener listener){
        mRevealListener = listener;
    }

    public interface RevealListener{
        void onVisibilityChange(boolean visible);
    }
}
