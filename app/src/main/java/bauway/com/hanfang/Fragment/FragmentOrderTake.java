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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.BuildConfig;
import bauway.com.hanfang.R;
import bauway.com.hanfang.activity.BindDeviceActivity;
import bauway.com.hanfang.activity.DeviceListActivity;
import bauway.com.hanfang.activity.DeviceSettingActivity;
import bauway.com.hanfang.activity.ValidateActivity;
import bauway.com.hanfang.adapter.MyFragmentPagerAdapter;
import bauway.com.hanfang.interfaces.DialogCallback;
import bauway.com.hanfang.util.DateUtils;
import bauway.com.hanfang.util.DialogUtil;
import bauway.com.hanfang.util.NetworkUtil;
import bauway.com.hanfang.util.ToastUtil;
import bauway.com.hanfang.zxing.activity.CaptureActivity;
import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

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
    private TextView tv_frag_wendu, tv_frag_time, tv_frag_fengsu, tv_frag_device_name, tv_frag_device_ypcode;
    private ImageView iv_device_drug_codesss, iv_device_bluetooth, iv_find_play1;
    private ViewPager vpager;
    private CheckBox checkbox_dengguang;

    private MyFragmentPagerAdapter mAdapter;
    private SmaManager mSmaManager;
    private Boolean iswork = true;
    private LinearLayout ll_hongguang;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public RxSharedPreferences userRxPreferences;

    int num = 0;
    private Boolean isok = true;//是否没有验证,并且已经过期

    public static Handler mHandler;//接受MyFragment1发送过来的消息
    String getProduct = "";

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = this.getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mSmaManager = SmaManager.getInstance().init(context).addSmaCallback(new SimpleSmaCallback() {

                @Override
                public void onConnected(BluetoothDevice device, boolean isConnected) {
                    if (isConnected) {
                        Log.e("device", "==device==" + device.getName() + "==" + device.getAddress());
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
                    try {
                        getProduct = new String(data, "Utf-8").trim();
                        Log.e("read==", getProduct.trim() + "///");
                        if (getProduct.equals("00mini.")) {
                            Message message = new Message();
                            message.what = 9;
                            message.obj = getProduct;
                            FragmentOrderTake.mHandler.sendMessage(message);

                            Message message2 = new Message();
                            message2.what = 9;
                            message2.obj = getProduct;
                            MyFragment1.mHandler.sendMessage(message2);
                        } else {
                            Message message = new Message();
                            message.what = 19;
                            message.obj = getProduct;
                            FragmentOrderTake.mHandler.sendMessage(message);

                            Message message2 = new Message();
                            message2.what = 19;
                            message2.obj = getProduct;
                            MyFragment1.mHandler.sendMessage(message2);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        mSmaManager.connect(true);

        mSmaManager = SmaManager.getInstance();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 19:
                        ll_hongguang.setVisibility(View.VISIBLE);
                        break;
                    case 9:
                        ll_hongguang.setVisibility(View.GONE);
                        break;
                    case 10:
                        tv_frag_device_ypcode.setText(msg.obj.toString());
                        break;
                    case 11:
                        SharedPreferences sharedPreferencesWendu1 = context.getSharedPreferences(
                                "MWENDU", Activity.MODE_PRIVATE);
                        String mwendu1 = sharedPreferencesWendu1.getString("mwendu1", "六");
                        tv_frag_wendu.setText(mwendu1 + " 档");
                        Log.e("mSmaManager==name==", mSmaManager.getNameAndAddress().toString() + "||" + mSmaManager.getNameAndAddress()[0]);
                        if (!mSmaManager.isConnected) {
                            ToastUtils.showShortSafe(R.string.device_not_connected);
                            return;
                        }
                        if (mwendu1.equals("一")) {
                            mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, 1 + "");
                        } else if (mwendu1.equals("二")) {
                            mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, 2 + "");
                        } else if (mwendu1.equals("三")) {
                            mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, 3 + "");
                        } else if (mwendu1.equals("四")) {
                            mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, 4 + "");
                        } else if (mwendu1.equals("五")) {
                            mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, 5 + "");
                        } else if (mwendu1.equals("六")) {
                            mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, 6 + "");
                        } else {

                        }
                        break;
                    case 12:
                        SharedPreferences sharedPreferencesTime1 = context.getSharedPreferences(
                                "MTIME", Activity.MODE_PRIVATE);
                        String mtime1 = sharedPreferencesTime1.getString("mtime1", "30");
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
                        String mfengsu1 = sharedPreferencesFengsu1.getString("mfengsu1", "高");
                        tv_frag_fengsu.setText(mfengsu1 + " 档");
                        if (!mSmaManager.isConnected) {
                            ToastUtils.showShortSafe(R.string.device_not_connected);
                            return;
                        }
                        if (mfengsu1.equals("低")) {
                            mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, 1 + "");
                        } else if (mfengsu1.equals("中")) {
                            mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, 2 + "");
                        } else if (mfengsu1.equals("高")) {
                            mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, 3 + "");
                        } else {

                        }

                        break;
                    case 14:
                        Log.e("14", "14");
                        mSmaManager.connect(true);
                        mSmaManager.isConnected = true;
