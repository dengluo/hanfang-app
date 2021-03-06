package com.bestmafen.smablelib.component;

import android.bluetooth.BluetoothDevice;

/**
 * This class is used to receive the data from a Bluetooth device,but we use {@link SimpleSmaCallback} replaced in normal
 * conditions.
 */
public interface SmaCallback {

    /**
     * This method will be invoked when a remote device gets connected.
     *
     * @param device the remote device which gets connected
     */
    void onConnected(BluetoothDevice device, boolean isConnected);

    void notConnected();

    /**
     * 读到电压
     *
     * @param voltage 电压值
     */
    void onReadVoltage(float voltage);

    /**
     * 读到累计口数
     *
     * @param puff 累计口数
     */
    void onReadPuffCount(int puff);

    /**
     * 读到累计支数
     *
     * @param count 累计支数
     */
    void onReadSmokeCount(int count);

    /**
     * 即将休眠
     */
    void onBeingDormant();

    /**
     * 产品区分
     */
    void onReadProduct(int count);

    /**
     * 蓝牙设备名称
     */
    void onReadDeviceName(byte[] data);

    /**
     * 风速
     */
    void onReadFengsu(int count);

    /**
     * 温度
     */
    void onReadWendu(int count);

    /**
     * 时间
     */
    void onReadTime(int count);

    /**
     * 授权使用时间
     */
    void onReadEmpowerTime(int count);

    /**
     * 授权使用次数
     */
    void onReadEmpowerCount(int count);



    /**
     * 正在充电
     *
     * @param voltage 充电时的电压值
     */
    void onCharging(float voltage);

    /**
     * NTC开短路
     *
     * @param ntc
     */
    void onNtcSwitch(boolean ntc);

    /**
     * 读到温度
     *
     * @param temperature 温度
     */
    void onReadTemperature(int temperature);

    void onWrite(byte[] data);

    void onRead(byte[] data);

    void onReadChargeCount(int count);

    void onReadHopes(byte[] data);

    void onReadHopem(byte[] data);

}
