package com.example.wuzhi;

public class Modbus {
    /**地址对应表元素单元**/
    public class  OPTable{
        private int cmd;
        private byte[] ob;
        //无参的构造方法
        public OPTable(){

        }

        public int getCmd() {
            return cmd;
        }

        public void setCmd(int cmd) {
            this.cmd = cmd;
        }

        public byte[] getOb() {
            return ob;
        }

        public void setOb(byte[] ob) {
            this.ob = ob;
        }
    }

    /**当前的指令**/
    public class MBCmd{
        private byte command;//功能码
        private int res;//返回码的状态,0:无返回,1:正确返回

        public MBCmd(){

        }

        public byte getCommand() {
            return command;
        }

        public void setCommand(byte command) {
            this.command = command;
        }

        public int getRes() {
            return res;
        }

        public void setRes(int res) {
            this.res = res;
        }
    }

    /**当前操作的指令管道**/
    public  class MBSci{
        private MBCmd[] cmd;//指令结构体
        private int index;//当前索引
        private int count;//当前功能码执行的次数
        private int maxRepeatCount;//最大发送次数
        private int rtCount;//实时读取的指令的个数(无限间隔时间读取)

        //无参的构造方法
        public MBSci(){

        }
        //有参的构造方法
        public MBSci(MBCmd[] cmd, int index, int count, int maxRepeatCount, int rtCount) {
            this.cmd = cmd;
            this.index = index;
            this.count = count;
            this.maxRepeatCount = maxRepeatCount;
            this.rtCount = rtCount;
        }

        public MBCmd[] getCmd() {
            return cmd;
        }

        public void setCmd(MBCmd[] cmd) {
            this.cmd = cmd;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getMaxRepeatCount() {
            return maxRepeatCount;
        }

        public void setMaxRepeatCount(int maxRepeatCount) {
            this.maxRepeatCount = maxRepeatCount;
        }

        public int getRtCount() {
            return rtCount;
        }

        public void setRtCount(int rtCount) {
            this.rtCount = rtCount;
        }
    }

    /**枚举**/

    public enum Fault{
        NORMAL,//正常. java中枚举不能赋值,是常量,不可改变
        OVP,//过压
        OCP,//过流
        OTP,//过温
        LVP,//欠压
    }

    /**设置存储电压电流值**/

    public class  TypedefPara{// _typedef_para
        private int vSet;
        private int iSet;
        private int ovp;
        private  int ocp;

        public TypedefPara(){

        }

        public int getvSet() {
            return vSet;
        }

        public void setvSet(int vSet) {
            this.vSet = vSet;
        }

        public int getiSet() {
            return iSet;
        }

        public void setiSet(int iSet) {
            this.iSet = iSet;
        }

        public int getOvp() {
            return ovp;
        }

        public void setOvp(int ovp) {
            this.ovp = ovp;
        }

        public int getOcp() {
            return ocp;
        }

        public void setOcp(int ocp) {
            this.ocp = ocp;
        }
    }

    /**
     *  java 类型
     * 8bit unsigned integer －－－> short
     * 8bit signed integer －－－> byte
     * 16bit unsigned integer －－－> int
     * 16bit signed integer －－－> short
     * 32bit unsigned integer －－－> long
     * 32bit signed integer －－－> int
     * **/
    public class TypedefDisp{//_typedef_disp
        private int vIn;
        private int vOut;
        private int iOut;
        private int vSet;
        private int iSet;
        private int ovp;
        private int ocp;

        private long w;
        private int t;
        private long mWh;
        private long mAh;

        public TypedefDisp(){

        }
        public int getvIn() {
            return vIn;
        }

        public void setvIn(int vIn) {
            this.vIn = vIn;
        }

        public int getvOut() {
            return vOut;
        }

        public void setvOut(int vOut) {
            this.vOut = vOut;
        }

        public int getiOut() {
            return iOut;
        }

        public void setiOut(int iOut) {
            this.iOut = iOut;
        }

        public int getvSet() {
            return vSet;
        }

        public void setvSet(int vSet) {
            this.vSet = vSet;
        }

        public int getiSet() {
            return iSet;
        }

        public void setiSet(int iSet) {
            this.iSet = iSet;
        }

        public int getOvp() {
            return ovp;
        }

        public void setOvp(int ovp) {
            this.ovp = ovp;
        }

        public int getOcp() {
            return ocp;
        }

        public void setOcp(int ocp) {
            this.ocp = ocp;
        }

        public long getW() {
            return w;
        }

