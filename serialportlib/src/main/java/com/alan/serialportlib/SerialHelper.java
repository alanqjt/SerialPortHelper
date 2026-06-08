package com.alan.serialportlib;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.DATAB;
import android_serialport_api.FLOWCON;
import android_serialport_api.PARITY;
import android_serialport_api.STOPB;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

/**
 * @author qujiantao
 */
public class SerialHelper {

    private SerialPort serialPort;
    private WriteThread writeThread;
    private ReadThread readThread;

    public static final String TAG = "SerialHelper";

    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private boolean ready = false;

    public boolean serialStart(Parameter parameter) {

        if (ready) {
            Log.e(TAG, "SerialHelper is running");
            return true;
        }

        try {
            serialPort = new SerialPort(new File(parameter.getSerialPath()), parameter.getBaudrate(), parameter.getStopBit(), parameter.getDataBit(), parameter.getParity(), parameter.getFlowCon(), parameter.getFlags(), parameter.getSuPath());
            // 拉高/拉低 DTR、RTS。USB 转串口芯片会把该状态锁存到硬件，
            // 很多 RS232 外设需要这两根线被拉高才会收发数据；板载 UART 无此线，调用会失败属正常。
            boolean modemOk = serialPort.setModemControl(parameter.isDtr(), parameter.isRts());
            if (parameter.isDebug()) {
                Log.d(TAG, "setModemControl dtr=" + parameter.isDtr() + " rts=" + parameter.isRts() + " result=" + modemOk);
            }
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            writeThread = new WriteThread(parameter);
            writeThread.setmOutputStreams(outputStream);

            readThread = new ReadThread(parameter);
            readThread.setmInputStreams(inputStream);
            readThread.start();
            writeThread.start();
            ready = true;
        } catch (Exception e) {
            // 包含 IOException 以及无权限/未 root 时的 SecurityException，
            // 串口打开失败只返回 false，不应让宿主 App 崩溃。
            Log.e(TAG, "serialStart failed: " + parameter.getSerialPath(), e);
            close();
            return false;
        }
        return true;
    }

    public void sendData(byte[] params) {
        if (null != writeThread) {
            if (null != params) {
                writeThread.addToQueue(params);
            }
        } else {
            Log.e(TAG, "sendData must after serialStart");
        }
    }

    public void close() {
        if (null != writeThread) {
            writeThread.stopDoing();
            writeThread = null;
        }
        if (null != readThread) {
            readThread.stopDoing();
            readThread = null;
        }
        if (null != serialPort) {
            serialPort.close();
            serialPort = null;
        }
        ready = false;
    }

    public String[] getAllSerialDevices() {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        return serialPortFinder.getAllDevices();
    }

    public String[] getAllSerialDevicesPath() {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        return serialPortFinder.getAllDevicesPath();
    }

}
