package io.dwak.holohackernews.app.ui.browser;

/**
 * Created by kounalem on 1/10/2016.
 */

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseActivity;
import io.dwak.holohackernews.app.util.Constants;
import io.dwak.holohackernews.app.util.HNLog;
import io.dwak.holohackernews.app.util.PhoneUtils;

/**
 * Class for displaying web content.
 * =================================
 * <p>
 * Content can be displayed two ways:
 * <p>
 * 1. From a url that is passed (Constants.BundleExtraArgumentNames.WEBSITE_URL)
 * As per Google recommendations use Chrome Custom Tabs when displaying third party content: https://developer.chrome.com/multidevice/android/customtabs
 * Chrome Custom Tabs functionality requires Chrome V45+.
 * <p>
 * If this is not available fallback is to use standard WebView control with addition of onKeyDown event to enhance navigation (specifically back button functionality)
 * <p>
 * Using Chrome Custom Tabs does not require any Views to be added to the Layout file...the tabs will be created programmatically and display all content.
 * To enable the fallback option the layout file contains a WebView control for displaying the web content.
 * Thus the views in the layout file are only relevant to the fallback option.
 * <p>
 * If Menu items, Action items or other values are required they should also be passed in as Extra values.
 * <p>
 * Note: Currently very few examples on the web.
 * The most complete example found: https://github.com/GoogleChrome/custom-tabs-client
 * <p>
 * <p>
 * 2. From html that is passed (Constants.BundleExtraArgumentNames.HTML_CONTENT)
 * Send the content to the webView control
 * <p>
 * Current Assumption: If url is being passed it is for an external site so the Chrome Custom Tab is the preferred option.
 * If the html is passed it will be internally generated (possibly from a service)
 */

public class WebContentActivity extends BaseActivity implements ServiceConnectionCallback {

    private static final String TAG = WebContentActivity.class.getSimpleName();
    public static final String MIME_TYPE = "text/html";
    public static final String ENCODING = Constants.UTF_8_CHAR_SET;

    protected String url;
    protected String html;

    protected CustomTabsSession customTabsSession;
    protected CustomTabsClient customTabsClient;
    protected CustomTabsServiceConnection customTabsServiceConnection;
    protected String mPackageNameToBind = CustomTabsHelper.STABLE_PACKAGE; //what version of Chrome to connect to Beta, Dev, Stable etc

    protected @InjectView (R.id.web_content_wv) WebView webView;
    protected @InjectView(R.id.web_content_sv) ScrollView scrollview;
    protected @InjectView(R.id.toolbar) Toolbar toolbar;
    protected @InjectView(R.id.web_content_first_button) Button firstBtn;
    protected @InjectView(R.id.web_content_second_button) Button secondBtn;

    protected boolean bUsingChromeCustomTab = false;

    //In case we need to implement listeners for events, e.g. to return button click in the case of a static HTML
    public interface ActivityResult {
        int error = 1000;
        int firstButtonSelected = 1001;
        int secondButtonSelected = 1002;
    }

    @OnClick(R.id.web_content_first_button)
    public void firstButtonClick(Button button) {
        setResult(ActivityResult.firstButtonSelected);
        finish();
    }

    @OnClick(R.id.web_content_second_button)
    public void secondButtonClick(Button button) {
        setResult(ActivityResult.secondButtonSelected);
        finish();
    }

    //Callback method that indicates what is happening in the Chrome Custome Tab
    //Look in CustomTabsCallback class to find what some of the navigationEvent values mean
    //Note: the list is not complete...eg 6 means back. Check Google site.
    protected static class NavigationCallback extends CustomTabsCallback {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            HNLog.d(TAG, String.valueOf("onNavigationEvent: Code = " + navigationEvent));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_content);

