package bauway.com.hanfang.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.FeedBackInfo;
import bauway.com.hanfang.bean.User;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 评价
 */

public class EvaluationActivity extends BaseActivity {
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.bt_return_1)
    ImageButton mBtReturn1;
    @BindView(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @BindView(R.id.et_user_feedback)
    EditText mEtUserFeedback;
    @BindView(R.id.bt_send_info)
    Button mBtSendInfo;
    @BindView(R.id.bt_www_address)
    TextView mBtWwwAddress;

    private User mUser;

    @Override
    protected int getLayoutRes() {
        return R.layout.evaluation_activity;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        mUser = getUserEntity();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_return_1, R.id.bt_send_info,R.id.bt_www_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return_1:
                this.finish();
                break;
            case R.id.bt_www_address:
                jumpWeb("https://www.ciggo.com/");
                break;
            case R.id.bt_send_info:
                String feedBack = mEtUserFeedback.getText().toString().trim();
                if (TextUtils.isEmpty(feedBack)) {
                    ToastUtils.showShort(R.string.plz_input_feedback);
                    return;
                }
                FeedBackInfo feedBackInfo = new FeedBackInfo(mUser.getObjectId(), feedBack);
                feedBackInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            ToastUtils.showShort(R.string.send_feedback_msg_success);
                            EvaluationActivity.this.finish();
                        } else {
                            ToastUtils.showShort(R.string.send_feedback_msg_failure);
                        }
                    }
                });

                break;
        }
    }

}
