package com.bestmafen.smablelib.component;

import android.bluetooth.BluetoothDevice;

/**
 * This class provides empty implementations of the methods from {@link SmaCallback}.Override this if you only care about
 * a few
 * of the available callback methods.
 */
public class SimpleSmaCallback implements SmaCallback {

    @Override
    public void onConnected(BluetoothDevice device, boolean isConnected) {

    }

    @Override
    public void notConnected() {

    }

    @Override
    public void onReadVoltage(float voltage) {

    }

    @Override
    public void onReadPuffCount(int puff) {

    }

    @Override
    public void onReadSmokeCount(int count) {

    }

    @Override
    public void onBeingDormant() {

    }

    @Override
    public void onReadDeviceName(byte[] name) {

    }

    @Override
    public void onReadProduct(int count) {

    }

    @Override
    public void onReadFengsu(int count) {

    }

    @Override
    public void onReadWendu(int count) {

    }

    @Override
    public void onReadTime(int count) {

    }

    @Override
    public void onReadEmpowerTime(int count) {

    }

    @Override
    public void onReadEmpowerCount(int count) {

    }

    @Override
    public void onCharging(float voltage) {

    }

    @Override
    public void onNtcSwitch(boolean ntc) {

    }

    @Override
    public void onReadTemperature(int temperature) {

    }

    @Override
    public void onWrite(byte[] data) {

    }

    @Override
    public void onRead(byte[] data) {

    }

    @Override
    public void onReadChargeCount(int count) {

    }

    @Override
    public void onReadHopes(byte[] data) {

    }

    @Override
    public void onReadHopem(byte[] data) {

    }
}
