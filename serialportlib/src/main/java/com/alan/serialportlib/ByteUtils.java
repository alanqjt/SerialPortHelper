package com.alan.serialportlib;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ByteUtils {

    public static void main(String[] args) {
        //System.out.println(bytesToLong(new byte[]{-57,-78,-20,1,0,0,0,0}));
    }

    /**
     * 将Byte列表转换为字节数组
     *
     * @param list Byte列表
     * @return 对应的字节数组，如果列表为空或大小小于0，则返回null
     */
    public static byte[] listTobyte(List<Byte> list) {
        if (list == null || list.size() < 0) {
            return null;
        }
        byte[] bytes = new byte[list.size()];
        int i = 0;
        Iterator<Byte> iterator = list.iterator();
        while (iterator.hasNext()) {
            bytes[i] = iterator.next();
            i++;
        }
        return bytes;
    }

    /**
     * 将int值转换为4字节的字节数组
     *
     * @param intValue int值
     * @return 4字节的字节数组
     */
    public static byte[] intTo4Byte(int intValue) {
        byte[] result = new byte[4];
        result[0] = (byte) ((intValue & 0xFF000000) >> 24);
        result[1] = (byte) ((intValue & 0x00FF0000) >> 16);
        result[2] = (byte) ((intValue & 0x0000FF00) >> 8);
        result[3] = (byte) ((intValue & 0x000000FF));
        return result;
    }

    /**
     * 将int值转换为2字节的字节数组，低位在前，高位在后
     *
     * @param intValue int值
     * @return 2字节的字节数组
     */
    public static byte[] intTo2ByteLH(int intValue) {
        byte[] result = new byte[2];
        result[1] = (byte) ((intValue & 0x0000FF00) >> 8);
        result[0] = (byte) ((intValue & 0x000000FF));
        return result;
    }

    /**
     * 将int值转换为3字节的字节数组
     *
     * @param intValue int值
     * @return 3字节的字节数组
     */
    public static byte[] intTo3Byte(int intValue) {
        byte[] result = new byte[3];
        result[0] = (byte) ((intValue & 0x00FF0000) >> 16);
        result[1] = (byte) ((intValue & 0x0000FF00) >> 8);
        result[2] = (byte) ((intValue & 0x000000FF));
        return result;
    }


    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param src 字节数组
     * @return 十六进制字符串，如果输入为空或长度为0，则返回null
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 将字节数组转换为int值
     *
     * @param bytes 字节数组
     * @return 转换后的int值
     */
    public static int bytesToInt(byte[] bytes) {
        switch (bytes.length) {
            case 4:
                return bytes4Toint(bytes);

            case 2:
                return bytes2Toint(bytes);

            case 1:
                return bytes[0] & 0x000000ff;

            default:
                return 0;
        }
    }

    /**
     * 将字节数组转换为long值
     *
     * @param bytes 字节数组
     * @return 转换后的long值
     */
    public static long bytesToLong(byte[] bytes) {
        switch (bytes.length) {
            case 8:
                return (((long) bytes[0] << 56) & 0xFF00000000000000L)
                        | (((long) bytes[1] << 48) & 0x00FF000000000000L)
                        | (((long) bytes[2] << 40) & 0x0000FF0000000000L)
                        | (((long) bytes[3] << 32) & 0x000000FF00000000L)
                        | (((long) bytes[4] << 24) & 0x00000000FF000000L)
                        | (((long) bytes[5] << 16) & 0x0000000000FF0000L)
                        | (((long) bytes[6] << 8) & 0x000000000000FF00L)
                        | ((long) bytes[7] & 0x00000000000000FFL);

            case 4:
                return bytes4Toint(bytes);

            case 2:
                return bytes2Toint(bytes);

            case 1:
                return bytes[0] & 0x000000FF;

            default:
                return 0;
        }
    }

    /**
     * 将4字节的字节数组转换为int值
     *
     * @param bytes 4字节的字节数组
     * @return 转换后的int值
     */
    public static int bytes4Toint(byte[] bytes) {
        int in2 = ((bytes[0] << 24) & 0xFF000000)
                | ((bytes[1] << 16) & 0x00FF0000)
                | ((bytes[2] << 8) & 0x0000FF00) | (bytes[3] & 0x000000FF);
        return in2;
    }

    /**
     * 将两个字节转换为int值
     *
     * @param a 第一个字节
     * @param b 第二个字节
     * @return 转换后的int值
     */
    public static int bytes2Toint(byte a, byte b) {
        int in2 = ((a << 8) & 0xFF00) | (b & 0x000FF);
        return in2;
    }


    /**
     * 将2字节的字节数组转换为int值
     *
     * @param bytes 2字节的字节数组
     * @return 转换后的int值
     */
    public static int bytes2Toint(byte[] bytes) {
        int in2 = ((bytes[0] << 8) & 0x0000FF00) | (bytes[1] & 0x000000FF);
        return in2;
    }

    /**
     * 将2字节的字节数组转换为short值
     *
     * @param bytes 2字节的字节数组
     * @return 转换后的short值
     */
    public static short bytesToShort(byte[] bytes) {
        return (short) (bytes[1] & 0xff | (bytes[0] & 0xff) << 8);
    }

    /**
     * 将short值转换为2字节的字节数组
     *
     * @param shortValue short值
     * @return 2字节的字节数组
     */
    public static byte[] shortToByte(short shortValue) {
        byte[] result = new byte[2];
        result[0] = (byte) ((shortValue & 0xFF00) >> 8);
        result[1] = (byte) ((shortValue & 0x00FF));
        return result;
    }

    /**
     * 将short值复制到long值的字节数组中
     *
     * @param toLong    目标字节数组
     * @param fromShort 源字节数组
     * @param offset    偏移量
     * @return 复制后的长度
     */
    public static int copyShort2Long(byte[] toLong, byte[] fromShort, int offset) {
        for (int i = 0; i < fromShort.length; i++) {
            toLong[i + offset] = fromShort[i];
        }
        return fromShort.length + offset;
    }

    /**
     * 将long值复制到short值的字节数组中
     *
     * @param toShort  目标字节数组
     * @param fromLong 源字节数组
     * @param offset   偏移量
     * @return 复制后的长度
     */
    public static int copyLong2Short(byte[] toShort, byte[] fromLong, int offset) {
        for (int i = 0; i < toShort.length; i++) {
            toShort[i] = fromLong[i + offset];
        }
        return toShort.length + offset;
    }

    /**
     * 将4字节整型转换为Date对象，格式为yymmddhh
     *
     * @param piece 4字节整型的字节数组
     * @return 对应的Date对象
     */
    public static Date bytesToDateH(byte[] piece) {
        int intValue = bytesToInt(piece);
        int y = 2000 + intValue / 1000000;
        int m = (intValue % 1000000) / 10000;
        int d = (intValue % 10000) / 100;
        int h = intValue % 100;

        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, d, h, 0);

        return calendar.getTime();
    }

    /**
     * 将4字节整型转换为Date对象，格式为yymmddhhmm
     *
     * @param piece 4字节整型的字节数组
     * @return 对应的Date对象
     */
    public static Date bytesToDateM(byte[] piece) {
        int intValue = bytesToInt(piece);
        int y = 2000 + intValue / 100000000;
        int m = (intValue % 100000000) / 1000000;
        int d = (intValue % 1000000) / 10000;
        int h = (intValue % 10000) / 100;
        int mi = intValue % 100;

        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, d, h, mi);

        return calendar.getTime();
    }

    /**
     * 将shortOne数组的每个元素与longOne数组的对应位置元素进行比较，如果longOne的对应位置没有元素，则将shortOne的该位置元素置为0
     *
     * @param shortOne 较短的数组
     * @param longOne  较长的数组
     */
    public static void cutIn(byte[] shortOne, byte[] longOne) {
        for (int i = 0; i < shortOne.length; i++) {
            if (longOne.length - shortOne.length + i < 0) {
                shortOne[i] = 0;
            } else {
                shortOne[i] = longOne[longOne.length - shortOne.length + i];
            }
        }
    }

    /**
     * 将shortOne数组的每个元素与longOne数组的对应位置元素进行比较，如果longOne的对应位置没有元素，则将shortOne的该位置元素置为0
     *
     * @param shortOne 较短的数组
     * @param longOne  较长的数组
     */
    public static void cutStr(byte[] shortOne, byte[] longOne) {
        for (int i = 0; i < shortOne.length; i++) {
            if (i > longOne.length - 1) {
                shortOne[i] = 0;
            } else {
                shortOne[i] = longOne[i];
            }
        }
    }

    /**
     * 将日期转换为自定义格式的字节数组
     *
     * @param date 日期对象
     * @return 表示日期的字节数组
     */
    public static byte[] dateMToBytes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR) % 100;
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        int timestamp = y * 100000000 + m * 1000000 + d * 10000 + h * 100 + mi;
        return intTo4Byte(timestamp);
    }

    /**
     * 将日期转换为自定义格式的字节数组
     *
     * @param date 日期对象
     * @return 表示日期的字节数组
     */
    public static byte[] dateHToBytes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR) % 100;
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int timestamp = y * 1000000 + m * 10000 + d * 100 + h;
        return intTo4Byte(timestamp);
    }

    /**
     * 将长整型数值转换为8字节的字节数组
     *
     * @param longValue 长整型数值
     * @return 表示长整型数值的字节数组
     */
    public static byte[] longToByte(long longValue) {
        byte[] result = new byte[8];

        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = new Long(longValue & 0xFFL).byteValue();
            longValue = longValue >> 8;
        }

        return result;
    }

    /**
     * 反转字节数组的元素顺序
     *
     * @param bytes 待反转的字节数组
     */
    public static void reverse(byte[] bytes) {
        if (null == bytes) {
            return;
        }
        byte b;
        for (int i = 0; i < bytes.length; i++) {
            if (i < bytes.length - i - 1) {
                b = bytes[i];
                bytes[i] = bytes[bytes.length - i - 1];
                bytes[bytes.length - i - 1] = b;
            } else {
                return;
            }
        }
    }

    /**
     * 将字节数组转换为C字符串格式，即在末尾添加空字节
     *
     * @param bytes 待转换的字节数组
     * @return 转换后的字节数组
     */
    public static byte[] toCString(byte[] bytes) {
        return toCString(bytes, 1);
    }

    /**
     * 使用自定义密钥对数据进行异或加密
     *
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public static byte[] FmEncryption(byte[] data) {
        byte[] cods = new byte[]{0x36, 0x08, (byte) 0xf6, 0x13, (byte) 0xe2,
                0x0D, 0x47, (byte) 0xA0};
        int codsi = 0;

        for (int i = 0; i < data.length; i++) {

            data[i] ^= cods[codsi];
            codsi++;
            if (codsi >= cods.length) {
                codsi = 0;
            }
        }
        return data;
    }

    /**
     * 使用自定义密钥对数据进行异或加密
     *
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] data) {
        byte[] cods = new byte[]{98, 100, 52, 97, 99, 49, 48, 51, 54, 99,
                102, 98, 51, 56, 55, 101, 49, 52, 49, 52, 50, 49, 51, 48, 49,
                100, 53, 52, 55, 99, 98, 52};
        int codsi = 0;

        for (int i = 0; i < data.length; i++) {

            data[i] ^= cods[codsi];
            codsi++;
            if (codsi >= cods.length) {
                codsi = 0;
            }
        }
        return data;
    }

    /**
     * 计算字节数组中所有字节的累加和
     *
     * @param data   待计算累加和的字节数组
     * @param offset 累加和计算的起始位置
     * @return 字节数组的累加和
     */
    public static int bytesSum(byte[] data, int offset) {
        // 等价于无符号整型累加
        int s = 0;
        for (int i = offset; i < data.length; i++) {
            s += bytesToInt(new byte[]{0, data[i]});
        }
        return (int) s;
    }

    private static final char[] HEX_CHAR = new char[]{'0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 将字节数组转换为十六进制字符串表示形式
     *
     * @param buffer 待转换的字节数组
     * @return 字节数组的十六进制字符串表示
     */
    public static final String dumpBytes(byte[] buffer) {
        if (buffer == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        int count = 0;
        for (byte b : buffer) {
            sb.append((HEX_CHAR[(b & 0x00F0) >> 4])).append(
                    (HEX_CHAR[b & 0x000F]));
            count++;
            if (count % 16 == 0) {
                sb.append("\n");
            } else if (count % 4 == 0) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * 从字节数组中提取字符串部分，遇到两个连续的空字节时停止
     *
     * @param bytes 待提取的字节数组
     * @return 提取的字符串部分的字节数组
     */
    public static byte[] getStringBytes(byte[] bytes) {
        int len = 0;
        for (int j = 0; j < bytes.length; j += 2) {
            if (bytes[j] == 0 && bytes[j + 1] == 0) {
                len = j;
                break;
            }
        }
        byte[] piece = new byte[len];
        copyLong2Short(piece, bytes, 0);
        return piece;
    }

    /**
     * 将字节数组填充到指定长度，根据位置参数决定填充的位置
     *
     * @param bytes    待填充的字节数组
     * @param len      目标长度
     * @param position 填充位置，-1表示在末尾填充，其他值表示在开头填充
     * @return 填充后的字节数组
     */
    public static byte[] fillIn(byte[] bytes, int len, int position) {
        if (bytes.length > len) {
            return null;
        } else if (bytes.length == len) {
            return bytes;
        } else {
            byte[] data = new byte[len];
            if (position == -1) {
                copyShort2Long(data, bytes, data.length - bytes.length);
            } else {
                copyShort2Long(data, bytes, 0);
            }
            return data;
        }
    }

    /**
     * 从字节数组中截取指定长度的部分
     *
     * @param len       截取的长度
     * @param intToByte 待截取的字节数组
     * @return 截取后的字节数组
     */
    public static byte[] cutInt(int len, byte[] intToByte) {
        if (len > intToByte.length) {
            return null;
        } else {
            byte[] bytes = new byte[len];
            cutIn(bytes, intToByte);
            return bytes;
        }
    }

    /**
     * 将字节数组转换为C字符串格式，即在末尾添加指定数量的空字节
     *
     * @param bytes 待转换的字节数组
     * @param i     添加的空字节数量
     * @return 转换后的字节数组
     */
    public static byte[] toCString(byte[] bytes, int i) {
        byte[] newBytes = new byte[bytes.length + i];
        ByteUtils.copyShort2Long(newBytes, bytes, 0);
        return newBytes;
    }

    /**
     * 反转字节数组的元素顺序
     *
     * @param intToByte 待反转的字节数组
     * @return 反转后的字节数组
     */
    public static byte[] getReverse(byte[] intToByte) {
        reverse(intToByte);
        return intToByte;
    }

    /**
     * 使用FNV哈希算法计算字节数组的哈希值
     *
     * @param data 待计算哈希值的字节数组
     * @return 字节数组的哈希值
     */
    public static int FNVHash1(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b : data) {
            hash = (hash ^ b) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }


    /**
     * 异或计算产生校验码
     *
     * @param datas 需要校验的数据
     * @param len   数据长度
     * @return 校验码
     */
    public static byte getXor(byte[] datas, int len) {

        byte temp = datas[0];

        for (int i = 1; i < len; i++) {
            temp ^= datas[i];
        }

        return temp;
    }

    /**
     * 将十进制数字转换为十六进制数字
     *
     * @param decimal 十进制数字
     * @return 十六进制表示的数字
     */
    public static int decimalToHex(int decimal) {
        return Integer.parseInt(decimalToHexString(decimal));
    }


    /**
     * 将十进制数转换为十六进制字符串
     * 此方法不使用内置的转换函数，而是通过数学计算和字符映射来实现转换
     *
     * @param decimal 十进制数
     * @return 对应的十六进制字符串
     */
    public static String decimalToHexString(int decimal) {
        // 初始化十六进制字符串变量
        String hex = "";
        // 当十进制数不等于0时，继续转换
        while (decimal != 0) {
            // 获取当前十进制数除以16的余数，用于转换为十六进制字符
            int hexValue = decimal % 16;
            // 将余数转换为十六进制字符，并添加到十六进制字符串的开头
            hex = toHexChar(hexValue) + hex;
            // 十进制数除以16，为下一轮迭代做准备
            decimal = decimal / 16;
        }
        // 返回转换后的十六进制字符串
        return hex;
    }

    /**
     * 将十六进制数字转换为字符
     *
     * @param hexValue 十六进制数字
     * @return 对应的字符
     */
    public static char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0) {
            return (char) (hexValue + '0');
        } else {
            return (char) (hexValue - 10 + 'A');
        }
    }


    /**
     * desc: 获取一个int 数据类型的  二进制 某一位是多少
     * ----->以 整数  85为例   转换成二进制后为 101 0101    获取第4位 的值 i =4 结果为0    获取第5位的值 i =5 结果为1
     * ps:位是由左往右   下标从1开始  不是从0开始
     *
     * @param N 源数据
     * @param i 想要获取的第几位
     **/
    public static int getbit(int N, int i) {
        return ((N >> (i - 1)) & 1);
    }
}
