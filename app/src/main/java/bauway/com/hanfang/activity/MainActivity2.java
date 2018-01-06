package bauway.com.hanfang.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bestmafen.easeblelib.util.EaseUtils;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bauway.com.hanfang.Fragment.FragmentOrderTake;
import bauway.com.hanfang.adapter.FragmentTabAdapter;
import bauway.com.hanfang.base.BaseActivity;
import bauway.com.hanfang.BuildConfig;
import bauway.com.hanfang.Fragment.FragmentFind;
import bauway.com.hanfang.Fragment.FragmentMe;
import bauway.com.hanfang.Fragment.FragmentStudio;
import bauway.com.hanfang.R;
import bauway.com.hanfang.util.PreferencesUtils;
import butterknife.BindView;
import cn.bmob.v3.BmobUser;

public class MainActivity2 extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity2";

    @BindView(R.id.rg_main)
    RadioGroup rg_main;
    @BindView(R.id.radio_studio)
    RadioButton radio_studio;
    @BindView(R.id.radio_order_take)
    RadioButton radio_order_take;
    @BindView(R.id.radio_find)
    RadioButton radio_find;
    @BindView(R.id.radio_me)
    RadioButton radio_me;

    FragmentTabAdapter tabAdapter;
    private SmaManager mSmaManager;
    private TextView mTvDebug;
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");
    String strDevice1 = "";


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

    @Override
    protected void initView() {
        Intent intent =getIntent();
        strDevice1 = intent.getStringExtra("deviceName1");
        tabAdapter = new FragmentTabAdapter(this,
                getFragments(), R.id.fl_main, rg_main);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().init(this).addSmaCallback(new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (BuildConfig.DEBUG) {
                    append("  ->  isConnected " + isConnected);
                }
            }

            @Override
            public void onWrite(byte[] data) {
                if (BuildConfig.DEBUG) {
                    append("  ->  onWrite", data);
                }
            }

            @Override
            public void onRead(byte[] data) {
                if (BuildConfig.DEBUG) {
                    append("  ->  onRead", data);
                }
            }
        });
        mSmaManager.connect(true);
        if (BuildConfig.DEBUG) {
//            mDebugViewManager = new DebugViewManager(this);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private List<Fragment> getFragments() {
        List<Fragment> list_fm = new ArrayList<Fragment>();
        FragmentStudio fg_studio=new FragmentStudio();
//        fg_studio.setDoGo(new DoGoJieDan() {
//
//            @Override
//            public void doGo() {
//                // TODO Auto-generated method stub
//                radio_order_take.setChecked(true);
//            }
//        });
        list_fm.add(fg_studio);// 工作室
        list_fm.add(new FragmentOrderTake());// 接单
        list_fm.add(new FragmentFind());// 发现
        list_fm.add(new FragmentMe());// 我
        return list_fm;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.btn:
//                startActivity(new Intent(this, CaptureActivity.class));
//                break;
//            case R.id.start_bluetooth:
//                //startActivity(new Intent(this, BluetoothActivity.class));
//                startActivity(new Intent(this, DeviceScanActivity.class));
//                break;
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
        mSmaManager.exit();
        super.onDestroy();
    }
}
