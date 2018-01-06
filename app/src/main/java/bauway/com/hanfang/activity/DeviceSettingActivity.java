package bauway.com.hanfang.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.BuildConfig;
import bauway.com.hanfang.R;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.interfaces.DialogCallback;
import bauway.com.hanfang.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;

public class DeviceSettingActivity extends BaseActivity {

    private static final String TAG = "DeviceSettingActivity";
    @BindView(R.id.iv_return)
    ImageView mIvReturn;
    @BindView(R.id.wv_time_setting)
    WheelView mWvTime;
    @BindView(R.id.wv_fengsu_setting)
    WheelView mWvFengsu;
    @BindView(R.id.wv_wendu_setting)
    WheelView mWvWendu;
    @BindView(R.id.ll_device_connect_state)
    LinearLayout ll_device_connect_state;

    private SmaManager mSmaManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_setting;
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
        initWheelTime();
        initWheelFengsu();
        initWheelWendu();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().init(this).addSmaCallback(new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (BuildConfig.DEBUG) {
//                    append("  ->  isConnected " + isConnected);
                }
            }

            @Override
            public void onWrite(byte[] data) {
                if (BuildConfig.DEBUG) {
//                    append("  ->  onWrite", data);
                }
            }

            @Override
            public void onRead(byte[] data) {
                if (BuildConfig.DEBUG) {
//                    append("  ->  onRead", data);
                }
            }
        });
        mSmaManager.connect(true);
        if (BuildConfig.DEBUG) {
//            mDebugViewManager = new DebugViewManager(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mBtUnbind.setVisibility(TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0]) ? View.GONE :
//                View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        mSmaManager.exit();
        super.onDestroy();
    }

    /**
     * common皮肤、图文混排无皮肤、自定义布局
     */
    private void initWheelTime() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#60212121");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 24;

        mWvTime.setWheelAdapter(new ArrayWheelAdapter(this));
//        mWvTime.setSkin(WheelView.Skin.Common);
//        mWvTime.setSkin(WheelView.Skin.Holo);
        mWvTime.setWheelData(createMinutes());
        mWvTime.setStyle(style);
        mWvTime.setExtraText("", Color.parseColor("#60212121"), 40, 70);
    }

    private void initWheelFengsu() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#60212121");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 24;

        mWvFengsu.setWheelAdapter(new ArrayWheelAdapter(this));
//        mWvTime.setSkin(WheelView.Skin.Common);
//        mWvTime.setSkin(WheelView.Skin.Holo);
        mWvFengsu.setWheelData(createMinutes());
        mWvFengsu.setStyle(style);
        mWvFengsu.setExtraText("", Color.parseColor("#60212121"), 40, 70);
    }

    private void initWheelWendu() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#60212121");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 24;

        mWvWendu.setWheelAdapter(new ArrayWheelAdapter(this));
//        mWvTime.setSkin(WheelView.Skin.Common);
//        mWvTime.setSkin(WheelView.Skin.Holo);
        mWvWendu.setWheelData(createMinutes());
        mWvWendu.setStyle(style);
        mWvWendu.setExtraText("", Color.parseColor("#60212121"), 40, 70);
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createArrays() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add("item" + i);
        }
        return list;
    }

    @OnClick({R.id.iv_return,R.id.ll_device_connect_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.ll_device_connect_state:
                if (!TextUtils.isEmpty(SmaManager.getInstance().getNameAndAddress()[1])) {
                    ToastUtils.showShort(R.string.already_bind);
                    DialogUtil.defaultDialog(mContext, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
//                                mBtUnbind.setVisibility(View.GONE);
                                    Context ctx =DeviceSettingActivity.this;
                                    SharedPreferences sp = ctx.getSharedPreferences("DEVICE",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("deviceName1", "未连接");
                                    editor.commit();
                                }
                            });
                    return;
                }

                startActivity(new Intent(DeviceSettingActivity.this, BindDeviceActivity.class));
                break;

            default:
                break;
        }
    }

}