        ButterKnife.inject(this);
        setupViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressAction();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    protected void setupViews() {

        ButterKnife.inject(this);

        //set the page title
        if (getIntent().hasExtra(Constants.BundleExtraArgumentNames.PAGE_TITLE)) {

            if(toolbar !=null){
                toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
                toolbar.setNavigationOnClickListener(v -> finish());
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.BundleExtraArgumentNames.PAGE_TITLE));
            }
        }
        //Has a website link been sent?
        if (getIntent().hasExtra(Constants.BundleExtraArgumentNames.WEBSITE_URL))
            url = getIntent().getStringExtra(Constants.BundleExtraArgumentNames.WEBSITE_URL);

        //Has html content been sent?
        if (getIntent().hasExtra(Constants.BundleExtraArgumentNames.HTML_CONTENT))
            html = getIntent().getStringExtra(Constants.BundleExtraArgumentNames.HTML_CONTENT);

        //check that there is something
        if (TextUtils.isEmpty(url) && TextUtils.isEmpty(html)) {
            setResult(ActivityResult.error);
            finish();
            return;
        }

        if (TextUtils.isEmpty(url))
            //no url so content must be from a String extra or a String id - so displaying in a WebView
            initialiseHtmlContent();
        else
            //got a url so displaying in Chrome Custom Tab (or WebView)
            initialiseUrlContent();

    }

    protected void initialiseHtmlContent() {

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (!TextUtils.isEmpty(html)) {
            //loading twice will help prevent display issues encountered when loading a series of documents
            webView.loadData(html, MIME_TYPE, ENCODING);
            webView.loadData(html, MIME_TYPE, ENCODING);
            scrollview.setSmoothScrollingEnabled(false);
            scrollview.fullScroll(ScrollView.FOCUS_UP);
        }

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {

                //Not sure what calling activity might want to display so check all these values before using them
                if (getIntent().hasExtra(Constants.BundleExtraArgumentNames.FIRST_BUTTON_TEXT)) {
                    firstBtn.setText(getIntent().getStringExtra(Constants.BundleExtraArgumentNames.FIRST_BUTTON_TEXT));
                    firstBtn.setVisibility(View.VISIBLE);
                }

                if (getIntent().hasExtra(Constants.BundleExtraArgumentNames.SECOND_BUTTON_TEXT)) {
                    secondBtn.setText(getIntent().getStringExtra(Constants.BundleExtraArgumentNames.SECOND_BUTTON_TEXT));
                    secondBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void initialiseUrlContent() {
        //Decide what we will be using on this activity - tabs or webview.
        //Set bUsingChromeCustomTab so can be tested elsewhere in class
        bUsingChromeCustomTab = isChromeInstalledAndHaveCustomTabsFunctionality();
        if (bUsingChromeCustomTab) {
            //bind to the
            bindCustomTabsService();
        } else {
            //the usual WebView set up including enabling JavaScript
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }

    //Does the phone have Chrome installed and a version of Chrome that supports custom tabs?
    protected boolean isChromeInstalledAndHaveCustomTabsFunctionality() {

        if (PhoneUtils.isAppInstalledOnDevice(this, Constants.GOOGLE_CHROME_PACKAGE_NAME)) {

            //get the package version name
            String versionName = PhoneUtils.getVersionNameOfAppInstalledOnDevice(this, Constants.GOOGLE_CHROME_PACKAGE_NAME);

            if (!TextUtils.isEmpty(versionName)) {

                //split the version name into components eg 39.1.2.54
                String deliminator = "\\."; //period has a meaning in regular expressions so need to escape it
                String versionNameComponents[] = TextUtils.split(versionName, deliminator);

                //check major version is a number...should always be but checking anyway
                if (TextUtils.isDigitsOnly(versionNameComponents[0])) {

                    //compare against the version of Chrome when custom tabs were introduced.
                    int majorVersioNumber = Integer.parseInt(versionNameComponents[0]);
                    if (majorVersioNumber >= Constants.GOOGLE_CHROME_MIN_VERSION_NUMBER_TO_USE_CUSTOM_TABS)
                        return true; //use chrome custom tabs
                }
            }
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();

        //If we are using Chrome Custom Tabs the want to make sure that we never see the WebView
        //The only time this could happen is if you press back when on the tab so that there is nothing previous in the tab history
        //If this occurs and the WebView is visible assume the user has finished viewing web content and finish() activity.
        if (bUsingChromeCustomTab) {
            WebView webView = (WebView) findViewById(R.id.web_content_wv);
            Rect scrollBounds = new Rect();

            //checks is WebView is visible on screen at this point.
            if (!webView.getLocalVisibleRect(scrollBounds))
                finish();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //The Chrome tab has all the back navigation options we need so this is only relevant to WebView
        if (!bUsingChromeCustomTab ) {

            //On WebView when you hit back it will jump you back to previous activity, not the previous
            //web page that was viewed in the WebView. This becomes an issue if you start navigating
            //within the WebView.
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (null != webView && webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            onBackPressAction();
                        }
                        return true;
                }

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onBackPressAction() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else {
            overridePendingTransition(R.anim.fadein, R.anim.view_right_to_offscreen);
        }
    }

    @Override
    protected void onDestroy() {
        unbindCustomTabsService();
        super.onDestroy();
    }

    protected CustomTabsSession getSession() {
        if (customTabsClient == null) {
            customTabsSession = null;
        } else if (customTabsSession == null) {
            customTabsSession = customTabsClient.newSession(new NavigationCallback());
        }
        return customTabsSession;
    }


    protected void bindCustomTabsService() {
        //client already connected so no point trying again
        if (customTabsClient != null)
            return;

        //attempt to bind to the custom tabs service
        //if connection attempt is successful the onServiceConnected callback is fired
        customTabsServiceConnection = new ServiceConnection(this);
        if (!CustomTabsClient.bindCustomTabsService(this, mPackageNameToBind, customTabsServiceConnection))
            customTabsServiceConnection = null;
    }

    protected void unbindCustomTabsService() {

        if (customTabsServiceConnection == null)
            return;

        unbindService(customTabsServiceConnection);
        customTabsClient = null;
        customTabsSession = null;
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        customTabsClient = client;
        if (customTabsClient == null)
            return;

        customTabsClient.warmup(0);
        CustomTabsSession session = getSession();
        session.mayLaunchUrl(Uri.parse(url), null, null);

        //Create the chrome tabs and it's content
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.primary_dark)).setShowTitle(true);
        builder.setStartAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_close));
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public void onServiceDisconnected() {
        customTabsClient = null;
    }


}