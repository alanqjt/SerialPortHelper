package com.alan.serialportlib;


import android.util.Log;

import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WriteThread extends Thread {
    private OutputStream mOutputStreams;
    private ScheduledExecutorService service1 = null;
    Parameter parameter;
    //队列长度
    private LinkedBlockingQueue<byte[]> queue;

    int frequency;

    public WriteThread(Parameter parameter) {
        this.parameter = parameter;
        int size = parameter.getQueueSizeBySend();
        if (size <= 0) {
            size = 100;
        }
        queue = new LinkedBlockingQueue<>(size);

        frequency = parameter.getFrequencyByReceived();
        if (frequency <= 0) {
            frequency = 200;
        }

    }

    public void stopDoing() {
        try {
            service1.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setmOutputStreams(OutputStream mOutputStreams) {
        this.mOutputStreams = mOutputStreams;
    }


    protected void addToQueue(byte[] params) {
        queue.add(params);
    }

    @Override
    public void run() {
        super.run();
        service1 = Executors.newSingleThreadScheduledExecutor();
        service1.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                byte[] params = queue.poll();
                if (params != null) {
                    try {
                        if (mOutputStreams != null) {
                            mOutputStreams.write(params);
                            mOutputStreams.flush();
                            if (null != parameter.getOnSerialPortDataListener()) {
                                parameter.getOnSerialPortDataListener().onDataSent(params, params.length, ByteUtils.bytesToHexString(params));
                            }
                        } else {
                            Log.d("SerialHelperWriteThread", "mOutputStreams == null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 20, frequency, TimeUnit.MILLISECONDS);
    }

}