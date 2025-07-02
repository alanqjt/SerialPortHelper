package android_serialport_api;

/**
 * Serial port parity
 */
public enum PARITY {
    NONE(0),
    ODD(1),
    EVEN(2),
    SPACE(3),
    MARK(4);

    int parity;

    PARITY(int parity) {
        this.parity = parity;
    }

    public int getParity() {
        return this.parity;
    }
}