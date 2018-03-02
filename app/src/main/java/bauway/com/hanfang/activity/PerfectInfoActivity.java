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
import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.CountDownTimerUtils;
import bauway.com.hanfang.util.DialogUtil;
import bauway.com.hanfang.util.NetworkUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 完善机构注册信息
 * danny
 */
public class PerfectInfoActivity extends BaseActivity {

    private static final String TAG = "PerfectInfoActivity";

    @BindView(R.id.et_perfectinfo_organization_name)
    EditText et_perfectinfo_organization_name;
    @BindView(R.id.et_perfectinfo_legal_representative)
    EditText et_perfectinfo_legal_representative;
    @BindView(R.id.et_perfectinfo_personinfo_head)
    EditText et_perfectinfo_personinfo_head;
    @BindView(R.id.et_perfectinfo_registration_mark)
    EditText et_perfectinfo_registration_mark;
    @BindView(R.id.et_perfectinfo_address)
    EditText et_perfectinfo_address;
    @BindView(R.id.bt_perfectinfo_register)
    Button bt_perfectinfo_register;
    @BindView(R.id.iv_return)
    ImageView iv_return;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_perfectinfo;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        BmobSMS.initialize(this, Constants.BMOB_ID);
    }

    @Override
    protected void initView() {
        String emailHistory = userRxPreferences.getString(Constants.LOGIN_PHONE).get();

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.iv_return, R.id.bt_perfectinfo_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.bt_perfectinfo_register:
                perfectinfoRegister();
                break;
            default:
                break;
        }
    }


    private void perfectinfoRegister() {
        String name = et_perfectinfo_organization_name.getText().toString().trim();
        String representative = et_perfectinfo_legal_representative.getText().toString().trim();
        String head = et_perfectinfo_personinfo_head.getText().toString().trim();
        String mark = et_perfectinfo_registration_mark.getText().toString().trim();
        String address = et_perfectinfo_address.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.plz_input_organization_name);
            return;
        }
        if (TextUtils.isEmpty(representative)) {
            ToastUtils.showShort(R.string.plz_input_legal_representative);
            return;
        }
        if (TextUtils.isEmpty(head)) {
            ToastUtils.showShort(R.string.plz_input_personinfo_head);
            return;
        }
        if (TextUtils.isEmpty(mark)) {
            ToastUtils.showShort(R.string.plz_input_registration_mark);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.plz_input_address);
            return;
        }
        String[] arr = {name, representative, head, mark, address};

        DialogUtil.progressDialog(PerfectInfoActivity.this, getString(R.string.register_now), false);
        String objectId = BmobUser.getCurrentUser().getObjectId();
        final String phoneNumber = BmobUser.getCurrentUser().getMobilePhoneNumber();
        final User user = new User();
        user.setInfo(arr);
        user.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "更新成功"+phoneNumber);
                    userRxPreferences.getString(Constants.LOGIN_PHONE).set(phoneNumber);
                    startActivity(new Intent(PerfectInfoActivity.this, LoginActivity2.class));
                    PerfectInfoActivity.this.finish();
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        DialogUtil.hide();//dismissDialog();
        super.onDestroy();
    }
}
