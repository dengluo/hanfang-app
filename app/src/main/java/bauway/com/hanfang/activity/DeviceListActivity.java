package bauway.com.hanfang.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.R;
import bauway.com.hanfang.adapter.DeviceListAdapter;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.ItemBean;
import bauway.com.hanfang.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.sms.BmobSMS;

public class DeviceListActivity extends BaseActivity {

    @BindView(R.id.lv_device)
    ListView lv_device;
    @BindView(R.id.iv_device_add)
    ImageView iv_device_add;

    private static final String TAG = "DeviceSettingActivity";
    private Context ctx;
    private DeviceListAdapter mAdapter;
    private List<ItemBean> mData;
    public static Handler mHandler;//接受BindDeviceActivity发送过来的消息
    private ItemBean ib;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_list;
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

    @SuppressLint("HandlerLeak")
    @Override
    protected void initView() {
        ib = new ItemBean();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 19:
                        ib.setText(msg.obj.toString());
                        mData.add(ib);
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.showShortToast(DeviceListActivity.this, msg.obj + "");
                        break;
                }
            }
        };
        mData = new ArrayList<ItemBean>();
        mAdapter = new DeviceListAdapter(this, mData);
        lv_device.setAdapter(mAdapter);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ctx = DeviceListActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_return, R.id.iv_device_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                this.finish();
                break;
            case R.id.iv_device_add:
//                mData.add(new ItemBean());
//                mAdapter.notifyDataSetChanged();
                startActivity(new Intent(DeviceListActivity.this, BindDeviceActivity.class).putExtra("shebei", "device1"));
                break;

            default:
                break;
        }
    }

}
