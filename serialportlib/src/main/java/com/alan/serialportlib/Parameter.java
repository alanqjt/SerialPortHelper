package com.alan.serialportlib;

import java.util.List;

import android_serialport_api.STOPB;
import android_serialport_api.DATAB;
import android_serialport_api.PARITY;
import android_serialport_api.FLOWCON;

/**
 * @author qujiantao
 */
public class Parameter {

    // 权限地址
    private final String suPath;
    // 波特率
    private final int baudrate;
    // 串口地址
    private final String serialPath;

    // 停止位，默认 B1
    private final int stopBit;
    // 数据位，默认 CS8
    private final int dataBit;
    // 奇偶校验，默认 NONE
    private final int parity;
    // 流控，默认 NONE
    private final int flowCon;
    // 打开串口标志位，默认 0
    private final int flags;

    // 协议开头
    private final List<Integer> protocolHead;
    // 协议结尾
    private final int protocolEnd;
    // 协议长度
    private final int protocolLength;
    // 协议长度截取下标（从0开始）
    private final int proLenIndex;
    // 协议模型：固定/可变长度
    private final int protocolModel;
    // 无用数据长度
    private final int uselessLength;

    // 写线程处理速度
    private final int frequencyBySend;
    // 读线程处理速度
    private final int frequencyByReceived;

    // 写队列缓存大小
    private final int queueSizeBySend;
    // 读队列缓存大小
    private final int queueSizeByReceived;

    // 数据监听器
    private final OnSerialPortDataListener onSerialPortDataListener;

    // 是否打印日志
    private final boolean debug;

    // 固定长度协议模型常量
    public static final int PROTOCOLMODEL_FIXED = 0;
    // 可变长度协议模型常量
    public static final int PROTOCOLMODEL_VARIABLE = 1;
    // 无协议模型常量
    public static final int PROTOCOLMODEL_NONE = -1;

    // 私有构造函数，由 Builder 构建
    private Parameter(Builder builder) {
        this.suPath = builder.suPath;
        this.baudrate = builder.baudrate;
        this.serialPath = builder.serialPath;
        this.stopBit = builder.stopBit;
        this.dataBit = builder.dataBit;
        this.parity = builder.parity;
        this.flowCon = builder.flowCon;
        this.flags = builder.flags;
        this.protocolHead = builder.protocolHead;
        this.protocolEnd = builder.protocolEnd;
        this.protocolLength = builder.protocolLength;
        this.proLenIndex = builder.proLenIndex;
        this.protocolModel = builder.protocolModel;
        this.uselessLength = builder.uselessLength;
        this.frequencyBySend = builder.frequencyBySend;
        this.frequencyByReceived = builder.frequencyByReceived;
        this.queueSizeBySend = builder.queueSizeBySend;
        this.queueSizeByReceived = builder.queueSizeByReceived;
        this.onSerialPortDataListener = builder.onSerialPortDataListener;
        this.debug = builder.debug;
    }

    public String getSuPath() {
        return suPath;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public String getSerialPath() {
        return serialPath;
    }

    public int getStopBit() {
        return stopBit;
    }

    public int getDataBit() {
        return dataBit;
    }

    public int getParity() {
        return parity;
    }

    public int getFlowCon() {
        return flowCon;
    }

    public int getFlags() {
        return flags;
    }

    public List<Integer> getProtocolHead() {
        return protocolHead;
    }

    public int getProtocolEnd() {
        return protocolEnd;
    }

    public int getProtocolLength() {
        return protocolLength;
    }

    public int getProLenIndex() {
        return proLenIndex;
    }

    public int getProtocolModel() {
        return protocolModel;
    }

    public int getUselessLength() {
        return uselessLength;
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

    public OnSerialPortDataListener getOnSerialPortDataListener() {
        return onSerialPortDataListener;
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "suPath='" + suPath + '\'' +
                ", baudrate=" + baudrate +
                ", serialPath='" + serialPath + '\'' +
                ", stopBit=" + stopBit +
                ", dataBit=" + dataBit +
                ", parity=" + parity +
                ", flowCon=" + flowCon +
                ", flags=" + flags +
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

    /**
     * Builder 类用于构建 Parameter 实例
     */
    public static class Builder {
        // 必填字段
        private final String serialPath;
        private final int baudrate;
        private final List<Integer> protocolHead;
        private final int protocolModel;
        private final OnSerialPortDataListener onSerialPortDataListener;

        // 可选字段（默认值）
        private String suPath;
        private int stopBit = STOPB.B1.getStopBit();
        private int dataBit = DATAB.CS8.getDataBit();
        private int parity = PARITY.NONE.getParity();
        private int flowCon = FLOWCON.NONE.getFlowCon();
        private int flags = 0;
        private int protocolEnd;
        private int protocolLength;
        private int proLenIndex;
        private int uselessLength = 0;
        private int frequencyBySend = 1;
        private int frequencyByReceived = 1;
        private int queueSizeBySend = 16;
        private int queueSizeByReceived = 16;
        private boolean debug = false;

        /**
         * 构造 Builder，必须传入关键参数
         *
         * @param serialPath               串口路径
         * @param baudrate                 波特率
         * @param protocolHead             协议头
         * @param protocolModel            协议模型（固定/可变）
         * @param onSerialPortDataListener 数据监听器
         */
        public Builder(String serialPath, int baudrate, List<Integer> protocolHead,
                       int protocolModel, OnSerialPortDataListener onSerialPortDataListener) {
            this.serialPath = serialPath;
            this.baudrate = baudrate;
            this.protocolHead = protocolHead;
            this.protocolModel = protocolModel;
            this.onSerialPortDataListener = onSerialPortDataListener;
        }

        public Builder setSuPath(String suPath) {
            this.suPath = suPath;
            return this;
        }

        public Builder setStopBit(int stopBit) {
            this.stopBit = stopBit;
            return this;
        }

        public Builder setDataBit(int dataBit) {
            this.dataBit = dataBit;
            return this;
        }

        public Builder setParity(int parity) {
            this.parity = parity;
            return this;
        }

        public Builder setFlowCon(int flowCon) {
            this.flowCon = flowCon;
            return this;
        }

        public Builder setFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder setProtocolEnd(int protocolEnd) {
            this.protocolEnd = protocolEnd;
            return this;
        }

        public Builder setProtocolLength(int protocolLength) {
            this.protocolLength = protocolLength;
            return this;
        }

        public Builder setProLenIndex(int proLenIndex) {
            this.proLenIndex = proLenIndex;
            return this;
        }

        public Builder setUselessLength(int uselessLength) {
            this.uselessLength = uselessLength;
            return this;
        }

        public Builder setFrequencyBySend(int frequencyBySend) {
            this.frequencyBySend = frequencyBySend;
            return this;
        }

        public Builder setFrequencyByReceived(int frequencyByReceived) {
            this.frequencyByReceived = frequencyByReceived;
            return this;
        }

        public Builder setQueueSizeBySend(int queueSizeBySend) {
            this.queueSizeBySend = queueSizeBySend;
            return this;
        }

        public Builder setQueueSizeByReceived(int queueSizeByReceived) {
            this.queueSizeByReceived = queueSizeByReceived;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        /**
         * 构建最终的 Parameter 对象
         */
        public Parameter build() {
            return new Parameter(this);
        }
    }
}
