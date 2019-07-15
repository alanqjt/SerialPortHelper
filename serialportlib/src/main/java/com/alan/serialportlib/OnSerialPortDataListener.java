package com.alan.serialportlib;

public interface OnSerialPortDataListener {

    void onDataReceived(byte[] bytes, int length, String hexData);

    void onDataSent(byte[] bytes, int length, String hexData);
}
