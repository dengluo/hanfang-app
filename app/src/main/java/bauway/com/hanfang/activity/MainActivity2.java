package bauway.com.hanfang.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bestmafen.easeblelib.util.EaseUtils;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bauway.com.hanfang.Fragment.FragmentOrderTake;
import bauway.com.hanfang.Fragment.MyFragment1;
import bauway.com.hanfang.Fragment.MyFragment3;
import bauway.com.hanfang.adapter.FragmentTabAdapter;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.BuildConfig;
import bauway.com.hanfang.Fragment.FragmentMe;
import bauway.com.hanfang.R;
import bauway.com.hanfang.util.PreferencesUtils;
import butterknife.BindView;
import cn.bmob.v3.BmobUser;

public class MainActivity2 extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity2";

    @BindView(R.id.rg_main)
    RadioGroup rg_main;
//    @BindView(R.id.radio_studio)
//    RadioButton radio_studio;
    @BindView(R.id.radio_order_take)
    RadioButton radio_order_take;
//    @BindView(R.id.radio_find)
//    RadioButton radio_find;
    @BindView(R.id.radio_me)
    RadioButton radio_me;

    FragmentTabAdapter tabAdapter;
    private SmaManager mSmaManager;
    private TextView mTvDebug;
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");
    String strDevice1 = "";
    private SharedPreferences sharedPreferences;
    private SmaCallback mSmaCallback;
    private long exitTime = 0;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

        if (BuildConfig.DEBUG) {
//            mDebugViewManager.showDebugWindow();
            findViewById(R.id.v_debug).setVisibility(View.VISIBLE);
            mTvDebug = (TextView) findViewById(R.id.tv_debug);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        //沉浸式状态栏设置
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
//        window.setStatusBarColor(getColor(R.color.blue_ice));
        Intent intent =getIntent();
        strDevice1 = intent.getStringExtra("deviceName1");
        tabAdapter = new FragmentTabAdapter(this,
                getFragments(), R.id.fl_main, rg_main);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void init(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("count",MODE_PRIVATE);
        int count = sharedPreferences.getInt("count",0);
        Log.d("print", String.valueOf(count));
        //判断程序是第几次运行，如果是第一次运行则跳转到引导页面
//        if (count == 0){
//            Intent intent = new Intent();
//            intent.setClass(getApplicationContext(), WelcomeActivity.class);
//            startActivity(intent);
//            this.finish();
//        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //存入数据
        editor.putInt("count",++count);
        //提交修改
        editor.commit();

        mSmaManager = SmaManager.getInstance().init(this).addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (isConnected) {
//                    mSmaManager.write(SmaManager.SET.GET_PRODUCT);
                }
            }



            @Override
            public void onCharging(final float voltage) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        mTvDcv.setText(String.valueOf(voltage));
//                        updateChargeTime();
                    }
                });
            }

            @Override
            public void onReadTemperature(final int temperature) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("temperature==",String.valueOf(temperature));
//                        mTvTemp.setText(String.valueOf(temperature));
                    }
                });
            }

            @Override
            public void onReadPuffCount(final int puff) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        mTvUseNum.setText(String.valueOf(puff));
                    }
                });
            }

            @Override
            public void onReadChargeCount(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        mTvChargeCount.setText(String.valueOf(count));
                    }
                });
            }

            @Override
            public void onReadWendu(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("wendu==",String.valueOf(count));
                        Message message = new Message();
                        message.what = 21;
                        message.obj = String.valueOf(count);
                        FragmentOrderTake.mHandler.sendMessage(message);

                        Message message2 = new Message();
                        message2.what = 22;
                        message2.arg1 = count;
                        MyFragment1.mHandler.sendMessage(message2);
                    }
                });
            }

            @Override
            public void onReadProduct(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("product==",String.valueOf(count));
//                        Message message = new Message();
//                        message.what = 22;
//                        message.obj = String.valueOf(count);
//                        FragmentOrderTake.mHandler.sendMessage(message);
//
//                        Message message2 = new Message();
//                        message2.what = 22;
//                        message2.arg1 = count;
//                        MyFragment3.mHandler.sendMessage(message2);
                    }
                });
            }

            @Override
            public void onReadFengsu(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("fengsu==",String.valueOf(count));
                        Message message = new Message();
                        message.what = 22;
                        message.obj = String.valueOf(count);
                        FragmentOrderTake.mHandler.sendMessage(message);

                        Message message2 = new Message();
                        message2.what = 22;
                        message2.arg1 = count;
                        MyFragment3.mHandler.sendMessage(message2);
                    }
                });
            }

            @Override
            public void onReadTime(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("time==",String.valueOf(count));
                        Message message = new Message();
                        message.what = 23;
                        message.obj = String.valueOf(count);
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }
                });
            }

            @Override
            public void onReadEmpowerTime(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //传递消息,接收设备发来的"授权使用时间"
                        Log.e("empowerTime==",String.valueOf(count));
                        Message message = new Message();
                        message.what = 24;
                        message.arg1 = count;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }
                });
            }

            @Override
            public void onReadEmpowerCount(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //传递消息,接收设备发来的"授权使用次数"
                        Log.e("EmpowerCount==",String.valueOf(count));
                        Message message = new Message();
                        message.what = 25;
                        message.arg1 = count;
                        FragmentOrderTake.mHandler.sendMessage(message);
                    }
                });
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private List<Fragment> getFragments() {
        List<Fragment> list_fm = new ArrayList<Fragment>();
        list_fm.add(new FragmentOrderTake());// 治疗
        list_fm.add(new FragmentMe());// 我
        return list_fm;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    public String getTimeStr() {
        return mDateFormat.format(new Date());
    }

    private String getValue(@NonNull byte[] extra) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] valuePart = Arrays.copyOfRange(extra, 2, 8);
            for (byte b : valuePart) {
                sb.append((char) b);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param type 0写 1读
     * @param data content
     */
    private synchronized void append(final String type, final byte[] data) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTvDebug.append(getTimeStr() + "  " + type + "\n");
                mTvDebug.append(EaseUtils.byteArray2HexString(data));
                mTvDebug.append("  " + getValue(data));
                mTvDebug.append("\n\n");
            }
        });
    }

    private synchronized void append(final String value) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTvDebug.append(getTimeStr() + "\n");
                mTvDebug.append("  " + value);
                mTvDebug.append("\n\n");
            }
        });
    }

    private void exitApp() {
        BmobUser.logOut();
        PreferencesUtils.clearEntity(mContext);
        ToastUtils.cancel();
        myApplication.exit();
        startActivity(new Intent(this, LoginActivity2.class));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSmaManager.removeSmaCallback(mSmaCallback);
//        Log.e("onDestroy","=="+mSmaManager.getNameAndAddress()[0]);
        mSmaManager.exit();
        super.onDestroy();
    }

    /**
     * 重写返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                finish();
//                System.exit(0);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
