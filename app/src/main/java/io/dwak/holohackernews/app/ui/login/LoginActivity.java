package io.dwak.holohackernews.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.EditText;

import com.dd.CircularProgressButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.base.BaseViewModelActivity;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.LocalDataManager;
import rx.Observable;
import rx.android.observables.ViewObservable;

public class LoginActivity extends BaseViewModelActivity<LoginViewModel> {

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
        setViewModel(new LoginViewModel());

        // Creates Observables from the EditTexts and enables the login button if they aren't empty
        final Observable<Boolean> userNameObservable = ViewObservable.text(mUsername, true)
                .map(editText -> !TextUtils.isEmpty(editText.getText()));
        final Observable<Boolean> passwordObservable = ViewObservable.text(mPassword, true)
                .map(editText -> !TextUtils.isEmpty(editText.getText()));
        Observable.combineLatest(userNameObservable, passwordObservable,
                (usernameFilled, passwordFilled) -> usernameFilled && passwordFilled)
                .subscribe(fieldsFilled -> {
                    mLoginButton.setProgress(0);
                    mLoginButton.setEnabled(fieldsFilled);
                });

        ViewObservable.clicks(mLoginButton, false)
                .subscribe(button -> {
                    mLoginButton.setIndeterminateProgressMode(true);
                    mLoginButton.setProgress(50);
                    getViewModel().login(mUsername.getText().toString(), mPassword.getText().toString())
                            .subscribe(userCookie -> {
                                if (userCookie == null) {
                                    mLoginButton.setProgress(-1);
                                }
                                else {
                                    mLoginButton.setProgress(100);
                                    LocalDataManager.getInstance().setUserLoginCookie(userCookie);
                                    LocalDataManager.getInstance().setUserName(mUsername.getText().toString());
                                    LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(LOGIN_SUCCESS));
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
