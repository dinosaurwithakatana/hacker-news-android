package io.dwak.holohackernews.app.base;

import android.os.Bundle;

/**
 * Created by vishnu on 2/15/15.
 */
public abstract class BaseViewModelActivity<T extends BaseViewModel> extends BaseActivity{
    private T mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel();
    }

    public T getViewModel() {
        if(mViewModel == null){
            try {
                mViewModel = getViewModelClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mViewModel;
    }

    public void setViewModel(T viewModel) {
        mViewModel = viewModel;
    }

    public abstract Class<T> getViewModelClass();
}
