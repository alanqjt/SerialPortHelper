package android_serialport_api;

/**
 * Commonly used serial port baudrate
 */
public enum BAUDRATE {
    B0(0),
    B50(50),
    B75(75),
    B110(110),
    B134(134),
    B150(150),
    B200(200),
    B300(300),
    B600(600),
    B1200(1200),
    B1800(1800),
    B2400(2400),
    B4800(4800),
    B9600(9600),
    B19200(19200),
    B38400(38400),
    B57600(57600),
    B115200(115200),
    B230400(230400),
    B460800(460800),
    B500000(500000),
    B576000(576000),
    B921600(921600),
    B1000000(1000000),
    B1152000(1152000),
    B1500000(1500000),
    B2000000(2000000),
    B2500000(2500000),
    B3000000(3000000),
    B3500000(3500000),
    B4000000(4000000);

    int baudrate;

    BAUDRATE(int baudrate) {
        this.baudrate = baudrate;
    }

    int getBaudrate() {
        return this.baudrate;
    }

}