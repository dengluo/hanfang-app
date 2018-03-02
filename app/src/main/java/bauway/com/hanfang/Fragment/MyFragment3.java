package bauway.com.hanfang.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bestmafen.easeblelib.util.L;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import bauway.com.hanfang.MyApplication;
import bauway.com.hanfang.R;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment3 extends Fragment {
    private WheelView wv_time_setting;
    private String mFengsu1 = "";
    private Context context;
    public static Handler mHandler;//接受MainActivity2发送过来的消息

    public MyFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content3, container, false);
        context = this.getActivity();
        wv_time_setting = (WheelView) view.findViewById(R.id.wv_time_setting);
        initWheelTime(2);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 22:
                        Log.e("pos--",msg.arg1+"");
                        initWheelTime(msg.arg1-1);
//                        wv_time_setting.setSelection(msg.arg1-1);
                        break;

                }
            }
        };
        return view;
    }

    private List<String> createMainDatas() {
        String[] strings = {"低", "中", "高"};
        return Arrays.asList(strings);
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 23; i++) {
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
        wv_time_setting.setWheelData(createMainDatas());
        wv_time_setting.setStyle(style);
        wv_time_setting.setExtraText("", Color.parseColor("#3F51B5"), 40, 70);
        wv_time_setting.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mFengsu1 = o.toString();
                SharedPreferences sp1 = context.getSharedPreferences("MFENGSU",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp1.edit();
                editor1.putString("mfengsu1", mFengsu1);
                editor1.commit();
                Message message = new Message();
                message.what = 13;
                FragmentOrderTake.mHandler.sendMessage(message);
            }
        });
    }
}
