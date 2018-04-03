package bauway.com.hanfang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    private static final String TAG = "HelpDocsActivity";
    @BindView(R.id.iv_return)
    ImageView mIvReturn;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about_us;
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

    }

    @OnClick({R.id.iv_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            default:
                break;
        }
    }
}