        public void setW(long w) {
            this.w = w;
        }

        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public long getmWh() {
            return mWh;
        }

        public void setmWh(long mWh) {
            this.mWh = mWh;
        }

        public long getmAh() {
            return mAh;
        }

        public void setmAh(long mAh) {
            this.mAh = mAh;
        }
    }

    public class TypedefState{//_typedef_state

        private byte onOff;
        private byte check;
        private byte cv_cc;
        private byte fault_state;
        private byte m;
        private byte remote;
        private byte discharge;
        private byte sue;

        public TypedefState(){

        }

        public byte getOnOff() {
            return onOff;
        }

        public void setOnOff(byte onOff) {
            this.onOff = onOff;
        }

        public byte getCheck() {
            return check;
        }

        public void setCheck(byte check) {
            this.check = check;
        }

        public byte getCv_cc() {
            return cv_cc;
        }

        public void setCv_cc(byte cv_cc) {
            this.cv_cc = cv_cc;
        }

        public byte getFault_state() {
            return fault_state;
        }

        public void setFault_state(byte fault_state) {
            this.fault_state = fault_state;
        }

        public byte getM() {
            return m;
        }

        public void setM(byte m) {
            this.m = m;
        }

        public byte getRemote() {
            return remote;
        }

        public void setRemote(byte remote) {
            this.remote = remote;
        }

        public byte getDischarge() {
            return discharge;
        }

        public void setDischarge(byte discharge) {
            this.discharge = discharge;
        }

        public byte getSue() {
            return sue;
        }

        public void setSue(byte sue) {
            this.sue = sue;
        }
    }

    /***全局参数*/

    public class ParamT{
        private long paramVer;//参数区版本控制(可用于程序升级时,决定是否对参数区进行升级)
        private long factorNo;//出厂编号
        private byte model;//型号 0 无 1 WZ6008
        private byte openFlag;//开机界面 0无  1有
        private byte onOff_mode;//默认输出状态 0关闭 1打开
        private byte voiceFlag;//声音标志位 0关闭按键声音 1打开按键声音
        private byte backLight;//背光亮度0-5
        private byte languageValue;//语言 0简体中文 1英文
        private byte address;//设备地址1
        private byte com_interface;//通信接口类型 0 TTL 1WIFI 2蓝牙 3USB  interface为关键字不能命名
        private byte callOut_onOff;//调出后输出状态 0不输出 1输出
        private byte window;//默认界面 0不数字 1曲线 2电池 3指针仪表
        private long baudRate;//波特率 0 9600 1 19200 2 38400 3 75600 4 115200

        private TypedefPara[] m;//存储4的参数

        private int vLow;//欠压阈值

        private float tOver;//过温度阈值

        private byte smartconfigFlag;
        private byte ch1AttId;
        private byte ch2AttId;
        /**wipIp 地址参数**/
        private byte[] wipIp;//本机IP地址

        //无参的构造方法
        public ParamT(){

        }


        public long getParamVer() {
            return paramVer;
        }

        public void setParamVer(long paramVer) {
            this.paramVer = paramVer;
        }

        public long getFactorNo() {
            return factorNo;
        }

        public void setFactorNo(long factorNo) {
            this.factorNo = factorNo;
        }

        public byte getModel() {
            return model;
        }

        public void setModel(byte model) {
            this.model = model;
        }

        public byte getOpenFlag() {
            return openFlag;
        }

        public void setOpenFlag(byte openFlag) {
            this.openFlag = openFlag;
        }

        public byte getOnOff_mode() {
            return onOff_mode;
        }

        public void setOnOff_mode(byte onOff_mode) {
            this.onOff_mode = onOff_mode;
        }

        public byte getVoiceFlag() {
            return voiceFlag;
        }

        public void setVoiceFlag(byte voiceFlag) {
            this.voiceFlag = voiceFlag;
        }

        public byte getBackLight() {
            return backLight;
        }

        public void setBackLight(byte backLight) {
            this.backLight = backLight;
        }

        public byte getLanguageValue() {
            return languageValue;
        }

        public void setLanguageValue(byte languageValue) {
            this.languageValue = languageValue;
        }

        public byte getAddress() {
            return address;
        }

        public void setAddress(byte address) {
            this.address = address;
        }

        public byte getCom_interface() {
            return com_interface;
        }

        public void setCom_interface(byte com_interface) {
            this.com_interface = com_interface;
        }

        public byte getCallOut_onOff() {
            return callOut_onOff;
        }

