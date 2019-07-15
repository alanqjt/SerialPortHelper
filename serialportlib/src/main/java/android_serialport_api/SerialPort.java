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
    public FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    /**
     * @param device   //句柄
     * @param baudrate //波特率
     * @param flags    //串口标志位
     * @throws SecurityException
     * @throws IOException
     */
    public SerialPort(File device, int baudrate, int flags,String suPath) throws SecurityException, IOException {

        //检查访问权限，如果没有读写权限，进行文件操作，修改文件访问权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                //通过挂载到linux的方式，修改文件的操作权限
                Process su ;
                if (null != suPath && !suPath.equals("")){
                    su = Runtime.getRuntime().exec(suPath);
                }else{
                    su = Runtime.getRuntime().exec("/system/xbin/su");
                }

                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException("SerialPort: 没有权限");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //当切换到不正常串口地址 会闪退，不能打开
                Log.e(TAG, "串口地址异常，不能正常打开串口");
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudrate, flags);

        if (mFd == null) {
            throw new IOException("native open returns null");
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


    // JNI(调用java本地接口，实现串口的打开和关闭)
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    static {//加载jni下的C文件库
//        Log.d(TAG, "本地库加载中");
        try {
            System.loadLibrary("serial_port");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
