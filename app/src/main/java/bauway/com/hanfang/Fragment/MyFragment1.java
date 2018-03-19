package bauway.com.hanfang.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bestmafen.easeblelib.util.L;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bauway.com.hanfang.MyApplication;
import bauway.com.hanfang.R;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment1 extends Fragment {
    private WheelView wv_time_setting;
    private String mWendu1 = "";
    private Context context;

    public static Handler mHandler;//接受MainActivity2发送过来的消息

    public MyFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content1, container, false);
        context = this.getActivity();
        wv_time_setting = (WheelView) view.findViewById(R.id.wv_time_setting);
        initWheelTime(5);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 19:
                        Log.e("pos19--",msg.arg1+"");
                        if (!msg.obj.equals("00mini.")){
//                            initWheelTime(msg.arg1-1);
                        }
                        break;
                    case 9:
                        Log.e("pos9--",msg.arg1+"");
                        if (msg.obj.equals("00mini.")){
                            initWheelTime1(msg.arg1-1);
                        }
                        break;
                    case 22:
                        Log.e("pos22--",msg.arg1+"");
                        initWheelTime(msg.arg1-1);
                        break;

                }
            }
        };

        return view;
    }

    private List<String> createTimes() {
        String[] strings = {"一", "二", "三", "四", "五", "六"};
        return Arrays.asList(strings);
    }

    private List<String> createTimes1() {
        String[] strings = {"一", "二", "三"};
        return Arrays.asList(strings);
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 7; i++) {
//            if (i < 10) {
//                list.add("0" + i);
//            } else {
//                list.add("" + i);
//            }
            list.add("" + i);
        }
        return list;
    }

    /**
     * common皮肤、图文混排无皮肤、自定义布局
     */
    private void initWheelTime(int pos) {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#60212121");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 24;

        wv_time_setting.setWheelAdapter(new ArrayWheelAdapter(MyApplication.getInstance()));
        wv_time_setting.setLoop(true);
        wv_time_setting.setWheelSize(3);
        wv_time_setting.setSelection(pos);
        wv_time_setting.setSkin(WheelView.Skin.Holo);
        wv_time_setting.setWheelData(createTimes());
        wv_time_setting.setStyle(style);
        wv_time_setting.setExtraText("", Color.parseColor("#3F51B5"), 40, 70);
        wv_time_setting.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mWendu1 = o.toString();
                Log.e("mWendu1==",mWendu1);
                SharedPreferences sp1 = context.getSharedPreferences("MWENDU",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp1.edit();
                editor1.putString("mwendu1", mWendu1);
                editor1.commit();
                Message message = new Message();
                message.what = 11;
                FragmentOrderTake.mHandler.sendMessage(message);
            }
        });

    }

    private void initWheelTime1(int pos) {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#60212121");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 24;

        wv_time_setting.setWheelAdapter(new ArrayWheelAdapter(MyApplication.getInstance()));
        wv_time_setting.setLoop(true);
        wv_time_setting.setWheelSize(3);
        wv_time_setting.setSelection(pos);
        wv_time_setting.setSkin(WheelView.Skin.Holo);
        wv_time_setting.setWheelData(createTimes1());
        wv_time_setting.setStyle(style);
        wv_time_setting.setExtraText("", Color.parseColor("#3F51B5"), 40, 70);
        wv_time_setting.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mWendu1 = o.toString();
                Log.e("mWendu1==",mWendu1);
                SharedPreferences sp1 = context.getSharedPreferences("MWENDU",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp1.edit();
                editor1.putString("mwendu1", mWendu1);
                editor1.commit();
                Message message = new Message();
                message.what = 11;
                FragmentOrderTake.mHandler.sendMessage(message);
            }
        });

    }
}
