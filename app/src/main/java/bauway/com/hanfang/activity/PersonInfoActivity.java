package bauway.com.hanfang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.haofang.R;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;

public class PersonInfoActivity extends BaseActivity {

    private static final String TAG = "PersonInfoActivity";
    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.ll_fragme_accountinfo)
    LinearLayout mLlAccountinfo;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personinfo;
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

    @OnClick({R.id.iv_return, R.id.ll_fragme_accountinfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.ll_fragme_accountinfo:
                Intent intent = new Intent(PersonInfoActivity.this, ChangePwdActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
