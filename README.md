SerialPortHelper
========


[![](https://img.shields.io/badge/SerialPort-1.0.0-brightgreen.svg)](https://github.com/cepr/android-serialport-api)
[![](https://jitpack.io/v/alanqjt/SerialPortHelper.svg)](https://jitpack.io/#alanqjt/SerialPortHelper)
![](https://travis-ci.org/CymChad/BaseRecyclerViewAdapterHelper.svg?branch=master)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)


目录
====
* [引入](#引入)
* [使用说明](#使用说明)
* [属性说明](#属性说明)
* [效果演示](#效果演示)
* [项目成品](#项目成品)

引入
---------
* Android Studio

将[serialportlib](serialportlib)引入

```xml
dependencies {
     implementation project(path: ':serialportlib')
}
```

也可以直接

```xml
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```xml
dependencies {
     implementation 'com.github.alanqjt:SerialPortHelper:v1.0.0'
}
```

* Eclipse

还在使用Eclipse的你可以回家了!(把代码复制下来自己导入过去，但是还是建议赶紧把项目开发切换到Android Studio)




使用说明
---------
串口协议一般分为两种，第一种为固定长度，第二种为可变长度，而可变长度又分为两种。
所以此库能帮助您快速适配三种类型的协议,如果还是不能满足开发需求，可以自行修改serialportlib中的ReadThread(兄弟，我也只能帮你到这了)


* 第一种协议固定长度 PROTOCOLMODEL_FIXED

```Java
//协议模版
//请在串口调试助手发送E1 09 8A 01 01 00 01 08 EF

List<Integer> protocolHead = new ArrayList<>();
protocolHead.add(0xE1);
Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_FIXED, new OnSerialPortDataListener() {
    @Override
    public void onDataReceived(byte[] bytes, int length, String hexData) {
               
    }

    @Override
    public void onDataSent(byte[] bytes, int length, String hexData) {
                
    }
});
parameter.setProLenIndex(1);
parameter.setProtocolLength(9);
SerialHelper.getInstance().serialStart(parameter);
```


* 第二种协议固定长度 PROTOCOLMODEL_VARIABLE
<br></br>
1.data包含了所要截取的长度

```Java
//协议模版
//请在串口调试助手发送E1 09 8A 01 01 00 01 08 EF
List<Integer> protocolHead = new ArrayList<>();
protocolHead.add(0xE1);
Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, new OnSerialPortDataListener() {
    @Override
    public void onDataReceived(byte[] bytes, int length, String hexData) {
               
    }

    @Override
    public void onDataSent(byte[] bytes, int length, String hexData) {
               
    }
});
parameter.setProLenIndex(1);
SerialHelper.getInstance().serialStart(parameter);

```

<br></br>
2.data不包含了所要截取的长度，指的是有意义的数据长度

```Java
//协议模版
//请在串口调试助手发送E1 05 8A 01 01 00 01 08 EF
//请在串口调试助手发送55 05 8A 01 01 00 01 08 EF
List<Integer> protocolHead = new ArrayList<>();
protocolHead.add(0xE1);
protocolHead.add(0x55);
Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, new OnSerialPortDataListener() {
    @Override
    public void onDataReceived(byte[] bytes, int length, String hexData) {
               
    }

    @Override
    public void onDataSent(byte[] bytes, int length, String hexData) {
               
    }
});
parameter.setProLenIndex(1);
parameter.setUselessLength(4);
SerialHelper.getInstance().serialStart(parameter);
```



* 发送数据

```Java
SerialHelper.getInstance().sendData(data);
```


* 监听多种协议开头

```Java
List<Integer> protocolHead = new ArrayList<>();
protocolHead.add(0xE1);
protocolHead.add(0x55);
Parameter parameter = new Parameter(SERIALPATH, BAUDRATE, protocolHead, Parameter.PROTOCOLMODEL_VARIABLE, new OnSerialPortDataListener() {
    @Override
    public void onDataReceived(byte[] bytes, int length, String hexData) {
               
    }

    @Override
    public void onDataSent(byte[] bytes, int length, String hexData) {
               
    }
});
```


* 读写线程消息队列缓存长度

```Java
//发送数据队列长度
parameter.setQueueSizeBySend(100);//默认缓存100

//监听数据队列长度
parameter.setQueueSizeByReceived(100);//默认缓存100
```


* 读写线程消息处理并发处理速度

```Java
//发送数据间隔
parameter.setFrequencyBySend(200);//默认间隔为200毫秒

//监听数据间隔
parameter.setFrequencyByReceived(200);//默认间隔为200毫秒
```


* 修改权限地址

```Java
parameter.setSuPath("/system/xbin/su");
```



* 启动串口

```Java
SerialHelper.getInstance().serialStart(parameter);
```




属性说明
---------

| 属性			|说明			 |
| ------------- |  :-------------|
| suPath      			|	权限地址		|
| baudrate        |   波特率		|
| serialPath 		|	串口地址		|
| protocolHead 	|   协议开头		|
| protocolEnd 	|   协议结尾 	|
| protocolLength 			|   协议长度 	|
| proLenIndex 				|   协议长度截取的下标 从0开始	|
| protocolModel 			|   协议模型 	|
| uselessLength 			|   无用的数据长度 	|
| frequencyBySend 			|   写线程处理速度,  只能大于0 默认为200毫秒	|
| frequencyByReceived 			|   读线程处理速度,  只能大于0 默认为200毫秒 |
| queueSizeBySend 			|   写线程队列缓存长度, 只能大于0 默认为100	|
| queueSizeByReceived 			|   读线程队列缓存长度, 只能大于0 默认为100	|
| onSerialPortDataListener 			|   数据监听 |
| PROTOCOLMODEL_FIXED 			|   固定长度协议,  proLenIndex就没有意义了 |
| PROTOCOLMODEL_VARIABLE 			|   可变长度协议,  proLenIndex才有意义		|




效果演示
---------


项目成品
---------












