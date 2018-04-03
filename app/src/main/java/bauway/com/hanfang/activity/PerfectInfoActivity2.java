package bauway.com.hanfang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.R;
import bauway.com.hanfang.View.WheelNumDialog;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.User;
import bauway.com.hanfang.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 完善机构注册信息
 * danny
 */
public class PerfectInfoActivity2 extends BaseActivity implements WheelNumDialog.OnDialogBackListener {

    private static final String TAG = "PerfectInfoActivity";

    @BindView(R.id.et_perfectinfo_name)
    EditText et_perfectinfo_name;
    @BindView(R.id.et_perfectinfo_age)
    EditText et_perfectinfo_age;
    @BindView(R.id.tv_perfectinfo_height)
    TextView tv_perfectinfo_height;
    @BindView(R.id.tv_perfectinfo_weight)
    TextView tv_perfectinfo_weight;
    @BindView(R.id.bt_perfectinfo_register)
    Button bt_perfectinfo_register;
    @BindView(R.id.iv_return)
    ImageView iv_return;
    @BindView(R.id.radioGroup1)
    RadioGroup radioGroup1;
    @BindView(R.id.radio0)
    RadioButton radio0;
    @BindView(R.id.radio1)
    RadioButton radio1;

    private RadioButton radioButton;
    private String selectText = "男";
    private int currentButton = 0;
    private String currentTall = "155cm";
    private String currentWeight = "57.3kg";

    private void showNumDialog(int value) {
        WheelNumDialog numDialog = new WheelNumDialog(this, R.style.dialog,
                value);
        numDialog.setListener(this);
        numDialog.setCanceledOnTouchOutside(true);
        numDialog.show();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_perfectinfo2;
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
        tv_perfectinfo_height.setText(currentTall);
        tv_perfectinfo_weight.setText(currentWeight);
        radioGroup1
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioButton = (RadioButton) findViewById(radioGroup1
                                .getCheckedRadioButtonId());
                        selectText = radioButton.getText().toString();
                    }
                });
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.iv_return, R.id.bt_perfectinfo_register,R.id.tv_perfectinfo_height,R.id.tv_perfectinfo_weight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_perfectinfo_height:
                currentButton = 0;
                showNumDialog(0);
                break;
            case R.id.tv_perfectinfo_weight:
                currentButton = 2;
                showNumDialog(2);
                break;
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
        String name = et_perfectinfo_name.getText().toString().trim();
        String sex = selectText;
        String age = et_perfectinfo_age.getText().toString().trim();
        String height = tv_perfectinfo_height.getText().toString().trim();
        String weight = tv_perfectinfo_weight.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(R.string.plz_input_person_name);
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            ToastUtils.showShort(R.string.plz_input_person_sex);
            return;
        }
        if (TextUtils.isEmpty(age)) {
            ToastUtils.showShort(R.string.plz_input_person_age);
            return;
        }
        if (TextUtils.isEmpty(height)) {
            ToastUtils.showShort(R.string.plz_input_person_height);
            return;
        }
        if (TextUtils.isEmpty(weight)) {
            ToastUtils.showShort(R.string.plz_input_person_weight);
            return;
        }
        String[] arr = {name, sex, age, height, weight};

        DialogUtil.progressDialog(PerfectInfoActivity2.this, getString(R.string.register_now), false);
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
                    startActivity(new Intent(PerfectInfoActivity2.this, LoginActivity2.class));
                    PerfectInfoActivity2.this.finish();
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

    @Override
    public void back(String text) {
        if (currentButton == 0) {
            currentTall = text;
            tv_perfectinfo_height.setText(text + "cm");
            tv_perfectinfo_height.setTextColor(getResources().getColor(R.color.black));
        }
        if (currentButton == 2) {
            currentWeight = text;
            tv_perfectinfo_weight.setText(text + "kg");
            tv_perfectinfo_weight.setTextColor(getResources().getColor(
                    R.color.black));
        }
    }
}
