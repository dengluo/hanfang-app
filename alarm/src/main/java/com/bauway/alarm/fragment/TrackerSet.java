package com.bauway.alarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.activity.ConnectDevice;
import com.bauway.alarm.activity.RingSet;
import com.bauway.alarm.base.BaseFragment;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.interfaces.DialogCallback;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.MyNumberPicker.MyNumberPicker;
import com.bauway.alarm.util.MyNumberPicker.MyNumberPickerTools;
import com.bauway.alarm.util.PreferencesUtils;
import com.bauway.alarm.util.Tools;
import com.bestmafen.smablelib.component.SmaManager;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhaotaotao on 2017/12/14.
 * <p>
 * 防丢器设置
 */

public class TrackerSet extends BaseFragment {

    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.line1)
    View mLine1;
    @BindView(R.id.num_picker_hour)
    MyNumberPicker mNumPickerHour;
    @BindView(R.id.num_picker_hour_unit)
    MyNumberPicker mNumPickerHourUnit;
    @BindView(R.id.num_picker_min)
    MyNumberPicker mNumPickerMin;
    @BindView(R.id.num_picker_min_unit)
    MyNumberPicker mNumPickerMinUnit;
    @BindView(R.id.device_voltage)
    TextView mDeviceVoltage;
    @BindView(R.id.bt_switch_vibration)
    SwitchCompat mBtSwitchVibration;
    @BindView(R.id.bt_switch)
    LinearLayout mBtSwitch;
    @BindView(R.id.switch_alarm)
    SwitchCompat mSwitchAlarm;
    @BindView(R.id.tv_bind_status)
    TextView mTvBindStatus;
    @BindView(R.id.bt_bind_device)
    TextView mBtBindDevice;
    Unbinder unbinder;

    private String[] num, hourMinutes;

    private SmaManager mSmaManager;

    public static TrackerSet newInstance() {
        return new TrackerSet();
    }

    @Override
    protected void initView(View mView) {
        num = Tools.getNum(0, 100);
        mNumPickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerHour.setDisplayedValues(num);
        mNumPickerHour.setMinValue(0);
        mNumPickerHour.setMaxValue(num.length - 1);
        mNumPickerHour.setValue(0);
        mNumPickerHour.setWrapSelectorWheel(true);

        mNumPickerHourUnit.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerHourUnit.setDisplayedValues(new String[]{"Hour"});
        mNumPickerHourUnit.setMinValue(0);
        mNumPickerHourUnit.setWrapSelectorWheel(false);

        hourMinutes = Tools.getHourMinutes();
        mNumPickerMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerMin.setDisplayedValues(hourMinutes);
        mNumPickerMin.setMinValue(0);
        mNumPickerMin.setMaxValue(hourMinutes.length - 1);
        mNumPickerMin.setValue(0);
        mNumPickerMin.setWrapSelectorWheel(true);

        mNumPickerMinUnit.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerMinUnit.setDisplayedValues(new String[]{"min"});
        mNumPickerMinUnit.setMinValue(0);
        mNumPickerMinUnit.setWrapSelectorWheel(false);

        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerHour);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerHourUnit);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerMin);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerMinUnit);
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.f_tracker_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance();
    }

    @OnClick({R.id.bt_switch_vibration, R.id.bt_switch,
            R.id.bt_bind_device, R.id.switch_alarm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_switch_vibration:
                break;
            case R.id.bt_switch:
                startActivity(new Intent(mContext, RingSet.class));
                break;
            case R.id.switch_alarm:
                break;
            case R.id.bt_bind_device:
                if (TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0])) {
                    startActivity(new Intent(mContext, ConnectDevice.class));
                } else {
                    DialogUtil.defaultDialog(mContext, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    SmaManager.getInstance().unbind();
                                    PreferencesUtils.putInt(mContext, MyConstants.DEVICE_TYPE, -1);
                                    getActivity().finish();
                                }
                            });
                }
                break;
        }
    }
}
