package android_serialport_api;

/**
 * Serial port data bits
 */
public enum DATAB {
    CS5(5),
    CS6(6),
    CS7(7),
    CS8(8);

    int dataBit;

    DATAB(int dataBit) {
        this.dataBit = dataBit;
    }

    public int getDataBit() {
        return this.dataBit;
    }
}