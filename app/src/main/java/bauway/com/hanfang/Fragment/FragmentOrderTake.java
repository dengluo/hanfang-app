package bauway.com.hanfang.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.hanfang.BuildConfig;
import bauway.com.hanfang.R;
import bauway.com.hanfang.activity.BindDeviceActivity;
import bauway.com.hanfang.activity.DeviceListActivity;
import bauway.com.hanfang.activity.DeviceSettingActivity;
import bauway.com.hanfang.adapter.MyFragmentPagerAdapter;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.zxing.activity.CaptureActivity;

/**
 * Created by danny on 2017/12/28.
 * 治疗
 */

public class FragmentOrderTake extends Fragment implements View.OnClickListener {

    private Context context;
    private View view_main;
    //UI Objects
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private TextView tv_frag_wendu, tv_frag_time, tv_frag_fengsu, tv_frag_device_name,tv_frag_device_ypcode;
    private ImageView iv_device_drug_codesss, iv_device_bluetooth,iv_find_play1;
    private ViewPager vpager;
    private CheckBox checkbox_dengguang;

    private MyFragmentPagerAdapter mAdapter;
    private SmaManager mSmaManager;
    private Boolean iswork = true;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    public static Handler mHandler;//接受MyFragment1发送过来的消息

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = this.getActivity();
        mSmaManager = SmaManager.getInstance().init(context).addSmaCallback(new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (isConnected) {
                    Log.e("device","==device=="+device.getName()+"=="+device.getAddress());
                    mSmaManager.setNameAndAddress(device.getName(), device.getAddress());
                    mSmaManager.mEaseConnector.setAddress(device.getAddress());
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
//                if (BuildConfig.DEBUG) {
////                    append("  ->  onRead", data);
//                }
                Log.e("read==",data+"");
            }
        });
        mSmaManager.connect(true);

        mSmaManager = SmaManager.getInstance();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10:
                        if (mSmaManager.getNameAndAddress()[0].equals("")) {
                            tv_frag_device_ypcode.setText("");
                        }else {
                            tv_frag_device_ypcode.setText(msg.obj.toString());
                        }
                        break;
                    case 11:
                        SharedPreferences sharedPreferencesWendu1 = context.getSharedPreferences(
                                "MWENDU", Activity.MODE_PRIVATE);
                        String mwendu1 = sharedPreferencesWendu1.getString("mwendu1", "1");
                        tv_frag_wendu.setText(mwendu1 + " 档");
                        Log.e("mSmaManager==name==", mSmaManager.getNameAndAddress().toString() + "||" + mSmaManager.getNameAndAddress()[0]);
                        if (!mSmaManager.isConnected) {
                            ToastUtils.showShortSafe(R.string.device_not_connected);
                            return;
                        }
                        mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, mwendu1);
                        break;
                    case 12:
                        SharedPreferences sharedPreferencesTime1 = context.getSharedPreferences(
                                "MTIME", Activity.MODE_PRIVATE);
                        String mtime1 = sharedPreferencesTime1.getString("mtime1", "1");
                        tv_frag_time.setText(mtime1 + " min");
                        if (!mSmaManager.isConnected) {
                            ToastUtils.showShortSafe(R.string.device_not_connected);
                            return;
                        }
                        mSmaManager.write(SmaManager.SET.ALARM_OCLOCK, mtime1);
                        break;
                    case 13:
                        SharedPreferences sharedPreferencesFengsu1 = context.getSharedPreferences(
                                "MFENGSU", Activity.MODE_PRIVATE);
                        String mfengsu1 = sharedPreferencesFengsu1.getString("mfengsu1", "中");
                        tv_frag_fengsu.setText(mfengsu1 + " 档");
                        if (!mSmaManager.isConnected) {
                            ToastUtils.showShortSafe(R.string.device_not_connected);
                            return;
                        }
                        if(mfengsu1.equals("低")){
                            mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, 1+"");
                        }else if(mfengsu1.equals("中")){
                            mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, 2+"");
                        }else if(mfengsu1.equals("高")){
                            mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, 3+"");
                        }else {

                        }

                        break;
                    case 14:
                        Log.e("14","14");
                        mSmaManager.connect(true);
                        mSmaManager.isConnected = true;
                        mSmaManager.mEaseConnector.connect(true);
                        String deviceName = msg.obj.toString().substring(0, msg.obj.toString().indexOf("=="));
                        String deviceAddress = msg.obj.toString().substring(msg.obj.toString().indexOf("==") + 2, msg.obj.toString().length());
                        if (!TextUtils.isEmpty(deviceAddress))
                            mSmaManager.mEaseConnector.setAddress(deviceAddress).connect(true);

                        if (!mSmaManager.isConnected) {
                            ToastUtils.showShortSafe(R.string.device_not_connected);
                            checkbox_dengguang.setChecked(false);
                            return;
                        }
                        tv_frag_device_name.setText(deviceName);
                        mSmaManager.setNameAndAddress(deviceName, deviceAddress);
                        mSmaManager.mEaseConnector.setAddress(deviceAddress);

                        break;
                    case 15:
                        Log.e("15","15");
                        if (mSmaManager.getNameAndAddress()[0].equals("")) {
                            tv_frag_device_name.setText("未连接");
                        }else {
                            tv_frag_device_name.setText(mSmaManager.getNameAndAddress()[0]);
                        }
                        break;
                }
            }
        };
        inintView();
        rb_channel.setChecked(true);
        return view_main;
    }

    private void inintView() {
        view_main = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_buy, null);
        mAdapter = new MyFragmentPagerAdapter(this.getFragmentManager());
        rg_tab_bar = (RadioGroup) view_main.findViewById(R.id.rg_tab_bar);
        rb_channel = (RadioButton) view_main.findViewById(R.id.rb_channel);
        rb_message = (RadioButton) view_main.findViewById(R.id.rb_message);
        rb_better = (RadioButton) view_main.findViewById(R.id.rb_better);
        iv_find_play1 = (ImageView) view_main.findViewById(R.id.iv_find_play1);
        checkbox_dengguang = (CheckBox) view_main.findViewById(R.id.checkbox_dengguang);
        checkbox_dengguang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox_dengguang.isChecked()){
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        checkbox_dengguang.setChecked(false);
                        return;
                    }
                    ToastUtil.showShortToast(context,"open");
                    mSmaManager.write(SmaManager.SET.ENABLE_GOAL_LIGHT);
                }else{
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        checkbox_dengguang.setChecked(false);
                        return;
                    }
                    ToastUtil.showShortToast(context,"close");
                    mSmaManager.write(SmaManager.SET.DISABLE_GOAL_LIGHT);
                }
            }
        });
        tv_frag_wendu = (TextView) view_main.findViewById(R.id.tv_frag_wendu);
        tv_frag_time = (TextView) view_main.findViewById(R.id.tv_frag_time);
        tv_frag_fengsu = (TextView) view_main.findViewById(R.id.tv_frag_fengsu);
        tv_frag_device_name = (TextView) view_main.findViewById(R.id.tv_frag_device_name);
        tv_frag_device_ypcode = (TextView) view_main.findViewById(R.id.tv_frag_device_ypcode);
        if (mSmaManager.getNameAndAddress()[0].equals("")) {
            tv_frag_device_name.setText("未连接");
        }else {
            tv_frag_device_name.setText(mSmaManager.getNameAndAddress()[0]);
        }
        if (mSmaManager.getNameAndAddress()[0].equals("")) {
            tv_frag_device_ypcode.setText("");
        }else {
            SharedPreferences mSpScan = context.getSharedPreferences(
                    "SCAN", Activity.MODE_PRIVATE);
            String scanResult1 = mSpScan.getString("scanResult1", "未输入");
            tv_frag_device_ypcode.setText(scanResult1);
//            tv_frag_device_ypcode.setText(msg.obj.toString());
        }
        iv_device_drug_codesss = (ImageView) view_main.findViewById(R.id.iv_device_drug_codesss);
        iv_device_bluetooth = (ImageView) view_main.findViewById(R.id.iv_device_bluetooth);
        iv_device_drug_codesss.setOnClickListener(this);
        iv_device_bluetooth.setOnClickListener(this);
        iv_find_play1.setOnClickListener(this);
        rg_tab_bar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_channel:
                        vpager.setCurrentItem(PAGE_ONE);
                        break;
                    case R.id.rb_message:
                        vpager.setCurrentItem(PAGE_TWO);
                        break;
                    case R.id.rb_better:
                        vpager.setCurrentItem(PAGE_THREE);
                        break;
                }
            }
        });

        vpager = (ViewPager) view_main.findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
                if (state == 2) {
                    switch (vpager.getCurrentItem()) {
                        case PAGE_ONE:
                            rb_channel.setChecked(true);
                            break;
                        case PAGE_TWO:
                            rb_message.setChecked(true);
                            break;
                        case PAGE_THREE:
                            rb_better.setChecked(true);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_device_drug_codesss:
                startActivity(new Intent(context, CaptureActivity.class).putExtra("shebei", "device1"));
                break;
            case R.id.iv_device_bluetooth:
                startActivity(new Intent(context, DeviceListActivity.class));
                break;
            case R.id.iv_find_play1:
                checkBluetoothValid();
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }
                if (iswork){
                    iv_find_play1.setBackgroundResource(R.drawable.pause);
                    iswork = false;
                }else {
                    iv_find_play1.setBackgroundResource(R.drawable.play);
                    iswork = true;
                }
                mSmaManager.write(SmaManager.SET.PLAY_WORKE);
                break;
        }
    }

    private void checkBluetoothValid() {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null) {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("错误").setMessage("你的设备不具备蓝牙功能!").create();
            dialog.show();
            return;
        }

        if(!adapter.isEnabled()) {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("提示")
                    .setMessage("蓝牙设备未打开,请开启此功能后重试!")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(mIntent, 1);
                        }
                    })
                    .create();
            dialog.show();
        }
    }
}
