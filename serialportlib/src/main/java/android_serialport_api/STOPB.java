package android_serialport_api;

/**
 * Serial port stop bit
 */
public enum STOPB {
    B1(1),
    B2(2);

    int stopBit;

    STOPB(int stopBit) {
        this.stopBit = stopBit;
    }

    public int getStopBit() {
        return this.stopBit;
    }

}