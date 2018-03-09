package bauway.com.hanfang.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.DialogUtil;
import bauway.com.hanfang.util.PreferencesUtils;
import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity2 extends BaseActivity implements View.OnClickListener, View.OnKeyListener {
    private static final String TAG = "Login2Activity";

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.pwd)
    EditText et_pwd;
    @BindView(R.id.forget_pwd)
    TextView forgetPwd;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.image_pwd)
    ImageView imagePwd;
    private boolean isopen = true;//用来标记密码是否可见

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login2;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        et_pwd.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_pwd.setOnKeyListener(this);
        imagePwd.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String phone = userRxPreferences.getString(Constants.LOGIN_PHONE).get();
        Log.i("bmob", "phone：" + phone);
        if (!TextUtils.isEmpty(phone)) {
            username.setText(phone);
        }
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.register:
//                ToastUtil.showShortToast(mContext, "register");
                startActivity(new Intent(this, RegisterActivity2.class));
                break;
            case R.id.forget_pwd:
                startActivity(new Intent(this, ForgetPasswordActivity2.class));
                break;
            case R.id.image_pwd:
                if (isopen) {
                    et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imagePwd.setImageDrawable(getResources().getDrawable(R.drawable.general_password_show));
                } else {
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imagePwd.setImageDrawable(getResources().getDrawable(R.drawable.general_password_hidden));
                }
                isopen = !isopen;
                break;
            default:
                ToastUtil.showLongToast(LoginActivity2.this, "id有误！");
                break;
        }
    }

    private void login() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtil.showShortToast(LoginActivity2.this, "网络连接异常!");
            return;
        }
        final String email = username.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showShortToast(this, getString(R.string.plz_input_phone));
            return;
        }
        final String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showShortToast(this, getString(R.string.plz_input_pwd));
            return;
        }
        DialogUtil.progressDialog(LoginActivity2.this, getString(R.string.login_now), false);

        BmobUser.loginByAccount(email, pwd, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
//                    Log.e(TAG, userRxPreferences.getString(Constants.LOGIN_EMAIL).get()+"//"+userRxPreferences.getString(Constants.LOGIN_PHONE).get());
                    userRxPreferences.getString(Constants.LOGIN_EMAIL).set(email);
                    userRxPreferences.getString(Constants.LOGIN_PWD).set(pwd);
                    PreferencesUtils.putEntity(LoginActivity2.this, user);
                    startActivity(new Intent(LoginActivity2.this, MainActivity2.class));
                    finish();
                } else {
                    Log.e(TAG, "done: " + e.getErrorCode() + ":" + e.getMessage());
                    switch (e.getErrorCode()) {
                        case 101:
                            ToastUtil.showShortToast(LoginActivity2.this, "用户名或密码错误");
                            break;
                        case 9001:
                            ToastUtil.showShortToast(LoginActivity2.this, "Application Id为空，请初始化");
                            break;
                        case 9010:
                            ToastUtil.showShortToast(LoginActivity2.this, "网络超时");
                            break;
                        case 9016:
                            ToastUtil.showShortToast(LoginActivity2.this, "无网络连接，请检查您的手机网络.");
                            break;
                        default:
                            ToastUtil.showShortToast(LoginActivity2.this, "登录异常");
                            break;
                    }
                }
                DialogUtil.hide();
            }
        });
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
            hintKbTwo();
            login();
            return true;
        }


        return false;
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        DialogUtil.hide();//dismissDialog();
        super.onDestroy();
    }

}
