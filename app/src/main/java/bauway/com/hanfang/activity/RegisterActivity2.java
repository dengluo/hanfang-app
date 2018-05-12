package bauway.com.hanfang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.util.CountDownTimerUtils;
import bauway.com.hanfang.util.MyUtil;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity2 extends BaseActivity {

    private static final String TAG = "RegisterActivity2";

    @BindView(R.id.et_register_email)
    EditText et_phone_code;
    @BindView(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @BindView(R.id.et_register_pwd_again)
    EditText mEtRegisterPwdAgain;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.bt_register2)
    Button mBtRegister2;
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
    @BindView(R.id.chekbox_agreement_myz)//是否开启30天免验证
            CheckBox chekbox_agreement_myz;
    @BindView(R.id.ll_register_code)
    LinearLayout ll_register_code;

    private User mUser;

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
        mUser = getUserEntity();
    }

    @Override
    protected void initView() {
        String emailHistory = userRxPreferences.getString(Constants.LOGIN_PHONE).get();
        if (!TextUtils.isEmpty(emailHistory)) {
            et_phone_code.setTag(emailHistory);
            et_phone_code.setSelection(et_phone_code.getText().toString().length());
        }
        chekbox_agreement_myz.setChecked(false);
        chekbox_agreement_myz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_register_code.setVisibility(View.GONE);
                } else {
                    ll_register_code.setVisibility(View.VISIBLE);
                }
            }
        });
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
                erificationCode.setText(getString(R.string.sent));
                CountDownTimerUtils timer = new CountDownTimerUtils(erificationCode, 60000, 1000);
                timer.start();
            }
        });

    }

    @OnClick({R.id.iv_return, R.id.bt_register, R.id.bt_register2, R.id.verification_code, R.id.txt_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.bt_register://注册
//                register();
                break;
            case R.id.bt_register2://机构注册
                register2();
                break;
            case R.id.verification_code://验证码
                hideKeyboard();
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
        if (!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtils.showShort(getString(R.string.toast_yzm_2));
            return;
        }
        String phone = et_phone_code.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(getString(R.string.plz_input_phone));
            return;
        }
        if (!MyUtil.isMobileNO(phone)){
            ToastUtils.showShort(R.string.plz_phone_format);
            return;
        }
        cn.bmob.v3.BmobSMS.requestSMSCode(phone, "register", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    LogUtils.d("短信发送成功，短信ID：" + integer);
                    ToastUtils.showShort(R.string.code_is_send_plz_check);
                    myListener.setupdateUIVericationCode();
                } else {
                    LogUtils.d("短信验证码请求失败：" + e.toString());
                    ToastUtils.showShort(R.string.code_is_send_error_plz_try);
                }
                DialogUtil.hide();
            }
        });
    }

    private void register2() {
        final String et_phone = et_phone_code.getText().toString().trim();
        if (TextUtils.isEmpty(et_phone)) {
            ToastUtils.showShort(R.string.plz_input_phone);
            return;
        }
        if (!MyUtil.isMobileNO(et_phone)){
            ToastUtils.showShort(R.string.plz_phone_format);
            return;
        }
        String register_code = et_register_code.getText().toString().trim();
        if (TextUtils.isEmpty(register_code) && !chekbox_agreement_myz.isChecked()) {
            ToastUtils.showShort(R.string.plz_input_yzm);
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
            ToastUtils.showShort(R.string.please_check_tos);
            return;
        }
        DialogUtil.progressDialog(mContext, getString(R.string.register_now), false);
        mUser.setUsername(et_phone_code.getText().toString().trim());
        mUser.setMobilePhoneNumber(et_phone_code.getText().toString().trim());
        mUser.setPassword(mEtRegisterPwd.getText().toString().trim());
        mUser.setMobilePhoneNumberVerified(true);
        mUser.setApp_name(AppUtils.getAppName());
        mUser.setIsPerson(false);
        if (chekbox_agreement_myz.isChecked()) {
            mUser.setSMSBOOL(false);
        } else {
            mUser.setSMSBOOL(true);
        }

        if (chekbox_agreement_myz.isChecked()){
            mUser.signUp( new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        // ToastUtils.showShort(R.string.register_success_plz_check_email);
                        ToastUtils.showShort(R.string.register_success_plz_check_phone);
                        userRxPreferences.getString(Constants.LOGIN_PHONE).set(et_phone_code.getText().toString().trim());
                        startActivity(new Intent(mContext, PerfectInfoActivity.class));
                        RegisterActivity2.this.finish();
                    } else {
                        Log.e(TAG, "done: " + e.getErrorCode() + ":" + e.getMessage());
                        if (203 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.phone_already_register);
                        } else if(207 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.code_error);
                        } else if (202 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.phone_already_register);
                        } else {
                            ToastUtils.showShort(R.string.register_failure);
                        }
                    }
                    DialogUtil.hide();
                }
            });
        }else{
            mUser.signOrLogin(register_code, new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        // ToastUtils.showShort(R.string.register_success_plz_check_email);
                        ToastUtils.showShort(R.string.register_success_plz_check_phone);
                        userRxPreferences.getString(Constants.LOGIN_PHONE).set(et_phone_code.getText().toString().trim());
                        startActivity(new Intent(mContext, PerfectInfoActivity.class));
                        RegisterActivity2.this.finish();
                    } else {
                        Log.e(TAG, "done: " + e.getErrorCode() + ":" + e.getMessage());
                        if (203 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.phone_already_register);
                        } else if(207 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.code_error);
                        } else if (202 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.phone_already_register);
                        }  else {
                            ToastUtils.showShort(R.string.register_failure);
                        }
                    }
                    DialogUtil.hide();
                }
            });
        }



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
