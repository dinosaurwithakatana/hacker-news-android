package io.dwak.holohackernews.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dwak.holohackernews.app.base.BaseFragment;
import io.dwak.holohackernews.app.base.BaseViewModel;

/**
 * Created by vishnu on 3/13/15.
 */
public abstract class ViewModelFragment<T extends BaseViewModel> extends BaseFragment {
    private T mViewModel;

    protected T getViewModel(){
        if(mViewModel == null){
            try {
                mViewModel = getViewModelClass().newInstance();
                mViewModel.setResources(getResources());
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return mViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onAttachToView();
    }

    public void onPause(){
        super.onPause();
        mViewModel.onDetachFromView();
    }

    protected abstract Class<T> getViewModelClass();
    protected abstract View getRootView(LayoutInflater inflater, ViewGroup container);
}
