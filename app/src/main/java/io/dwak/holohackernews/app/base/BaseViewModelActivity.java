package io.dwak.holohackernews.app.base;

import android.os.Bundle;

import io.dwak.holohackernews.app.ui.BaseActivity;

/**
 * Created by vishnu on 2/15/15.
 */
public class BaseViewModelActivity<T extends BaseViewModel> extends BaseActivity{
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
