package com.alan.serialportlib;

/**
 * @author qujiantao
 */
public interface OnSerialPortDataListener {

    void onDataReceived(byte[] bytes, int length, String hexData);

    void onDataSend(byte[] bytes, int length, String hexData);
}
