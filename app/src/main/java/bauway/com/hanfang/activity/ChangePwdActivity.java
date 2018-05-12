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

import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.R;
import bauway.com.hanfang.util.DialogUtil;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePwdActivity extends BaseActivity {

    private static final String TAG = "ChangePwdActivity";
    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.image_changepwd_oldpwd)
    ImageView mIvOldpwd;
    @BindView(R.id.image_changepwd_newpwd)
    ImageView mIvNewpwd;
    @BindView(R.id.et_changpwd_oldpwd)
    EditText mEtOldpwd;
    @BindView(R.id.et_changpwd_newpwd)
    EditText mEtNewpwd;
    @BindView(R.id.bt_change_pwd)
    Button mBtnChangepwd;
    private boolean isopen = true;//用来标记密码是否可见
    private boolean isopen2 = true;//用来标记密码是否可见

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_changepwd;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        String emailHistory = userRxPreferences.getString(Constants.LOGIN_PHONE).get();
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_return, R.id.image_changepwd_oldpwd, R.id.image_changepwd_newpwd, R.id.et_changpwd_oldpwd, R.id.et_changpwd_newpwd, R.id.bt_change_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.image_changepwd_oldpwd:
                if (isopen) {
                    mEtOldpwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvOldpwd.setImageDrawable(getResources().getDrawable(R.drawable.general_password_show));
                } else {
                    mEtOldpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvOldpwd.setImageDrawable(getResources().getDrawable(R.drawable.general_password_hidden));
                }
                isopen = !isopen;
                break;
            case R.id.image_changepwd_newpwd:
                if (isopen2) {
                    mEtNewpwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvNewpwd.setImageDrawable(getResources().getDrawable(R.drawable.general_password_show));
                } else {
                    mEtNewpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvNewpwd.setImageDrawable(getResources().getDrawable(R.drawable.general_password_hidden));
                }
                isopen2 = !isopen2;
                break;
            case R.id.bt_change_pwd:
                changePwd();
                break;

            default:
                break;
        }
    }

    private void changePwd() {
        if (!NetworkUtil.isNetworkAvailable(this)){
            ToastUtil.showShortToast(ChangePwdActivity.this,getString(R.string.toast_yzm_2));
            return;
        }
        final String oldpwd = mEtOldpwd.getText().toString().trim();
        final String newpwd = mEtNewpwd.getText().toString().trim();
        String pwd = userRxPreferences.getString(Constants.LOGIN_PWD).get();
        if (TextUtils.isEmpty(oldpwd)) {
            ToastUtils.showShort(R.string.plz_input_oldpwd);
            return;
        }
        if (TextUtils.isEmpty(newpwd)) {
            ToastUtils.showShort(R.string.plz_input_newpwd);
            return;
        }
        if (!oldpwd.equals(pwd)) {
            ToastUtils.showShort(R.string.plz_error_oldpwd);
            return;
        }
        DialogUtil.progressDialog(mContext, getString(R.string.change_password_now), false);
        BmobUser newUser = new BmobUser();
        newUser.setPassword(newpwd);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(R.string.change_success_password);
//                    userRxPreferences.getString(Constants.LOGIN_PHONE).set(et_phone);
                    startActivity(new Intent(mContext, LoginActivity2.class));
                    finish();
                } else {
                    Log.e(TAG, "done: " + e.getErrorCode() + ":" + e.getMessage());
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

}