        public void setCallOut_onOff(byte callOut_onOff) {
            this.callOut_onOff = callOut_onOff;
        }

        public byte getWindow() {
            return window;
        }

        public void setWindow(byte window) {
            this.window = window;
        }

        public long getBaudRate() {
            return baudRate;
        }

        public void setBaudRate(long baudRate) {
            this.baudRate = baudRate;
        }

        public TypedefPara[] getM() {
            return m;
        }

        public void setM(TypedefPara[] m) {
            this.m = m;
        }

        public int getvLow() {
            return vLow;
        }

        public void setvLow(int vLow) {
            this.vLow = vLow;
        }

        public float gettOver() {
            return tOver;
        }

        public void settOver(float tOver) {
            this.tOver = tOver;
        }

        public byte getSmartconfigFlag() {
            return smartconfigFlag;
        }

        public void setSmartconfigFlag(byte smartconfigFlag) {
            this.smartconfigFlag = smartconfigFlag;
        }

        public byte getCh1AttId() {
            return ch1AttId;
        }

        public void setCh1AttId(byte ch1AttId) {
            this.ch1AttId = ch1AttId;
        }

        public byte getCh2AttId() {
            return ch2AttId;
        }

        public void setCh2AttId(byte ch2AttId) {
            this.ch2AttId = ch2AttId;
        }

        public byte[] getWipIp() {
            return wipIp;
        }

        public void setWipIp(byte[] wipIp) {
            this.wipIp = wipIp;
        }
    }


    public static  final byte MB_WRITE_MODE = 0x20;             //设置操作模式

    public static  final  byte MB_WRITE_ADDRESS = 0x21;           //设置

    public static  final  byte MB_WRITE_POWER = 0x22;             //设置操作模式

    public static  final  byte MB_READ_STATE = 0x23;             //设置操作模式

    public static  final byte MB_READ_INFO = 0x24;             //设置操作模式


    public static  final  byte MB_READ_SYSTEM = 0x25;             //设置操作模式

    public static  final  byte MB_WRITE_SYSTEM = 0x26;             //设置操作模式


    public static  final  byte MB_READ_M = 0x27;             //设置操作模式

    public static  final  byte MB_WRITE_M = 0x28;             //设置操作模式


    public static  final  byte MB_READ_DISPLAY1 = 0x29;             //设置操作模式

    public static  final  byte MB_READ_DISPLAY2 = 0x2A;             //设置操作模式

    public static  final   byte MB_READ_DISPLAY3 = 0x2B;             //设置操作模式


    public static  final  byte MB_WRITE_DISPLAY = 0x2C;             //设置操作模式


    public static  final  byte MB_READ_M0 = 0x2D;             //设置操作模式

    public static  final  byte MB_WRITE_M0 = 0x2E;             //设置操作模式


    public static  final byte MB_READ_M1 = 0x2F;             //设置操作模式

    public static  final  byte MB_WRITE_M1 = 0x30;             //设置操作模式


    public static  final  byte MB_READ_M2 = 0x31;             //设置操作模式

    public static  final  byte MB_WRITE_M2 = 0x32;             //设置操作模式


    public static  final  byte MB_READ_M3 = 0x33;             //设置操作模式

    public static  final byte MB_WRITE_M3 = 0x34;             //设置操作模式


    public static  final  byte MB_READ_M4 = 0x35;             //设置操作模式

    public static  final   byte MB_WRITE_M4 = 0x36;             //设置操作模式


    public static  final  byte MB_WRITE_PROTECT = 0x50;             //设置操作模式


    public static  final  byte MB_WRITE_ZERO = 0x60;             //设置操作模式

    public static  final  byte MB_WRITE_RESET = 0x61;             //恢复出厂



    public static  final  int MB_MAX_LENGTH = 120;               //最大数据长度

    public static  final  int MB_SCI_MAX_COUNT = 15;             //指令管道最大存放的指令各数

    public static  final int MB_MAX_REPEAT_COUNT = 3;           //指令最多发送次数

    private static boolean sciLock=false;//调度器锁 true:加锁  false:解锁
    private static  byte[] buff=new byte[MB_MAX_LENGTH];//接收缓冲器
    private static  int buffLen=0;
    private static  byte[] rBuff=null;//正确接收缓冲器
    private static byte[] wBuff=null;//正确发送缓冲器


    public  MBSci gMBSci=new MBSci(new MBCmd[MB_MAX_REPEAT_COUNT],0,MB_MAX_REPEAT_COUNT,0,0);




}
