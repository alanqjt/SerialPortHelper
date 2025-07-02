package com.alan.serial;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.alan.serialportlib.OnSerialPortDataListener;
import com.alan.serialportlib.Parameter;
import com.alan.serialportlib.SerialHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author qujiantao
 */
public class MainActivity extends AppCompatActivity implements OnSerialPortDataListener {

    private TextView textSend;
    private TextView textReceived;
    private static final String SERIALPATH = "/dev/ttyS6";
    private static final int BAUDRATE = 9600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSend = findViewById(R.id.textSend);
        textReceived = findViewById(R.id.textReceived);
        textSend.setMovementMethod(ScrollingMovementMethod.getInstance());
        textReceived.setMovementMethod(ScrollingMovementMethod.getInstance());
//        modelByFixed();
//        modelByVariable();
//        modelByVariable2();
        modelByNone();
    }

    /**
     * 模式0：无协议
     * 发送数据时，协议头和协议长度是可变的
     * 协议头：无
     * 协议长度：无
     */
    private void modelByNone() {
        Parameter parameter = new Parameter.Builder(SERIALPATH, BAUDRATE, null, Parameter.PROTOCOLMODEL_NONE, this).setDebug(true).build();
        SerialHelper serialHelper = new SerialHelper();
        serialHelper.serialStart(parameter);
        serialHelper.getAllSerialDevices();
        serialHelper.getAllSerialDevicesPath();
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
//        serialHelper.sendData("生活不易，井盖叹气".getBytes());
        serialHelper.sendData(p);
    }

    /**
     * 模式一：固定长度协议
     * 发送数据时，协议头和协议长度是固定的
     * 协议头：E1
     * 协议长度：9
     */
    private void modelByFixed() {
        //协议模版
        //请在串口调试助手发送E1 09 8A 01 01 00 01 08 EF

        List<Integer> protocolHead = new ArrayList<>();
        protocolHead.add(0xE1);
        Parameter parameter = new Parameter.Builder(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_FIXED, this).setProLenIndex(1).setProtocolLength(9).build();
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
        SerialHelper serialHelper = new SerialHelper();
        serialHelper.serialStart(parameter);
        serialHelper.sendData(p);
        serialHelper.getAllSerialDevices();
        serialHelper.getAllSerialDevicesPath();
    }

    /**
     * 模式二：可变长度协议
     * 发送数据时，协议头是固定的，协议长度是可变的
     * 协议头：E1
     * 协议长度：可变
     */
    private void modelByVariable() {
        //协议模版
        //请在串口调试助手发送E1 09 8A 01 01 00 01 08 EF
        List<Integer> protocolHead = new ArrayList<>();
        protocolHead.add(0xE1);
        Parameter parameter = new Parameter.Builder(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, this).setProLenIndex(1).setDebug(true).build();
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
        SerialHelper serialHelper = new SerialHelper();
        serialHelper.serialStart(parameter);
        serialHelper.sendData(p);
    }

    /**
     * 模式二：可变长度协议
     * 发送数据时，协议头是固定的，协议长度是可变的
     * 协议头：E1
     * 协议长度：可变
     * 无用数据长度：4
     */
    private void modelByVariable2() {
        //UselessLength 例子
        //协议模版
        //请在串口调试助手发送E1 05 8A 01 01 00 01 08 EF
        List<Integer> protocolHead = new ArrayList<>();
        protocolHead.add(0xE1);
        protocolHead.add(0x55);
        Parameter parameter = new Parameter.Builder(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, this)
                .setProLenIndex(1)//设置LEN下标
                .setUselessLength(4)//除了data有效数据外的长度
                .setDebug(true).build();
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
        SerialHelper serialHelper = new SerialHelper();
        serialHelper.serialStart(parameter);
        serialHelper.sendData(p);
    }


    void refreshLogView(final TextView textView, final String msg) {
        this.runOnUiThread(() -> {
            textView.append(msg);
            int offset = textView.getLineCount() * textView.getLineHeight();
            if (offset > textView.getHeight()) {
                textView.scrollTo(0, offset - textView.getHeight());
            }
        });

    }

    public static String dataLongToStrYMDHMS(long date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(date);
        final String myDate = formatter.format(curDate);
        return myDate;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataReceived(byte[] bytes, int length, String hexData) {
        Log.d("SerialLog", "TXT接收:" + new String(bytes) + "   ,HEX :" + hexData);
        refreshLogView(textReceived, "\n" + dataLongToStrYMDHMS(System.currentTimeMillis()) + "       " + hexData);
    }

    @Override
    public void onDataSend(byte[] bytes, int length, String hexData) {
        refreshLogView(textSend, "\n" + dataLongToStrYMDHMS(System.currentTimeMillis()) + "       " + hexData);
        Log.d("SerialLog", "发送:" + new String(bytes));
    }
}
