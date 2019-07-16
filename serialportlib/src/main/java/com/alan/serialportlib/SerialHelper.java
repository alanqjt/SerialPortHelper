package com.alan.serialportlib;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class SerialHelper {

    private static SerialHelper serialHelper = null;

    private SerialPort serialPort;

    private WriteThread writeThread;
    private ReadThread readThread;

    public static final String TAG = "SerialHelper";

    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private boolean ready = false;

    private SerialHelper() {
    }

    public static synchronized SerialHelper getInstance() {
        if (serialHelper == null)
            serialHelper = new SerialHelper();
        return serialHelper;
    }

    public boolean serialStart(Parameter parameter) {

        if (ready) {
            Log.e(TAG, "SerialHelper is running");
            return true;
        }

        try {
            serialPort = new SerialPort(new File(parameter.getSerialPath()), parameter.getBaudrate(), 0, parameter.getSuPath());
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            writeThread = new WriteThread(parameter);
            writeThread.setmOutputStreams(outputStream);

            readThread = new ReadThread(parameter);
            readThread.setmInputStreams(inputStream);
            readThread.start();
            writeThread.start();
            ready = true;
        } catch (IOException e) {
            e.printStackTrace();
            ready = false;
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
        writeThread.stopDoing();
        readThread.stopDoing();
        serialPort.close();
        writeThread = null;
        readThread = null;
        ready = false;
        serialHelper = null;
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
