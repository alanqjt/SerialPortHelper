package com.alan.serial;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.alan.serialportlib.OnSerialPortDataListener;
import com.alan.serialportlib.Parameter;
import com.alan.serialportlib.SerialHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textSend;
    private TextView textReceived;
    private static final  String SERIALPATH = "/dev/ttyS3";
    private static final  int BAUDRATE = 9600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSend = findViewById(R.id.textSend);
        textReceived = findViewById(R.id.textReceived);
        textSend.setMovementMethod(ScrollingMovementMethod.getInstance());
        textReceived.setMovementMethod(ScrollingMovementMethod.getInstance());
//        modelByFixed();
        modelByVariable();
//        modelByVariable2();
    }

    private void modelByFixed() {
        //协议模版
        //请在串口调试助手发送E1 09 8A 01 01 00 01 08 EF

        List<Integer> protocolHead = new ArrayList<>();
        protocolHead.add(0xE1);

        Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_FIXED, new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes, int length, String hexData) {
                refreshLogView(textReceived, "\n" + DataLongToStr_YMD_HMS(System.currentTimeMillis()) + "       " + hexData);
            }

            @Override
            public void onDataSent(byte[] bytes, int length, String hexData) {
                refreshLogView(textSend, "\n" + DataLongToStr_YMD_HMS(System.currentTimeMillis()) + "       " + hexData);
            }
        });
        parameter.setProLenIndex(1);
        parameter.setProtocolLength(9);
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
        SerialHelper.getInstance().serialStart(parameter);
        SerialHelper.getInstance().sendData(p);

        SerialHelper.getInstance().getAllSerialDevices();
        SerialHelper.getInstance().getAllSerialDevicesPath();
    }


    private void modelByVariable() {
        //协议模版
        //请在串口调试助手发送E1 09 8A 01 01 00 01 08 EF
        List<Integer> protocolHead = new ArrayList<>();
        protocolHead.add(0xE1);
        Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes, int length, String hexData) {
                refreshLogView(textReceived, "\n" + DataLongToStr_YMD_HMS(System.currentTimeMillis()) + "       " + hexData);
            }

            @Override
            public void onDataSent(byte[] bytes, int length, String hexData) {
                refreshLogView(textSend, "\n" + DataLongToStr_YMD_HMS(System.currentTimeMillis()) + "       " + hexData);
            }
        });
        parameter.setProLenIndex(1);
        parameter.setDebug(true);
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
        SerialHelper.getInstance().serialStart(parameter);
        SerialHelper.getInstance().sendData(p);
    }


    private void modelByVariable2() {
        //UselessLength 例子
        //协议模版
        //请在串口调试助手发送E1 05 8A 01 01 00 01 08 EF
        List<Integer> protocolHead = new ArrayList<>();
        protocolHead.add(0xE1);
        protocolHead.add(0x55);
        Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes, int length, String hexData) {
                refreshLogView(textReceived, "\n" + DataLongToStr_YMD_HMS(System.currentTimeMillis()) + "       " + hexData);
            }

            @Override
            public void onDataSent(byte[] bytes, int length, String hexData) {
                refreshLogView(textSend, "\n" + DataLongToStr_YMD_HMS(System.currentTimeMillis()) + "       " + hexData);
            }
        });
        parameter.setProLenIndex(1);
        parameter.setUselessLength(4);
        parameter.setDebug(true);
        byte[] p = {0x55, 1, 1, 0, 1, 0, 1, 0};
        SerialHelper.getInstance().serialStart(parameter);
        SerialHelper.getInstance().sendData(p);
    }


    void refreshLogView(final TextView textView, final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(msg);
                int offset = textView.getLineCount() * textView.getLineHeight();
                if (offset > textView.getHeight()) {
                    textView.scrollTo(0, offset - textView.getHeight());
                }
            }
        });

    }

    public static String DataLongToStr_YMD_HMS(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(date);
        final String myDate = formatter.format(curDate);
        return myDate;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SerialHelper.getInstance().close();
    }
}
