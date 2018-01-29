package bauway.com.hanfang.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.Fragment.FragmentOrderTake;
import bauway.com.hanfang.R;
import bauway.com.hanfang.adapter.DeviceListAdapter;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.bean.ItemBean;
import bauway.com.hanfang.util.ACache;
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
    private ArrayList<ItemBean> mData;
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
        ACache aCache = ACache.get(DeviceListActivity.this);
        ArrayList<String> list2 = (ArrayList<String>) aCache.getAsObject("danny");
//        Log.e("list2.toString()",list2.toString());
//        Log.e("list22.toString()",list2.size()+"");
        mData = new ArrayList<ItemBean>();
        if (list2 == null){
//            Log.e("mxg", "list == null");
            list2 = new ArrayList<>();
            aCache.put("danny",list2);
            mAdapter = new DeviceListAdapter(this,mData, null);
        }else{
            mAdapter = new DeviceListAdapter(this, null,list2);
        }
        lv_device.setAdapter(mAdapter);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 19:
                        ACache aCache = ACache.get(mContext);
                        ArrayList<String> list = (ArrayList<String>) aCache.getAsObject("danny");
                        ib = new ItemBean();
                        ib.setText(msg.obj.toString());
                        mData.add(ib);
                        mAdapter.notifyDataSetChanged();

                        list.add(msg.obj.toString());
                        aCache.put("danny", list);

//                        ToastUtil.showShortToast(DeviceListActivity.this, msg.obj + "");
                        break;
                    case 29:
//                        Log.e("msg.arg1","=="+msg.arg1+"=="+msg.obj);
                        ACache aCache2 = ACache.get(mContext);
                        ArrayList<String> list3 = (ArrayList<String>) aCache2.getAsObject("danny");
                        Log.e("list3.toString()",list3.toString());
//                        String value = list2.get(msg.arg1).toString();
                        list3.remove(msg.arg1);
//                        list3 = new ArrayList<>();
                        aCache2.put("danny",list3);
                        mAdapter.notifyDataSetChanged();
                        initView();
//                        lv_device.setAdapter(mAdapter);
                        break;
                    case 39:
                        Message message = new Message();
                        message.what = 14;
                        message.obj = msg.obj;
                        FragmentOrderTake.mHandler.sendMessage(message);
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ctx = DeviceListActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
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
                startActivity(new Intent(DeviceListActivity.this, BindDeviceActivity.class).putExtra("shebei", "device"+(mData.size()+1)));
                break;

            default:
                break;
        }
    }

}
