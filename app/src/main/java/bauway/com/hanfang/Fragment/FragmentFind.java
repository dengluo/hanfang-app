package bauway.com.hanfang.Fragment;

import android.app.Activity;
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

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import bauway.com.hanfang.App.Constants;
import bauway.com.hanfang.activity.DeviceSettingActivity;
import bauway.com.hanfang.R;

/**
 * Created by shun8 on 2017/12/28.
 */

public class FragmentFind extends Fragment implements View.OnClickListener{

    private static final String TAG = "FragmentFind";
    public static final int REQUEST_CODE_BIND = 1;
    private Context context;
    private View view_main;
    public RxSharedPreferences userRxPreferences;
    private ImageView iv_device_state;
    private TextView tv_device_name1;
    private String mStrDevicename1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        context = this.getActivity();
        inintView();
        initDate();
        return view_main;
    }

    private void inintView() {
        view_main = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_find, null);
        iv_device_state = (ImageView) view_main.findViewById(R.id.iv_device_state);
        tv_device_name1 = (TextView) view_main.findViewById(R.id.tv_device_name1);
        iv_device_state.setOnClickListener(this);

    }

    private void initDate() {
        if (userRxPreferences == null) {
            SharedPreferences preferences = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
        String accountname = userRxPreferences.getString(Constants.LOGIN_EMAIL).get();
        String pwd = userRxPreferences.getString(Constants.LOGIN_PWD).get();
        Log.e(TAG, accountname+"//"+pwd);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "DEVICE", Activity.MODE_PRIVATE);
        mStrDevicename1 = sharedPreferences.getString("deviceName1", "");
        tv_device_name1.setText(mStrDevicename1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_device_state:
                Intent intent = new Intent(context, DeviceSettingActivity.class);
                startActivity(intent);
//                if (TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0])) {
//                    startActivityForResult(new Intent(context, ConnectDevice.class), REQUEST_CODE_BIND);
//                } else {
//                    DialogUtil.defaultDialog(context, getString(R.string.confirm_unbind_device), null, null, new
//                            DialogCallback() {
//
//                                @Override
//                                public void execute(Object dialog, Object content) {
//                                    //确认解绑
//                                    mSmaManager.unbind();
//                                    updateBindStatus(false);
//                                }
//                            });
//                }
                break;
        }
    }

}
