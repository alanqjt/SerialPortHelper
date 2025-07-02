package android_serialport_api;

/**
 * Serial port flow type
 */
public enum FLOWCON {
    NONE(0),
    HARD(1),
    SOFT(2);

    int flowCon;

    FLOWCON(int flowCon) {
        this.flowCon = flowCon;
    }

    public int getFlowCon() {
        return this.flowCon;
    }
}