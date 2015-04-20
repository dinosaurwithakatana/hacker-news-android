package io.dwak.holohackernews.app.base;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import io.dwak.holohackernews.app.R;
import rx.Subscription;

/**
 * Created by vishnu on 5/3/14.
 */
public abstract class BaseFragment extends Fragment {
    protected View mContainer;
    protected ProgressBar mProgressBar;
    protected Subscription mSubscription;

    protected void showProgress(boolean showProgress){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator fadeIn = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.fadein);
            ObjectAnimator fadeOut = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.fadeout);
            if (showProgress) {
                FadeListener fadeInListener = new FadeListener(mContainer, mProgressBar);
                fadeIn.addListener(fadeInListener);
                fadeIn.start();
            }
            else {
                FadeListener fadeOutListener = new FadeListener(mProgressBar, mContainer);
                fadeOut.addListener(fadeOutListener);
                fadeOut.start();
            }
        }
        else {
            mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
            mContainer.setVisibility(showProgress ? View.GONE : View.VISIBLE);
        }
    }

    class FadeListener implements Animator.AnimatorListener{
        View mFromView;
        View mToView;

        FadeListener(View fromView, View toView) {
            mFromView = fromView;
            mToView = toView;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mFromView.setVisibility(View.GONE);
            mToView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