//                        mSmaManager.mEaseConnector.connect(true);
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
                        mSmaManager.write(SmaManager.SET.GET_PRODUCT);

                        break;
                    case 15:
                        Log.e("15", "15");
                        if (mSmaManager.getNameAndAddress()[0].equals("")) {
                            tv_frag_device_name.setText("未连接");
                        } else {
                            tv_frag_device_name.setText(mSmaManager.getNameAndAddress()[0]);
                        }
                        break;
                    case 21:
                        if (mSmaManager.getNameAndAddress()[0].equals("")) {
                            tv_frag_device_name.setText("未连接");
                        } else {
                            if (msg.obj.equals("1")) {
                                tv_frag_wendu.setText("一 档");
                            } else if (msg.obj.equals("2")) {
                                tv_frag_wendu.setText("二 档");
                            } else if (msg.obj.equals("3")) {
                                tv_frag_wendu.setText("三 档");
                            } else if (msg.obj.equals("4")) {
                                tv_frag_wendu.setText("四 档");
                            } else if (msg.obj.equals("5")) {
                                tv_frag_wendu.setText("五 档");
                            } else if (msg.obj.equals("6")) {
                                tv_frag_wendu.setText("六 档");
                            }

                        }
                        break;
                    case 22:
                        if (mSmaManager.getNameAndAddress()[0].equals("")) {
                            tv_frag_device_name.setText("未连接");
                        } else {
                            if (msg.obj.equals("1")) {
                                tv_frag_fengsu.setText("低 档");
                            } else if (msg.obj.equals("2")) {
                                tv_frag_fengsu.setText("中 档");
                            } else if (msg.obj.equals("3")) {
                                tv_frag_fengsu.setText("高 档");
                            }
                        }
                        break;
                    case 23:
                        if (mSmaManager.getNameAndAddress()[0].equals("")) {
                            tv_frag_device_name.setText("未连接");
                        } else {
                            tv_frag_time.setText(msg.obj + " min");
                        }
                        break;
                }
            }
        };
        inintView();
        if (userRxPreferences == null) {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
        queryData();
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
                if (checkbox_dengguang.isChecked()) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        checkbox_dengguang.setChecked(false);
                        return;
                    }
                    ToastUtil.showShortToast(context, "open");
                    mSmaManager.write(SmaManager.SET.ENABLE_GOAL_LIGHT);
                } else {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        checkbox_dengguang.setChecked(false);
                        return;
                    }
