package io.dwak.holohackernews.app.base;

public abstract class BaseViewModelActivity<T extends BaseViewModel> extends BaseActivity{
    protected abstract T getViewModel();

    @Override
    protected void onResume() {
        super.onResume();
        getViewModel().onAttachToView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getViewModel().onDetachFromView();
    }
}
