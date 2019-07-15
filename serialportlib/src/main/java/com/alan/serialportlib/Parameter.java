package com.alan.serialportlib;

import java.util.List;

public class Parameter {

    //权限地址
    private String suPath;
    //波特率
    private int baudrate;
    //串口地址
    private String serialPath;
    //协议开头
    private List<Integer> protocolHead;
    //协议结尾
    private int protocolEnd;
    //协议长度
    private int protocolLength;
    //协议长度截取的下标   从0开始！！！！！
    private int proLenIndex;
    //协议模型
    private int protocolModel;

    //除了实际有用的data长度，无用的数据 可能包含 协议头 0xE1，结尾 0xEF，校验 0x08，长度 0x05 等等，因协议而定   E1 05 8A 01 01 00 01 08 EF
    //data 实际应该是 8A 01 01 00 01    也就是说 这里指的长度是在   proLenIndex  (0x05) 截取后面 5位，
    //整个数据的长度 应该为 协议头+长度+校验+结尾    4+ 5  = 9   其中的 0x05 data长度 是可变化的     uselessLength= 4;
    private int uselessLength = 0;

    //固定长度  proLenIndex就没有意义了
    public static final int PROTOCOLMODEL_FIXED = 0;
    //可变长度   proLenIndex才有意义
    public static final int PROTOCOLMODEL_VARIABLE = 1;

    //写线程处理速度  只能大于0
    private int frequencyBySend;

    //读线程处理速度  只能大于0
    private int frequencyByReceived;

    //写线程队列缓存长度
    private int queueSizeBySend;

    //读线程队列缓存长度
    private int queueSizeByReceived;

    //数据监听
    private OnSerialPortDataListener onSerialPortDataListener;

    public OnSerialPortDataListener getOnSerialPortDataListener() {
        return onSerialPortDataListener;
    }

    //是否打印日志
    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Parameter(String serialPath, int baudrate, List<Integer> protocolHead, int protocolModel,  OnSerialPortDataListener onSerialPortDataListener) {
        this.serialPath = serialPath;
        this.protocolHead = protocolHead;
        this.protocolModel = protocolModel;
        this.baudrate = baudrate;
        this.onSerialPortDataListener = onSerialPortDataListener;

    }

    public void setFrequencyBySend(int frequencyBySend) {
        this.frequencyBySend = frequencyBySend;
    }

    public void setFrequencyByReceived(int frequencyByReceived) {
        this.frequencyByReceived = frequencyByReceived;
    }

    public void setQueueSizeBySend(int queueSizeBySend) {
        this.queueSizeBySend = queueSizeBySend;
    }

    public void setQueueSizeByReceived(int queueSizeByReceived) {
        this.queueSizeByReceived = queueSizeByReceived;
    }

    public int getFrequencyBySend() {
        return frequencyBySend;
    }

    public int getFrequencyByReceived() {
        return frequencyByReceived;
    }

    public int getQueueSizeBySend() {
        return queueSizeBySend;
    }

    public int getQueueSizeByReceived() {
        return queueSizeByReceived;
    }

    public String getSuPath() {
        return suPath;
    }

    public void setSuPath(String suPath) {
        this.suPath = suPath;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public String getSerialPath() {
        return serialPath;
    }

    public void setSerialPath(String serialPath) {
        this.serialPath = serialPath;
    }

    public List<Integer> getProtocolHead() {
        return protocolHead;
    }

    public void setProtocolHead(List<Integer> protocolHead) {
        this.protocolHead = protocolHead;
    }

    public int getProtocolEnd() {
        return protocolEnd;
    }

    public void setProtocolEnd(int protocolEnd) {
        this.protocolEnd = protocolEnd;
    }

    public int getProtocolLength() {
        return protocolLength;
    }

    public void setProtocolLength(int protocolLength) {
        this.protocolLength = protocolLength;
    }

    public int getProLenIndex() {
        return proLenIndex;
    }

    public void setProLenIndex(int proLenIndex) {
        this.proLenIndex = proLenIndex;
    }

    public int getUselessLength() {
        return uselessLength;
    }

    public void setUselessLength(int uselessLength) {
        this.uselessLength = uselessLength;
    }

    public int getProtocolModel() {
        return protocolModel;
    }

    public void setProtocolModel(int protocolModel) {
        this.protocolModel = protocolModel;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "suPath='" + suPath + '\'' +
                ", baudrate=" + baudrate +
                ", serialPath='" + serialPath + '\'' +
                ", protocolHead=" + protocolHead +
                ", protocolEnd=" + protocolEnd +
                ", protocolLength=" + protocolLength +
                ", proLenIndex=" + proLenIndex +
                ", protocolModel=" + protocolModel +
                ", uselessLength=" + uselessLength +
                ", frequencyBySend=" + frequencyBySend +
                ", frequencyByReceived=" + frequencyByReceived +
                ", queueSizeBySend=" + queueSizeBySend +
                ", queueSizeByReceived=" + queueSizeByReceived +
                ", onSerialPortDataListener=" + onSerialPortDataListener +
                ", debug=" + debug +
                '}';
    }
}
