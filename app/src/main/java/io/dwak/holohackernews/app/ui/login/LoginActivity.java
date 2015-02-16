package io.dwak.holohackernews.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.dd.CircularProgressButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.HoloHackerNewsApplication;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import retrofit.client.Header;
import rx.Observable;
import rx.android.observables.ViewObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends ActionBarActivity {

    public static final String LOGIN_SUCCESS = "login-success";
    public static final String LOGOUT = "logout";
    private static final String TAG = LoginActivity.class.getSimpleName();
    @InjectView(R.id.username) EditText mUsername;
    @InjectView(R.id.password) EditText mPassword;
    @InjectView(R.id.login_button_with_progress) CircularProgressButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        // Creates Observables from the EditTexts and enables the login button if they aren't empty
        final Observable<Boolean> userNameObservable = ViewObservable.text(mUsername, true)
                .map(editText -> !TextUtils.isEmpty(editText.getText()));
        final Observable<Boolean> passwordObservable = ViewObservable.text(mPassword, true)
                .map(editText -> !TextUtils.isEmpty(editText.getText()));
        Observable.combineLatest(userNameObservable, passwordObservable,
                (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .subscribe(t1 -> {
                    mLoginButton.setProgress(0);
                    mLoginButton.setEnabled(t1);
                });

        ViewObservable.clicks(mLoginButton, false)
                .subscribe(button -> {
                    mLoginButton.setIndeterminateProgressMode(true);
                    mLoginButton.setProgress(50);
                    HoloHackerNewsApplication.getInstance()
                            .getLoginServiceInstance()
                            .login("news", mUsername.getText().toString(), mPassword.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(response -> {
                                String userCookie = null;
                                for (Header header : response.getHeaders()) {
                                    if ("set-cookie".equals(header.getName())) {
                                        if (header.getValue().contains("user")) {
                                            userCookie = header.getValue().substring(5);
                                            userCookie = userCookie.split(";")[0];
                                        }
                                    }
                                }
                                return userCookie;
                            })
                            .subscribe(userCookie -> {
                                if (userCookie == null) {
                                    mLoginButton.setProgress(-1);
                                }
                                else {
                                    mLoginButton.setProgress(100);
                                    LocalDataManager.getInstance().setUserLoginCookie(userCookie);
                                    LocalDataManager.getInstance().setUserName(mUsername.getText().toString());
                                    Intent loginIntent = new Intent(LOGIN_SUCCESS);
                                    LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(loginIntent);
                                    LoginActivity.this.finish();
                                }
                            });
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
