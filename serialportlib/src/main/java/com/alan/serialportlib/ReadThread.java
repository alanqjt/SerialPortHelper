package com.alan.serialportlib;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author qujiantao
 */
public class ReadThread extends Thread {

    public static final String TAG = "SerialHelperReadThread";

    private InputStream mInputStreams;
    private ScheduledExecutorService service1 = null;
    private ScheduledExecutorService service2 = null;
    Parameter parameter;
    private final LinkedBlockingQueue<byte[]> queue;

    int frequency;
    int protocolModel;
    boolean hasProtocol;

    public ReadThread(Parameter parameter) {
        this.parameter = parameter;
        int size = parameter.getQueueSizeByReceived();
        if (size <= 0) {
            size = 100;
        }
        queue = new LinkedBlockingQueue<>(size);
        protocolModel = parameter.getProtocolModel();
        hasProtocol = protocolModel == Parameter.PROTOCOLMODEL_FIXED || protocolModel == Parameter.PROTOCOLMODEL_VARIABLE;
        showLog(" 读取模式是否含有协议：" + hasProtocol);
        frequency = parameter.getFrequencyByReceived();
        if (frequency <= 0) {
            frequency = 200;
        }
    }

    protected void setmInputStreams(InputStream mInputStreams) {
        this.mInputStreams = mInputStreams;
    }

    public void stopDoing() {
        try {
            service1.shutdownNow();
            service2.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        super.run();

        if (hasProtocol) {
            service1 = Executors.newSingleThreadScheduledExecutor();
            service1.scheduleWithFixedDelay(() -> getFullBytes(), 20, 20, TimeUnit.MILLISECONDS);
        }

        service2 = Executors.newSingleThreadScheduledExecutor();
        service2.scheduleWithFixedDelay(() -> {
            int size;
            try {
                byte[] buffer = new byte[1024];
                if (mInputStreams == null) {
                    return;
                }
                size = mInputStreams.read(buffer);
                if (size > 0) {
                    byte[] b = new byte[size];
                    System.arraycopy(buffer, 0, b, 0, b.length);
                    try {
                        if (hasProtocol) {
                            queue.put(b);
                        } else {
                            onDataReceived(b);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b = null;
                } else {
                    showLog("read size is " + size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 10, 10, TimeUnit.MILLISECONDS);
    }

    private List<byte[]> removeList = new ArrayList<>();

    private void getFullBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                byte[] oneBytes = queue.peek();
                if (oneBytes != null) {
                    showLog(Thread.currentThread().getName() + " wait read " + ByteUtils.bytesToHexString(oneBytes));
                    baos.write(oneBytes);
                }
                byte[] currBytes = baos.toByteArray();
                int startPos = -1;
                int endPos = -1;
                // 找到开始的下标

                for (int i = 0; i < currBytes.length; i++) {
                    int start = 0xff & currBytes[i];
                    // 协议开始
                    if (parameter.getProtocolHead().contains(start)) {
                        startPos = i;
                        showLog(Thread.currentThread().getName() + " get the start postion " + startPos);
                        break;
                    }
                }
                //数据长度大于2才有意义
                if (startPos != -1 && (currBytes.length - startPos - 1) > 2) {
                    // 找到结束下标
                    //下标是在协议头所在的下标开始  再加上截取长度的所在的下标
                    int packageLen;
                    if (parameter.getProtocolModel() == Parameter.PROTOCOLMODEL_VARIABLE)
                    //数据长度可变模式
                    {
                        packageLen = (currBytes[startPos + parameter.getProLenIndex()] & 0xff) + parameter.getUselessLength();
                    } else if (parameter.getProtocolModel() == Parameter.PROTOCOLMODEL_FIXED) {
                        //数据长度固定模式
                        packageLen = parameter.getProtocolLength();
                    } else {
                        Log.e(TAG, "readThread PROTOCOLMODEL no enable");
                        break;
                    }
                    showLog("数据长度为:" + packageLen);
                    showLog(Thread.currentThread().getName() + " -1-- " + packageLen + " currlength " + currBytes.length + " " + ByteUtils.bytesToHexString(currBytes));
                    // 存在完整的包
                    if ((currBytes.length) == (packageLen)) {
                        endPos = startPos + packageLen;
                        showLog("数据结束为:" + endPos);
                        showLog(Thread.currentThread().getName() + " handel1 " + startPos + " " + endPos);
                        byte[] fullBytes = new byte[packageLen];
                        System.arraycopy(currBytes, startPos, fullBytes, 0, fullBytes.length);
                        onDataReceived(fullBytes);
                        // 最后移除
                        removeList.add(oneBytes);
                        baos.reset();
                        // 刚好完了
                        break;
                    } else if ((currBytes.length) > (packageLen)) {
                        showLog("长度大于想要截取的长度" + ByteUtils.bytesToHexString(currBytes));
                        endPos = startPos + packageLen;
                        byte[] fullBytes = new byte[packageLen];
                        Log.d(TAG, Thread.currentThread().getName() + " handel2 " + startPos + " " + endPos);
                        showLog(currBytes.length + " xxx " + ByteUtils.bytesToHexString(currBytes) + " " + startPos + " " + fullBytes.length);
                        System.arraycopy(currBytes, startPos, fullBytes, 0, fullBytes.length);
                        onDataReceived(fullBytes);
                        // 最后移除
                        queue.remove(oneBytes);
                        // 多了
                        baos.reset();
                        baos.write(currBytes, endPos, currBytes.length - endPos);
                    } else {
                        showLog("数据还没完继续等" + ByteUtils.bytesToHexString(currBytes));
                        queue.remove(oneBytes);
                        // 还没结束 等
                        showLog(Thread.currentThread().getName() + " wait");
                    }
                } else if (startPos != -1) {
                    showLog(Thread.currentThread().getName() + " wait2 " + queue.remove(oneBytes));
                } else {
                    if (oneBytes != null) {
                        queue.remove(oneBytes);
                    }
                    baos.reset();
                }

                if (Thread.interrupted()) {
                    break;
                }
                //处理速度
                Thread.sleep(frequency);
            }
            for (byte[] bb : removeList) {
                queue.remove(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onDataReceived(byte[] fullBytes) {
        showLog("获得整的byte：" + ByteUtils.bytesToHexString(fullBytes));
        if (null != parameter.getOnSerialPortDataListener()) {
            parameter.getOnSerialPortDataListener().onDataReceived(fullBytes, fullBytes.length, ByteUtils.bytesToHexString(fullBytes));
        }
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }


    private void showLog(String msg) {
        if (parameter.isDebug()) {
            Log.d(TAG, msg);
        }
    }


}