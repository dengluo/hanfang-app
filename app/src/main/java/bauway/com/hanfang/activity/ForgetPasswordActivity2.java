package bauway.com.hanfang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.util.CountDownTimerUtils;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.R;
import bauway.com.hanfang.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * author:king
 */
public class ForgetPasswordActivity2 extends BaseActivity {

    private static final String TAG = "ForgetPasswordActivity";
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.verification_code)
    EditText verificationCode;
    @BindView(R.id.phone_verification_code)
    TextView phoneVerificationCode;
    @BindView(R.id.pwd)
    EditText et_pwd;
    @BindView(R.id.image_pwd)
    ImageView imagePwd;
    @BindView(R.id.pwd_again)
    EditText pwdAgain;
    @BindView(R.id.image_pwd2)
    ImageView imagePwd2;
    @BindView(R.id.pwd_bt_reset)
    Button pwdBtReset;
    private boolean isopen=true;//用来标记密码是否可见
    private boolean isopen2=true;//用来标记密码是否可见
    private   String phone;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        setOnListener(new UpdateUIVericationCode() {
            @Override
            public void setupdateUIVericationCode() {
                upDatePhoneVerificationCodeUI();
            }
        });
    }

    @OnClick({R.id.iv_return, R.id.pwd_bt_reset,R.id.image_pwd, R.id.image_pwd2,R.id.phone_verification_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.pwd_bt_reset://重置密码
                //验证验证码
                verifacityCode();
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
            case R.id.image_pwd2:
                if (isopen2) {
                    pwdAgain.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imagePwd2.setImageDrawable(getResources().getDrawable(R.drawable.general_password_show));
                } else {
                    pwdAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imagePwd2.setImageDrawable(getResources().getDrawable(R.drawable.general_password_hidden));
                }
                isopen2 = !isopen2;
                break;
            case R.id.phone_verification_code://请求验证码
                requestVerificationCode();
                break;
            default:
                break;
        }
    }

    //请求验证码
    private void requestVerificationCode() {
        if (!NetworkUtil.isNetworkAvailable(this)){
            ToastUtil.showShortToast(ForgetPasswordActivity2.this,"网络连接异常!");
            return;
        }
        phone = username.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtil.showShortToast(mContext,"手机号码不能为空,请重新输入!");
            return;
        }
        BmobSMS.requestSMSCode(this, phone, "register", new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, cn.bmob.sms.exception.BmobException ex) {
                if (ex == null) {//验证码发送成功
                    Log.e("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                    myListener.setupdateUIVericationCode();
                }else{
                    Log.e(TAG, "done: -------------------------"+ex.getErrorCode()+","+ex.getMessage()+","+smsId );
                }
            }


        });


    }

    /**
     * 更新验证码UI
     */
    private void upDatePhoneVerificationCodeUI() {
        phoneVerificationCode.post(new Runnable() {
            @Override
            public void run() {
                phoneVerificationCode.setText("已发送");
                CountDownTimerUtils timer = new CountDownTimerUtils(phoneVerificationCode,60000,1000);
                timer.start();
            }
        });

    }
    //验证验证码
    private void verifacityCode() {
        if (!NetworkUtil.isNetworkAvailable(this)){
            ToastUtil.showShortToast(ForgetPasswordActivity2.this,"网络连接异常!");
            return;
        }
        String register_code = verificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(register_code)) {
            ToastUtils.showShort("验证码为空,请重新填写验证码");
            return;
        }

        BmobSMS.verifySmsCode(this, phone, register_code, new VerifySMSCodeListener() {


            @Override
            public void done(cn.bmob.sms.exception.BmobException ex) {
                if (ex == null) {//短信验证码已验证成功
                    Log.e("bmob", "验证通过");
                    ToastUtil.showShortToast(mContext, "短信验证已通过");
                    //重置密码!
                    resetPwd();
                } else {
                    ToastUtil.showShortToast(mContext, "短信验证失败");
                    Log.e("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                }
            }


        });
    }

    private void resetPwd() {
        final String et_phone = username.getText().toString().trim();
        if (TextUtils.isEmpty(et_phone)) {
            ToastUtils.showShort(R.string.plz_input_phone);
            return;
        }
        String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort(R.string.plz_input_pwd);
            return;
        }
        String pwdAgainStr = pwdAgain.getText().toString().trim();
        if (TextUtils.isEmpty(pwdAgainStr)) {
            ToastUtils.showShort(R.string.plz_input_pwd_again);
            return;
        }
        if (!pwd.equals(pwdAgainStr)) {
            ToastUtils.showShort(R.string.pwd_input_diff);
            return;
        }
        DialogUtil.progressDialog(mContext, getString(R.string.reset_password_now), false);
        BmobUser newUser = new BmobUser();
        newUser.setPassword(pwd);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(R.string.reset_success_password);
                    userRxPreferences.getString(Constants.LOGIN_PHONE).set(et_phone);
                    startActivity(new Intent(mContext,LoginActivity.class));
                    finish();
                } else {
                    Log.e(TAG, "done: "+e.getErrorCode() + ":" + e.getMessage() );
                    if (203 == e.getErrorCode()) {
                        ToastUtils.showShort(R.string.phone_already_register);
                    } else {
                        ToastUtils.showShort(R.string.reset_failure);
                    }
                }
                DialogUtil.hide();
            }

        });

    }

    public  interface UpdateUIVericationCode {
    public void  setupdateUIVericationCode();
}
    private UpdateUIVericationCode myListener;
    //回调方法
    public void setOnListener(UpdateUIVericationCode myListener) {
        this.myListener = myListener;
    }
}
