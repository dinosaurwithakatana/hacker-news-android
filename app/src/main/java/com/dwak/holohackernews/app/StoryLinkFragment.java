package com.dwak.holohackernews.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoryLinkFragment.OnStoryLinkFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoryLinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryLinkFragment extends Fragment {
    private static final String URL_TO_LOAD = "url_to_load";
    private static final String TAG = StoryLinkFragment.class.getSimpleName();
    private String mUrlToLoad;
    private OnStoryLinkFragmentInteractionListener mListener;
    private WebView mWebView;
    private Bundle mWebViewBundle;

    public StoryLinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StoryLinkFragment.
     */
    public static StoryLinkFragment newInstance(String param1) {
        StoryLinkFragment fragment = new StoryLinkFragment();
        Bundle args = new Bundle();
        args.putString(URL_TO_LOAD, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public String getUrlToLoad() {
        return mUrlToLoad;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrlToLoad = getArguments().getString(URL_TO_LOAD);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_link, null);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mWebView = (WebView) rootView.findViewById(R.id.story_web_view);

        mWebView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "page loaded");
                mWebView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        if (mWebViewBundle == null) {
            mWebView.loadUrl(mUrlToLoad);
        } else {
            mWebViewBundle = savedInstanceState;
            mWebView.restoreState(mWebViewBundle);
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebViewBundle = new Bundle();
        mWebView.saveState(mWebViewBundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null){
            mWebViewBundle = savedInstanceState;
            mWebView.restoreState(mWebViewBundle);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStoryLinkFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStoryLinkFragmentInteractionListener {
        public void onStoryLinkFragmentInteraction(Uri uri);
    }

}
