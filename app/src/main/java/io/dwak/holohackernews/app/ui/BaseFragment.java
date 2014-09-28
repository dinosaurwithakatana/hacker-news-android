package io.dwak.holohackernews.app.ui;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by vishnu on 5/3/14.
 */
public class BaseFragment extends Fragment {
    protected View mContainer;
    protected ProgressBar mProgressBar;

    protected void showProgress(boolean showProgress){
        mContainer.setVisibility(showProgress ? View.INVISIBLE: View.VISIBLE);
        mProgressBar.setVisibility(showProgress ? View.VISIBLE : View.INVISIBLE);
    }
}
