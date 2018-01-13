package bauway.com.hanfang.Fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestmafen.easeblelib.util.L;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.BuildConfig;
import bauway.com.hanfang.activity.DeviceSettingActivity;
import bauway.com.hanfang.R;
import bauway.com.hanfang.activity.DeviceSettingActivity2;
import bauway.com.hanfang.activity.DeviceSettingActivity3;
import bauway.com.hanfang.activity.DeviceSettingActivity4;
import bauway.com.hanfang.activity.DeviceSettingActivity5;
import bauway.com.hanfang.interfaces.DialogCallback;
import bauway.com.hanfang.util.DialogUtil;

/**
 * Created by shun8 on 2017/12/28.
 */

public class FragmentFind extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragmentFind";
    public static final int REQUEST_CODE_BIND = 1;
    private Context context;
    private View view_main;
    public RxSharedPreferences userRxPreferences;
    private ImageView iv_device_state1, iv_device_state2, iv_device_state3, iv_device_state4, iv_device_state5;
    private TextView tv_device_name1, tv_device_wendu1, tv_device_fengsu1, tv_device_time1;
    private TextView tv_device_name2, tv_device_wendu2, tv_device_fengsu2, tv_device_time2;
    private TextView tv_device_name3, tv_device_wendu3, tv_device_fengsu3, tv_device_time3;
    private TextView tv_device_name4, tv_device_wendu4, tv_device_fengsu4, tv_device_time4;
    private TextView tv_device_name5, tv_device_wendu5, tv_device_fengsu5, tv_device_time5;
    private TextView tv_find_drug_code1, tv_find_drug_code2, tv_find_drug_code3, tv_find_drug_code4, tv_find_drug_code5;
    private String mStrDevicename1 = "未连接";
    private String mStrDevicename2 = "未连接";
    private String mStrDevicename3 = "未连接";
    private String mStrDevicename4 = "未连接";
    private String mStrDevicename5 = "未连接";
    private String scanResult1, scanResult2, scanResult3, scanResult4, scanResult5;
    private SmaManager mSmaManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = this.getActivity();
        mSmaManager = SmaManager.getInstance().init(context).addSmaCallback(new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (BuildConfig.DEBUG) {

                }
            }

            @Override
            public void onWrite(byte[] data) {
                if (BuildConfig.DEBUG) {

                }
            }

            @Override
            public void onRead(byte[] data) {
                if (BuildConfig.DEBUG) {

                }
            }
        });
        mSmaManager.connect(true);
        if (BuildConfig.DEBUG) {
//            mDebugViewManager = new DebugViewManager(this);
        }
        inintView();
        initDate();
        return view_main;
    }

    private void inintView() {
        view_main = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_find, null);
        iv_device_state1 = (ImageView) view_main.findViewById(R.id.iv_device_state1);
        tv_device_name1 = (TextView) view_main.findViewById(R.id.tv_device_name1);
        tv_device_wendu1 = (TextView) view_main.findViewById(R.id.tv_device_wendu1);
        tv_device_fengsu1 = (TextView) view_main.findViewById(R.id.tv_device_fengsu1);
        tv_find_drug_code1 = (TextView) view_main.findViewById(R.id.tv_find_drug_code1);

        iv_device_state2 = (ImageView) view_main.findViewById(R.id.iv_device_state2);
        tv_device_name2 = (TextView) view_main.findViewById(R.id.tv_device_name2);
        tv_device_wendu2 = (TextView) view_main.findViewById(R.id.tv_device_wendu2);
        tv_device_fengsu2 = (TextView) view_main.findViewById(R.id.tv_device_fengsu2);
        tv_find_drug_code2 = (TextView) view_main.findViewById(R.id.tv_find_drug_code2);

        iv_device_state3 = (ImageView) view_main.findViewById(R.id.iv_device_state3);
        tv_device_name3 = (TextView) view_main.findViewById(R.id.tv_device_name3);
        tv_device_wendu3 = (TextView) view_main.findViewById(R.id.tv_device_wendu3);
        tv_device_fengsu3 = (TextView) view_main.findViewById(R.id.tv_device_fengsu3);
        tv_find_drug_code3 = (TextView) view_main.findViewById(R.id.tv_find_drug_code3);

        iv_device_state4 = (ImageView) view_main.findViewById(R.id.iv_device_state4);
        tv_device_name4 = (TextView) view_main.findViewById(R.id.tv_device_name4);
        tv_device_wendu4 = (TextView) view_main.findViewById(R.id.tv_device_wendu4);
        tv_device_fengsu4 = (TextView) view_main.findViewById(R.id.tv_device_fengsu4);
        tv_find_drug_code4 = (TextView) view_main.findViewById(R.id.tv_find_drug_code4);

        iv_device_state5 = (ImageView) view_main.findViewById(R.id.iv_device_state5);
        tv_device_name5 = (TextView) view_main.findViewById(R.id.tv_device_name5);
        tv_device_wendu5 = (TextView) view_main.findViewById(R.id.tv_device_wendu5);
        tv_device_fengsu5 = (TextView) view_main.findViewById(R.id.tv_device_fengsu5);
        tv_find_drug_code5 = (TextView) view_main.findViewById(R.id.tv_find_drug_code5);

        tv_device_time1 = (TextView) view_main.findViewById(R.id.tv_device_time1);
        iv_device_state1.setOnClickListener(this);
        tv_device_time2 = (TextView) view_main.findViewById(R.id.tv_device_time2);
        iv_device_state2.setOnClickListener(this);
        tv_device_time3 = (TextView) view_main.findViewById(R.id.tv_device_time3);
        iv_device_state3.setOnClickListener(this);
        tv_device_time4 = (TextView) view_main.findViewById(R.id.tv_device_time4);
        iv_device_state4.setOnClickListener(this);
        tv_device_time5 = (TextView) view_main.findViewById(R.id.tv_device_time5);
        iv_device_state5.setOnClickListener(this);

    }

    private void initDate() {
        if (userRxPreferences == null) {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
        String accountname = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        String pwd = userRxPreferences.getString(Constants.LOGIN_PWD).get();
        Log.e(TAG, accountname + "//" + pwd);
    }

    private synchronized void append(final String value) {

    }

    @Override
    public void onDestroy() {
        mSmaManager.exit();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSmaManager.getNameAndAddress()[0] == null && mSmaManager.getNameAndAddress()[0] == "") {
            tv_device_name1.setText("未连接");
            tv_find_drug_code1.setText("未输入");
            L.e("1111");
        } else {
            L.e("2222");
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    "DEVICE", Activity.MODE_PRIVATE);
            mStrDevicename1 = sharedPreferences.getString("deviceName1", "未连接");
            tv_device_name1.setText(mStrDevicename1);
            SharedPreferences mSpScan = context.getSharedPreferences(
                    "SCAN", Activity.MODE_PRIVATE);
            scanResult1 = mSpScan.getString("scanResult1", "未输入");
            tv_find_drug_code1.setText(scanResult1);
            if (mStrDevicename1.equals("未连接")) {
                iv_device_state1.setBackgroundResource(R.drawable.close);
            } else {
                iv_device_state1.setBackgroundResource(R.drawable.check);
            }
            SharedPreferences sharedPreferencesWendu1 = context.getSharedPreferences(
                    "WENDU", Activity.MODE_PRIVATE);
            String mStrWendu1 = sharedPreferencesWendu1.getString("wendu1", "30");
            tv_device_wendu1.setText(mStrWendu1);
            SharedPreferences sharedPreferencesFengsu1 = context.getSharedPreferences(
                    "FENGSU", Activity.MODE_PRIVATE);
            String mStrFengsu1 = sharedPreferencesFengsu1.getString("fengsu1", "30");
            tv_device_fengsu1.setText(mStrFengsu1);
            SharedPreferences sharedPreferencesTime1 = context.getSharedPreferences(
                    "TIME", Activity.MODE_PRIVATE);
            String mStrTime1 = sharedPreferencesTime1.getString("time1", "30");
            tv_device_time1.setText(mStrTime1);

            //设备2
            SharedPreferences sharedPreferences2 = context.getSharedPreferences(
                    "DEVICE2", Activity.MODE_PRIVATE);
            mStrDevicename2 = sharedPreferences2.getString("deviceName2", "未连接");
            tv_device_name2.setText(mStrDevicename2);

            SharedPreferences mSpScan2 = context.getSharedPreferences(
                    "SCAN", Activity.MODE_PRIVATE);
            scanResult2 = mSpScan2.getString("scanResult2", "未输入");
            tv_find_drug_code2.setText(scanResult2);
            if (mStrDevicename2.equals("未连接")) {
                iv_device_state2.setBackgroundResource(R.drawable.close);
            } else {
                iv_device_state2.setBackgroundResource(R.drawable.check);
            }
            SharedPreferences sharedPreferencesWendu2 = context.getSharedPreferences(
                    "WENDU2", Activity.MODE_PRIVATE);
            String mStrWendu2 = sharedPreferencesWendu2.getString("wendu2", "30");
            tv_device_wendu2.setText(mStrWendu2);
            SharedPreferences sharedPreferencesFengsu2 = context.getSharedPreferences(
                    "FENGSU2", Activity.MODE_PRIVATE);
            String mStrFengsu2 = sharedPreferencesFengsu2.getString("fengsu2", "30");
            tv_device_fengsu2.setText(mStrFengsu2);
            SharedPreferences sharedPreferencesTime2 = context.getSharedPreferences(
                    "TIME2", Activity.MODE_PRIVATE);
            String mStrTime2 = sharedPreferencesTime2.getString("time2", "30");
            tv_device_time2.setText(mStrTime2);

            //设备3
            SharedPreferences sharedPreferences3 = context.getSharedPreferences(
                    "DEVICE3", Activity.MODE_PRIVATE);
            mStrDevicename3 = sharedPreferences3.getString("deviceName3", "未连接");
            tv_device_name3.setText(mStrDevicename3);

            SharedPreferences mSpScan3 = context.getSharedPreferences(
                    "SCAN", Activity.MODE_PRIVATE);
            scanResult3 = mSpScan3.getString("scanResult3", "未输入");
            tv_find_drug_code3.setText(scanResult3);
            if (mStrDevicename3.equals("未连接")) {
                iv_device_state3.setBackgroundResource(R.drawable.close);
            } else {
                iv_device_state3.setBackgroundResource(R.drawable.check);
            }
            SharedPreferences sharedPreferencesWendu3 = context.getSharedPreferences(
                    "WENDU3", Activity.MODE_PRIVATE);
            String mStrWendu3 = sharedPreferencesWendu3.getString("wendu3", "30");
            tv_device_wendu3.setText(mStrWendu3);
            SharedPreferences sharedPreferencesFengsu3 = context.getSharedPreferences(
                    "FENGSU3", Activity.MODE_PRIVATE);
            String mStrFengsu3 = sharedPreferencesFengsu3.getString("fengsu3", "30");
            tv_device_fengsu3.setText(mStrFengsu3);
            SharedPreferences sharedPreferencesTime3 = context.getSharedPreferences(
                    "TIME3", Activity.MODE_PRIVATE);
            String mStrTime3 = sharedPreferencesTime3.getString("time3", "30");
            tv_device_time3.setText(mStrTime3);

            //设备4
            SharedPreferences sharedPreferences4 = context.getSharedPreferences(
                    "DEVICE4", Activity.MODE_PRIVATE);
            mStrDevicename4 = sharedPreferences4.getString("deviceName4", "未连接");
            tv_device_name4.setText(mStrDevicename4);

            SharedPreferences mSpScan4 = context.getSharedPreferences(
                    "SCAN", Activity.MODE_PRIVATE);
            scanResult4 = mSpScan4.getString("scanResult4", "未输入");
            tv_find_drug_code4.setText(scanResult4);
            if (mStrDevicename4.equals("未连接")) {
                iv_device_state4.setBackgroundResource(R.drawable.close);
            } else {
                iv_device_state4.setBackgroundResource(R.drawable.check);
            }
            SharedPreferences sharedPreferencesWendu4 = context.getSharedPreferences(
                    "WENDU4", Activity.MODE_PRIVATE);
            String mStrWendu4 = sharedPreferencesWendu4.getString("wendu4", "30");
            tv_device_wendu4.setText(mStrWendu4);
            SharedPreferences sharedPreferencesFengsu4 = context.getSharedPreferences(
                    "FENGSU4", Activity.MODE_PRIVATE);
            String mStrFengsu4 = sharedPreferencesFengsu4.getString("fengsu4", "30");
            tv_device_fengsu4.setText(mStrFengsu4);
            SharedPreferences sharedPreferencesTime4 = context.getSharedPreferences(
                    "TIME4", Activity.MODE_PRIVATE);
            String mStrTime4 = sharedPreferencesTime4.getString("time4", "30");
            tv_device_time4.setText(mStrTime4);

            //设备5
            SharedPreferences sharedPreferences5 = context.getSharedPreferences(
                    "DEVICE5", Activity.MODE_PRIVATE);
            mStrDevicename5 = sharedPreferences5.getString("deviceName5", "未连接");
            tv_device_name5.setText(mStrDevicename5);

            SharedPreferences mSpScan5 = context.getSharedPreferences(
                    "SCAN", Activity.MODE_PRIVATE);
            scanResult5 = mSpScan5.getString("scanResult5", "未输入");
            tv_find_drug_code5.setText(scanResult5);
            if (mStrDevicename5.equals("未连接")) {
                iv_device_state5.setBackgroundResource(R.drawable.close);
            } else {
                iv_device_state5.setBackgroundResource(R.drawable.check);
            }
            SharedPreferences sharedPreferencesWendu5 = context.getSharedPreferences(
                    "WENDU5", Activity.MODE_PRIVATE);
            String mStrWendu5 = sharedPreferencesWendu5.getString("wendu5", "30");
            tv_device_wendu5.setText(mStrWendu5);
            SharedPreferences sharedPreferencesFengsu5 = context.getSharedPreferences(
                    "FENGSU5", Activity.MODE_PRIVATE);
            String mStrFengsu5 = sharedPreferencesFengsu5.getString("fengsu5", "30");
            tv_device_fengsu5.setText(mStrFengsu5);
            SharedPreferences sharedPreferencesTime5 = context.getSharedPreferences(
                    "TIME5", Activity.MODE_PRIVATE);
            String mStrTime5 = sharedPreferencesTime5.getString("time5", "30");
            tv_device_time5.setText(mStrTime5);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_device_state1:
                if (mStrDevicename1.equals("未连接")) {
                    Intent intent = new Intent(context, DeviceSettingActivity.class);
                    if (tv_device_time1.getText().toString() != "") {
                        intent.putExtra("time", Integer.parseInt(tv_device_time1.getText().toString()));
                    } else {
                        intent.putExtra("time", 30);
                    }
                    if (tv_device_fengsu1.getText().toString() != "") {
                        intent.putExtra("fengsu", Integer.parseInt(tv_device_fengsu1.getText().toString()));
                    } else {
                        intent.putExtra("fengsu", 30);
                    }

                    if (tv_device_wendu1.getText().toString() != "") {
                        intent.putExtra("wendu", Integer.parseInt(tv_device_wendu1.getText().toString()));
                    } else {
                        intent.putExtra("wendu", 30);
                    }
                    startActivity(intent);
                } else {
//                    iv_device_state1.setBackgroundResource(R.drawable.close);
                    DialogUtil.defaultDialog(context, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
                                    SharedPreferences sp = context.getSharedPreferences("DEVICE",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("deviceName1", "未连接");
                                    editor.commit();
                                    iv_device_state1.setBackgroundResource(R.drawable.close);
                                    tv_device_name1.setText("未连接");
                                    tv_find_drug_code1.setText("未输入");
                                    scanResult1 = "未输入";
                                    mStrDevicename1 = "未连接";
                                }
                            });
                }

                break;
            case R.id.iv_device_state2:
                if (mStrDevicename2.equals("未连接")) {
                    Intent intent2 = new Intent(context, DeviceSettingActivity2.class);
                    if (tv_device_time2.getText().toString() != "") {
                        intent2.putExtra("time2", Integer.parseInt(tv_device_time2.getText().toString()));
                    } else {
                        intent2.putExtra("time2", 30);
                    }
                    if (tv_device_fengsu2.getText().toString() != "") {
                        intent2.putExtra("fengsu2", Integer.parseInt(tv_device_fengsu2.getText().toString()));
                    } else {
                        intent2.putExtra("fengsu2", 30);
                    }

                    if (tv_device_wendu2.getText().toString() != "") {
                        intent2.putExtra("wendu2", Integer.parseInt(tv_device_wendu2.getText().toString()));
                    } else {
                        intent2.putExtra("wendu2", 30);
                    }
                    startActivity(intent2);
                } else {
                    DialogUtil.defaultDialog(context, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
                                    SharedPreferences sp = context.getSharedPreferences("DEVICE2",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("deviceName2", "未连接");
                                    editor.commit();
                                    iv_device_state2.setBackgroundResource(R.drawable.close);
                                    tv_device_name2.setText("未连接");
                                    mStrDevicename2 = "未连接";
                                }
                            });
                }

                break;

            case R.id.iv_device_state3:
                if (mStrDevicename3.equals("未连接")) {
                    Intent intent3 = new Intent(context, DeviceSettingActivity3.class);
                    if (tv_device_time3.getText().toString() != "") {
                        intent3.putExtra("time3", Integer.parseInt(tv_device_time3.getText().toString()));
                    } else {
                        intent3.putExtra("time3", 30);
                    }
                    if (tv_device_fengsu3.getText().toString() != "") {
                        intent3.putExtra("fengsu3", Integer.parseInt(tv_device_fengsu3.getText().toString()));
                    } else {
                        intent3.putExtra("fengsu3", 30);
                    }

                    if (tv_device_wendu3.getText().toString() != "") {
                        intent3.putExtra("wendu3", Integer.parseInt(tv_device_wendu3.getText().toString()));
                    } else {
                        intent3.putExtra("wendu3", 30);
                    }
                    startActivity(intent3);
                } else {
                    DialogUtil.defaultDialog(context, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
                                    SharedPreferences sp = context.getSharedPreferences("DEVICE3",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("deviceName3", "未连接");
                                    editor.commit();

                                    SharedPreferences sp2 = context.getSharedPreferences("SCAN",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sp2.edit();
                                    editor.putString("shebei3", "未输入");
                                    editor.commit();
                                    iv_device_state3.setBackgroundResource(R.drawable.close);
                                    tv_device_name3.setText("未连接");
                                    tv_find_drug_code3.setText("未输入");
                                    mStrDevicename3 = "未连接";
                                }
                            });
                }

                break;

            case R.id.iv_device_state4:
                if (mStrDevicename4.equals("未连接")) {
                    Intent intent4 = new Intent(context, DeviceSettingActivity4.class);
                    if (tv_device_time4.getText().toString() != "") {
                        intent4.putExtra("time4", Integer.parseInt(tv_device_time4.getText().toString()));
                    } else {
                        intent4.putExtra("time4", 30);
                    }
                    if (tv_device_fengsu4.getText().toString() != "") {
                        intent4.putExtra("fengsu4", Integer.parseInt(tv_device_fengsu4.getText().toString()));
                    } else {
                        intent4.putExtra("fengsu4", 30);
                    }

                    if (tv_device_wendu4.getText().toString() != "") {
                        intent4.putExtra("wendu4", Integer.parseInt(tv_device_wendu4.getText().toString()));
                    } else {
                        intent4.putExtra("wendu4", 30);
                    }
                    startActivity(intent4);
                } else {
                    DialogUtil.defaultDialog(context, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
                                    SharedPreferences sp = context.getSharedPreferences("DEVICE4",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("deviceName4", "未连接");
                                    editor.commit();
                                    SharedPreferences sp2 = context.getSharedPreferences("SCAN",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sp2.edit();
                                    editor.putString("shebei4", "未输入");
                                    editor.commit();
                                    iv_device_state4.setBackgroundResource(R.drawable.close);
                                    tv_device_name4.setText("未连接");
                                    tv_find_drug_code4.setText("未输入");
                                    mStrDevicename4 = "未连接";

                                }
                            });
                }

                break;

            case R.id.iv_device_state5:
                if (mStrDevicename5.equals("未连接")) {
                    Intent intent5 = new Intent(context, DeviceSettingActivity5.class);
                    if (tv_device_time5.getText().toString() != "") {
                        intent5.putExtra("time5", Integer.parseInt(tv_device_time5.getText().toString()));
                    } else {
                        intent5.putExtra("time5", 30);
                    }
                    if (tv_device_fengsu5.getText().toString() != "") {
                        intent5.putExtra("fengsu5", Integer.parseInt(tv_device_fengsu5.getText().toString()));
                    } else {
                        intent5.putExtra("fengsu5", 30);
                    }

                    if (tv_device_wendu5.getText().toString() != "") {
                        intent5.putExtra("wendu5", Integer.parseInt(tv_device_wendu5.getText().toString()));
                    } else {
                        intent5.putExtra("wendu5", 30);
                    }
                    startActivity(intent5);
                } else {
                    DialogUtil.defaultDialog(context, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
                                    SharedPreferences sp = context.getSharedPreferences("DEVICE5",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("deviceName5", "未连接");
                                    editor.commit();
                                    SharedPreferences sp2 = context.getSharedPreferences("SCAN",
                                            Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sp2.edit();
                                    editor.putString("shebei5", "未输入");
                                    editor.commit();
                                    iv_device_state5.setBackgroundResource(R.drawable.close);
                                    tv_device_name5.setText("未连接");
                                    tv_find_drug_code5.setText("未输入");
                                    mStrDevicename5 = "未连接";
                                }
                            });
                }

                break;
        }
    }

}
