package io.dwak.holohackernews.app.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by vishnu on 2/15/15.
 */
public class BaseViewModelActivity<T extends BaseViewModel> extends ActionBarActivity {
    private T mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public T getViewModel() {
        return mViewModel;
    }

    public void setViewModel(T viewModel) {
        mViewModel = viewModel;
    }
}
