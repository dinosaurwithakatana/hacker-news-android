package io.dwak.holohackernews.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.LocalDataManager;

public class LoginActivity extends ActionBarActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String LOGIN_SUCCESS = "login-success";
    public static final String LOGOUT = "logout";
    @InjectView(R.id.login_webview) WebView mLoginWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mLoginWebview.loadUrl("https://news.ycombinator.com/login");
        WebSettings webSettings = mLoginWebview.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        mLoginWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.equals("https://news.ycombinator.com/")) {
                    String cookies = CookieManager.getInstance().getCookie(url);
                    final String[] split = cookies.split("; ");
                    String userCookie = null;
                    for (String s : split) {
                        if(s.toLowerCase().contains("user")){
                            userCookie = s;
                        }
                    }
                    final String[] userCookieSplit = userCookie.split("=");
                    LocalDataManager.getInstance().setUserLoginCookie(userCookieSplit[1]);
                    Log.d(TAG, cookies);
                    Intent loginIntent = new Intent(LOGIN_SUCCESS);
                    LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(loginIntent);
//                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
