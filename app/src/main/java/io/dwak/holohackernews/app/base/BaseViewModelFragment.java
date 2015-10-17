package io.dwak.holohackernews.app.base;

public abstract class BaseViewModelFragment<T extends BaseViewModel> extends BaseFragment {
    protected abstract T getViewModel();

    @Override
    public void onResume() {
        super.onResume();
        getViewModel().onAttachToView();
    }

    public void onPause(){
        super.onPause();
        getViewModel().onDetachFromView();
    }
}
