package bauway.com.hanfang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.util.CountDownTimerUtils;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity2 extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.et_register_email)
    EditText et_phone_code;
    @BindView(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @BindView(R.id.et_register_pwd_again)
    EditText mEtRegisterPwdAgain;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.txt_agreement)
    TextView mtvagreement;
    @BindView(R.id.et_register_code)
    EditText et_register_code;//验证码输入
    @BindView(R.id.verification_code)
    TextView erificationCode;//发过来的验证码
    @BindView(R.id.chekbox_agreement)//是否接受条款
            CheckBox chekbox_agreement;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        setOnListener(new ForgetPasswordActivity2.UpdateUIVericationCode() {
            @Override
            public void setupdateUIVericationCode() {
                upDatePhoneVerificationCodeUI();
            }
        });
    }

    @Override
    protected void initData() {
        BmobSMS.initialize(this, Constants.BMOB_ID);
    }

    @Override
    protected void initView() {
        String emailHistory = userRxPreferences.getString(Constants.LOGIN_PHONE).get();
        if (!TextUtils.isEmpty(emailHistory)) {
            et_phone_code.setTag(emailHistory);
            et_phone_code.setSelection(et_phone_code.getText().toString().length());
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    /**
     * 更新验证码UI
     */
    private void upDatePhoneVerificationCodeUI() {
        erificationCode.post(new Runnable() {
            @Override
            public void run() {
                erificationCode.setText("已发送");
                CountDownTimerUtils timer = new CountDownTimerUtils(erificationCode, 60000, 1000);
                timer.start();
            }
        });

    }

    @OnClick({R.id.iv_return, R.id.bt_register, R.id.verification_code, R.id.txt_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.bt_register://注册
                //验证验证码
                verifacityCode();

            case R.id.verification_code://验证码
                hideKeyboard();
                erificationCode.setText("验证码");
                requestVerificationCode();
                break;
            case R.id.txt_agreement://服务条款
                Intent intent = new Intent(RegisterActivity2.this, AgreementActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }

    //请求验证码
    private void requestVerificationCode() {
        if (!NetworkUtil.isNetworkAvailable(this)){
            ToastUtil.showShortToast(mContext, "网络连接异常!");
            return;
        }
        String phone = et_phone_code.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast(mContext, "手机号码不能为空!");
            return;
        }
        BmobSMS.requestSMSCode(this, phone, "register", new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, cn.bmob.sms.exception.BmobException ex) {
                if (ex == null) {//验证码发送成功
                    Log.e("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                    //erificationCode.setText("已发送");
                    myListener.setupdateUIVericationCode();
                }
            }
        });

    }


    private void register() {

        final String et_phone = et_phone_code.getText().toString().trim();
        if (TextUtils.isEmpty(et_phone)) {
            ToastUtils.showShort(R.string.plz_input_phone);
            return;
        }
        String pwd = mEtRegisterPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort(R.string.plz_input_pwd);
            return;
        }
        String pwdAgain = mEtRegisterPwdAgain.getText().toString().trim();
        if (TextUtils.isEmpty(pwdAgain)) {
            ToastUtils.showShort(R.string.plz_input_pwd_again);
            return;
        }
        if (!pwd.equals(pwdAgain)) {
            ToastUtils.showShort(R.string.pwd_input_diff);
            return;
        }
        if (!chekbox_agreement.isChecked()) {
            ToastUtils.showShort("请勾选服务条款");
            return;
        }
        DialogUtil.progressDialog(mContext, getString(R.string.register_now), false);
        User user = new User();
        user.setUsername(et_phone);
        user.setMobilePhoneNumber(et_phone);
        user.setPassword(pwd);
        user.setMobilePhoneNumberVerified(true);
        user.setApp_name(AppUtils.getAppName());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    // ToastUtils.showShort(R.string.register_success_plz_check_email);
                    ToastUtils.showShort(R.string.register_success_plz_check_phone);
                    userRxPreferences.getString(Constants.LOGIN_PHONE).set(et_phone);
                    startActivity(new Intent(mContext, LoginActivity.class));
                    RegisterActivity2.this.finish();
                } else {
                    Log.e(TAG, "done: " + e.getErrorCode() + ":" + e.getMessage());
                    if (203 == e.getErrorCode()) {
                        ToastUtils.showShort(R.string.phone_already_register);
                    } else {
                        ToastUtils.showShort(R.string.register_failure);
                    }
                }
                DialogUtil.hide();
            }
        });


    }

    //验证验证码
    private void verifacityCode() {
        String register_code = et_register_code.getText().toString().trim();
        String phone = et_phone_code.getText().toString().trim();
        if (!NetworkUtil.isNetworkAvailable(this)){
            ToastUtil.showShortToast(mContext, "网络连接异常!");
            return;
        }
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
                    //注册!
                    register();
                } else {
                    ToastUtil.showShortToast(mContext, "短信验证失败");
                    Log.e("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                }
            }
        });
    }

    public interface UpdateUIVericationCode {
        public void setupdateUIVericationCode();
    }

    private ForgetPasswordActivity2.UpdateUIVericationCode myListener;

    //回调方法
    public void setOnListener(ForgetPasswordActivity2.UpdateUIVericationCode myListener) {
        this.myListener = myListener;
    }
}
