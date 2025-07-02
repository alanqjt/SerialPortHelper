/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";
    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private final FileDescriptor mFd;
    private final FileInputStream mFileInputStream;
    private final FileOutputStream mFileOutputStream;

    /**
     * Open the serial port with the specified parameters.
     *
     * @param device   the serial device file, e.g., "/dev/ttyS0"
     * @param baudrate the baud rate, e.g., {@link BAUDRATE#B9600}
     * @param stopBits the stop bits, e.g., {@link STOPB#B1}
     * @param dataBits the data bits, e.g., {@link DATAB#B8}
     * @param parity   the parity, e.g., {@link PARITY#NONE}
     * @param flowCon  the flow control, e.g., {@link FLOWCON#NONE}
     * @param flags    flags for opening the port, e.g., O_RDWR | O_NOCTTY | O_NDELAY
     * @param suPath   the path to the superuser binary, e.g., "/system/bin/su"
     * @throws SecurityException if access to the device is denied
     * @throws IOException       if an I/O error occurs
     */
    public SerialPort(File device, int baudrate, int stopBits, int dataBits, int parity, int flowCon, int flags, String suPath) throws SecurityException, IOException {

        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                //通过挂载到linux的方式，修改文件的操作权限
                Process su;
                if (null != suPath && !suPath.isEmpty()) {
                    su = Runtime.getRuntime().exec(suPath);
                } else {
//                    su = Runtime.getRuntime().exec("/system/xbin/su");
                    su = Runtime.getRuntime().exec("/system/bin/su");
                }
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";

//                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";

                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudrate, stopBits, dataBits, parity, flowCon, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * The serial port has 5 parameters: serial device name, baud rate, check bit, data bit, stop bit
     * Among them, the check bit is generally defaulted to the NONE, the data bit is generally defaulted to 8, and the stop bit is defaulted to 1
     *
     * @param path     to the data pair of the serial device
     * @param baudrate {@link BAUDRATE} baudrate
     * @param stopBits {@link STOPB} stop bit
     * @param dataBits {@link DATAB} data bits
     * @param parity   {@link PARITY} check digit
     * @param flowCon  {@link FLOWCON} flow control
     * @param flags    O_RDWR read and write mode to open | O_NOCTTY Do not allow process management serial port | O_NDELAY Non-blocking
     * @return
     */
    private native static FileDescriptor open(String path, int baudrate, int stopBits, int dataBits, int parity, int flowCon, int flags);

    public native void close();

    static {
        System.loadLibrary("serialport");
    }
}