//                    ToastUtil.showShortToast(context, "close");
                    mSmaManager.write(SmaManager.SET.DISABLE_GOAL_LIGHT);
                }
            }
        });
        tv_frag_wendu = (TextView) view_main.findViewById(R.id.tv_frag_wendu);
        tv_frag_time = (TextView) view_main.findViewById(R.id.tv_frag_time);
        tv_frag_fengsu = (TextView) view_main.findViewById(R.id.tv_frag_fengsu);
        tv_frag_device_name = (TextView) view_main.findViewById(R.id.tv_frag_device_name);
        tv_frag_device_ypcode = (TextView) view_main.findViewById(R.id.tv_frag_device_ypcode);
        ll_hongguang = (LinearLayout) view_main.findViewById(R.id.ll_hongguang);

        if (mSmaManager.getNameAndAddress()[0].equals("")) {
            tv_frag_device_name.setText("未连接");
        } else {
            tv_frag_device_name.setText(mSmaManager.getNameAndAddress()[0]);

        }
        tv_frag_device_name.setOnClickListener(this);
        if (mSmaManager.getNameAndAddress()[0].equals("")) {
            tv_frag_device_ypcode.setText("");
        } else {
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
            case R.id.tv_frag_device_name:
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }
                EditText et = new EditText(context);
                editname(et);
                break;
            case R.id.iv_device_drug_codesss:
                if (isok){
                    startActivity(new Intent(context, CaptureActivity.class).putExtra("shebei", "device1"));
                }else {
                    ToastUtils.showShortSafe(R.string.tip_validate);
                }
                break;
            case R.id.iv_device_bluetooth:
                if (isok){
                    startActivity(new Intent(context, DeviceListActivity.class));
                }else {
                    ToastUtils.showShortSafe(R.string.tip_validate);
                }
                break;
            case R.id.iv_find_play1:
                if (!isok){
                    ToastUtils.showShortSafe(R.string.tip_validate);
                    return;
                }
                checkBluetoothValid();
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }
                if (tv_frag_device_ypcode.getText().toString().trim().equals("")) {
                    ToastUtils.showShortSafe(R.string.code_not_scan);
                    return;
                }
                if (iswork) {
                    iv_find_play1.setBackgroundResource(R.drawable.pause);
                    iswork = false;
                } else {
                    iv_find_play1.setBackgroundResource(R.drawable.play);
                    iswork = true;
                }
                mSmaManager.write(SmaManager.SET.PLAY_WORKE);
                break;
        }
    }

    private void checkBluetoothValid() {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("错误").setMessage("你的设备不具备蓝牙功能!").create();
            dialog.show();
            return;
        }

        if (!adapter.isEnabled()) {
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

    /*
        Bmob查询数据
         */
    public void queryData() {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            ToastUtil.showShortToast(context, "网络连接异常!");
            return;
        }
        String phone = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        BmobQuery query = new BmobQuery("_User");
        query.addWhereEqualTo("username", phone);
        query.setLimit(2);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + ary.toString());
                    try {
                        JSONObject object = (JSONObject) ary.get(0);
                        Log.i("createdAt", object.optString("createdAt"));
                        isVerify(object);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private Boolean isVerify(JSONObject object) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            DateUtils.daysBetween(DateUtils.ConverToDate(object.optString("createdAt")), DateUtils.ConverToDate(simpleDateFormat.format(date)));
            Log.i("createdAt2", "" + DateUtils.daysBetween(DateUtils.ConverToDate(object.optString("createdAt")), DateUtils.ConverToDate(simpleDateFormat.format(date))));
            Log.i("SMSBOOL", object.optBoolean("SMSBOOL") + "");
            num = DateUtils.daysBetween(DateUtils.ConverToDate(object.optString("createdAt")), DateUtils.ConverToDate(simpleDateFormat.format(date)));
            if (num > 30 && !object.optBoolean("SMSBOOL")) {
                DialogUtil.defaultDialog(context, getString(R.string.confirm_validate), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //验证
                                startActivity(new Intent(context, ValidateActivity.class));
                            }
                        });
                isok = false;
                return true;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

//    View contents = View.inflate(context, R.layout.change_device_name, null);
//    EditText et = (EditText) contents.findViewById(R.id.et_device_name);
    private void editname(final EditText et){
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("修改设备蓝牙名称");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //按下确定键后的事件
//                                    Toast.makeText(context, et.getText().toString(),Toast.LENGTH_LONG).show();
                if (et.getText().toString().trim().equals("")){
                    ToastUtils.showShortSafe("蓝牙名称不能为空");
                }else {
                    mSmaManager.write1(SmaManager.SET.EDIT_DEVICE_BLUETOOTH_NAME,et.getText().toString().trim().getBytes());
                    tv_frag_device_name.setText(et.getText().toString().trim());
                }
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create();
        builder.show();
    }
}
