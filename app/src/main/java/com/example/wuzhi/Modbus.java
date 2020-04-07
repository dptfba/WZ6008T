package com.example.wuzhi;


public class Modbus {

    /**地址对应表元素单元**/
    public static class  OPTable{
        private int cmd;
        private byte[] ob;
        //无参的构造方法
        public OPTable(){

        }

        public OPTable(int cmd, byte[] ob) {
            this.cmd = cmd;
            this.ob = ob;
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

        //无参的构造方法
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

        //无参的构造方法
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

        //无参的构造方法
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

        //无参的构造方法
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

    public class ParamT{//PARAM_T
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

    public static  final  byte MB_WRITE_ADDRESS = 0x21;           //设置新通讯地址

    public static  final  byte MB_WRITE_POWER = 0x22;             //设置电源输出状态

    public static  final  byte MB_READ_STATE = 0x23;             //读取状态

    public static  final byte MB_READ_INFO = 0x24;             //读取产品信息


    public static  final  byte MB_READ_SYSTEM = 0x25;             //读取系统参数

    public static  final  byte MB_WRITE_SYSTEM = 0x26;             //设置系统参数


    public static  final  byte MB_READ_M = 0x27;             //读取当前显示的设定值

    public static  final  byte MB_WRITE_M = 0x28;             //设置设置当前显示的设定值


    public static  final  byte MB_READ_DISPLAY1 = 0x29;             //读取显示

    public static  final  byte MB_READ_DISPLAY2 = 0x2A;             //读取显示

    public static  final   byte MB_READ_DISPLAY3 = 0x2B;             //读取显示


    public static  final  byte MB_WRITE_DISPLAY = 0x2C;             //设置显示


    public static  final  byte MB_READ_M0 = 0x2D;             //读取M0

    public static  final  byte MB_WRITE_M0 = 0x2E;             //设置M0


    public static  final byte MB_READ_M1 = 0x2F;             //读取M1

    public static  final  byte MB_WRITE_M1 = 0x30;             //设置M1


    public static  final  byte MB_READ_M2 = 0x31;             //读取M2

    public static  final  byte MB_WRITE_M2 = 0x32;             //设置M2


    public static  final  byte MB_READ_M3 = 0x33;             //读取M3

    public static  final byte MB_WRITE_M3 = 0x34;             //设置M3


    public static  final  byte MB_READ_M4 = 0x35;             //读取M4

    public static  final   byte MB_WRITE_M4 = 0x36;             //设置M4


    public static  final  byte MB_WRITE_PROTECT = 0x50;             //设置电源校准保护状态


    public static  final  byte MB_WRITE_ZERO = 0x60;             //设置电流校零点

    public static  final  byte MB_WRITE_RESET = 0x61;             //恢复出厂默认



    public static  final  int MB_MAX_LENGTH = 120;               //最大数据长度

    public static  final  int MB_SCI_MAX_COUNT = 15;             //指令管道最大存放的指令个数

    public static  final int MB_MAX_REPEAT_COUNT = 3;           //指令最多发送次数

    private static boolean sciLock=false;//调度器锁 true:加锁  false:解锁
    private static  byte[] buff=new byte[MB_MAX_LENGTH];//接收缓冲器
    private static  int buffLen=0;
    private static  byte[] rBuff=null;//正确接收缓冲器
    private static byte[] wBuff=null;//正确发送缓冲器


    public  MBSci gMBSci=new MBSci(new MBCmd[MB_MAX_REPEAT_COUNT],0,MB_MAX_REPEAT_COUNT,0,0);


    private static SerialPort comm=null;
    private static int mbRefreshTime=0;

    public static TypedefDisp display;
    public static ParamT g_Param;

    public static TypedefState g_State;

    public static  int cmd_delay=0;

    //变量所对应的地址,在此位置
    public static OPTable[] mbDataTable={//MBDataTable
            new OPTable(MB_WRITE_MODE,new byte[20]),//0
            new OPTable(MB_WRITE_ADDRESS,new byte[20]),
            new OPTable(MB_WRITE_POWER,new byte[20]),
            new OPTable(MB_READ_STATE,new byte[20]),
            new OPTable(MB_READ_INFO,new byte[20]),
            new OPTable(MB_READ_SYSTEM,new byte[20]),
            new OPTable(MB_WRITE_SYSTEM,new byte[20]),//6
            new OPTable(MB_READ_M,new byte[20]),
            new OPTable(MB_WRITE_M,new byte[20]),
            new OPTable(MB_READ_DISPLAY1,new byte[20]),
            new OPTable(MB_READ_DISPLAY2,new byte[20]),//10
            new OPTable(MB_READ_DISPLAY3,new byte[20]),
            new OPTable(MB_READ_M0,new byte[20]),//12
            new OPTable(MB_WRITE_M0,new byte[20]),
            new OPTable(MB_READ_M1,new byte[20]),
            new OPTable(MB_WRITE_M1,new byte[20]),
            new OPTable(MB_READ_M2,new byte[20]),//16
            new OPTable(MB_WRITE_M2,new byte[20]),
            new OPTable(MB_READ_M3,new byte[20]),
            new OPTable(MB_WRITE_M3,new byte[20]),
            new OPTable(MB_READ_M4,new byte[20]),
            new OPTable(MB_WRITE_M4,new byte[20]),
            new OPTable(MB_WRITE_PROTECT,new byte[20]),
            new OPTable(MB_WRITE_ZERO,new byte[20]),
            new OPTable(MB_WRITE_RESET,new byte[20])

    };

    public static byte gNode= (byte) 170;
    public static  int gBaud=9600;

    static byte bCC_CheckSum(byte[] buf,int len){
        byte checksum=0;
        int sum=0;
        for(int i=0;i<len;i++){
            sum=sum+buf[i];

        }
        checksum=(byte)(sum%256);
        return checksum;
    }

    //获取地址所对应的数据
    private static Object getAddressValue(byte cmd){
       for(int i=0;i<mbDataTable.length;i++){
           if(mbDataTable[i].cmd==cmd){
               return mbDataTable[i].cmd;

           }

       }
       return null;
    }


    //设置地址所对应的数据
    private static Object setAddressValue(byte cmd,byte[] data){
        for(int i=0;i<mbDataTable.length;i++){
            if(mbDataTable[i].cmd==cmd){
                System.arraycopy(data,0,mbDataTable[i].ob,0,20);
                return true;
            }

        }
        return null;

    }

    //累加和校验
    private static byte sumCheck(byte[] bs,int length){
        int num=0;
        //所有字节累加
        for(int i=0;i<length;i++){
            num=(num+bs[i])%0xFFFF;

        }
        byte ret=(byte)(num&0xff);//只要最后一个字节
        return ret;
    }

    /**
     * 主控方式,发送指令模板
     * node为节点,data为数据,addr为地址,con为变量各数,stat为功能码
     * **/
    private static byte[] sendTrainCyclostyle(byte node,byte[] data,byte cmd){
        byte crcVal=0;
        //byte[] headData = SendTrainHead(node, cmd); //写首部分数据
        //byte[] headDataLen = SendTrainBytes(headData, ref con, stat); //计算数据的长度，有字节则写入。
        //byte[] res = new byte[headDataLen.Length + con + 2];
        byte[] res = new byte[20];

        //headDataLen.CopyTo(res, 0);

        //if ((stat == MB_WRITE_MULTIPLE_REGS) || (stat == MB_WRITE_MULTIPLE_COILS))
        //Array.Copy(data, 0, res, headDataLen.Length, con);  //把数据复制到数据中
        res[0] = node;
        res[1] = g_Param.address;
        res[2] = cmd;
        System.arraycopy(data, 0, res, 3, 16);

        crcVal = bCC_CheckSum(res, 19);
        res[19] = crcVal;
        return res;

    }

    /**
     * 封装发送数据帧
     * node为从机地址,cmd为指令信息
     * */

    private static byte[] sendPduPack(byte node,MBCmd cmd) {
        byte[] res = null;
        byte[] data = new byte[16];
        switch (cmd.command) {
            case MB_READ_STATE:
            case MB_READ_SYSTEM:
            case MB_READ_INFO:
            case MB_READ_M:
            case MB_READ_DISPLAY1:
            case MB_READ_DISPLAY2:
            case MB_READ_DISPLAY3:
            case MB_READ_M0:
            case MB_READ_M1:
            case MB_READ_M2:
            case MB_READ_M3:
            case MB_READ_M4:
                sendTrainCyclostyle(node, data, cmd.command);
                break;

            case MB_WRITE_MODE:
                data[0] = g_State.remote;
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            //case MB_WRITE_ADDRESS:
            //    data[0] = 0x01;
            //    res =  sendTrainCyclostyle(node, data, cmd.command);
            //break;
            case MB_WRITE_POWER:
                data[0] = g_State.onOff;
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            //case MB_WRITE_SYSTEM:
            //    data[0] = g_State.Onoff;
            //    res = sendTrainCyclostyle(node, data, cmd.command);
            //break;

            case MB_WRITE_DISPLAY:
                data[0] = (byte) (display.ovp >> 8);
                data[1] = (byte) (display.ovp & 0xFF);
                data[2] = (byte) (display.ocp >> 8);
                data[3] = (byte) (display.ocp & 0xFF);
                data[4] = (byte) (display.vSet >> 8);
                data[5] = (byte) (display.vSet & 0xFF);
                data[6] = (byte) (display.iSet >> 8);
                data[7] = (byte) (display.iSet & 0xFF);
                data[8] = (byte) (display.t >> 24);
                data[9] = (byte) ((display.t >> 16) & 0xFF);
                data[10] = (byte) ((display.t >> 8) & 0xFF);//高8位
                data[11] = (byte) (display.t & 0xFF);//低位
                //data[12] = (byte)(display.ovp >> 8);
                //data[13] = (byte)(display.ovp % 8);
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_M:
                data[0] = g_State.m;
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;

            case MB_WRITE_M0:
                data[0] = (byte) (g_Param.m[0].ovp >> 8);
                data[1] = (byte) (g_Param.m[0].ovp & 0xFF);
                data[2] = (byte) (g_Param.m[0].ocp >> 8);
                data[3] = (byte) (g_Param.m[0].ocp & 0xFF);
                data[4] = (byte) (g_Param.m[0].vSet >> 8);
                data[5] = (byte) (g_Param.m[0].vSet & 0xFF);
                data[6] = (byte) (g_Param.m[0].iSet >> 8);
                data[7] = (byte) (g_Param.m[0].iSet & 0xFF);
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_M1:
                data[0] = (byte) (g_Param.m[1].ovp >> 8);
                data[1] = (byte) (g_Param.m[1].ovp & 0xFF);
                data[2] = (byte) (g_Param.m[1].ocp >> 8);
                data[3] = (byte) (g_Param.m[1].ocp & 0xFF);
                data[4] = (byte) (g_Param.m[1].vSet >> 8);
                data[5] = (byte) (g_Param.m[1].vSet & 0xFF);
                data[6] = (byte) (g_Param.m[1].iSet >> 8);
                data[7] = (byte) (g_Param.m[1].iSet & 0xFF);
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_M2:
                data[0] = (byte) (g_Param.m[2].ovp >> 8);
                data[1] = (byte) (g_Param.m[2].ovp & 0xFF);
                data[2] = (byte) (g_Param.m[2].ocp >> 8);
                data[3] = (byte) (g_Param.m[2].ocp & 0xFF);
                data[4] = (byte) (g_Param.m[2].vSet >> 8);
                data[5] = (byte) (g_Param.m[2].vSet & 0xFF);
                data[6] = (byte) (g_Param.m[2].iSet >> 8);
                data[7] = (byte) (g_Param.m[2].iSet & 0xFF);
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_M3:
                data[0] = (byte) (g_Param.m[3].ovp >> 8);
                data[1] = (byte) (g_Param.m[3].ovp & 0xFF);
                data[2] = (byte) (g_Param.m[3].ocp >> 8);
                data[3] = (byte) (g_Param.m[3].ocp & 0xFF);
                data[4] = (byte) (g_Param.m[3].vSet >> 8);
                data[5] = (byte) (g_Param.m[3].vSet & 0xFF);
                data[6] = (byte) (g_Param.m[3].iSet >> 8);
                data[7] = (byte) (g_Param.m[3].iSet & 0xFF);
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_M4:
                data[0] = (byte) (g_Param.m[4].ovp >> 8);
                data[1] = (byte) (g_Param.m[4].ovp & 0xFF);
                data[2] = (byte) (g_Param.m[4].ocp >> 8);
                data[3] = (byte) (g_Param.m[4].ocp & 0xFF);
                data[4] = (byte) (g_Param.m[4].vSet >> 8);
                data[5] = (byte) (g_Param.m[0].vSet & 0xFF);
                data[6] = (byte) (g_Param.m[0].iSet >> 8);
                data[7] = (byte) (g_Param.m[0].iSet & 0xFF);
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_ZERO:
                data[0] = 0x01;
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;
            case MB_WRITE_RESET:
                data[0] = 0x01;
                res = sendTrainCyclostyle(node, data, cmd.command);
                break;

        }
        return res;


    }

    /**
     * 回传的数据处理
     * buff为回传的整帧数据,addr为当前所操作的首地址
     **/

    private static boolean receiveDataProcess(byte[] buff,byte cmd){
        if(buff==null){
            return false;

        }
        if(buff.length<5){//回传的数据 地址+功能码+长度+2效验=5字节
            return false;

        }

        boolean res=true;
        switch (buff[2]){
            case MB_READ_STATE:
                if(cmd_delay==0){
                   g_State.onOff=buff[3] ;
                   g_State.cv_cc=buff[4];
                   g_State.fault_state=buff[5];
                }
                break;
            //case MB_READ_INFO:  break;
            //case MB_READ_SYSTEM:  break;           data[0] =(byte) (display.ovp>>8);

            case MB_READ_M:
                g_State.m = buff[3];
                ;
                break;
            case MB_READ_DISPLAY1:
                display.vIn = buff[3];
                display.vIn = (int) ((display.vIn << 8) + buff[4]);

                display.vOut = buff[5];
                display.vOut = (int) ((display.vOut << 8) + buff[6]);

                display.iOut = buff[7];
                display.iOut = (int) ((display.iOut << 8) + buff[8]);

                display.w = (int) ((buff[9] << 24) + (buff[10] << 16) + (buff[11] << 24) + buff[12]);
                break;
            //case MB_READ_DISPLAY2:
            //    display.vIn = buff[0];
            //    display.vIn = (int)((display.vIn << 8) + buff[1]);

            //    display.vOut = buff[3];
            //    display.vOut = (int)((display.vOut << 8) + buff[4]);

            //    display.iOut = buff[5];
            //    display.iOut = (int)((display.iOut << 8) + buff[6]);

            //    display.w = (int)((buff[7] << 24) + (buff[8] << 16) + (buff[9] << 24) + buff[10]);
            //    break;
            //case MB_READ_DISPLAY3: readReg(buff, addr); break;
            case MB_READ_DISPLAY3:
                display.ovp = buff[3];
                display.ovp = (int) ((display.ovp << 8) + buff[4]);

                display.ocp = buff[5];
                display.ocp = (int) ((display.ocp << 8) + buff[6]);

                display.vSet = buff[7];
                display.vSet = (int) ((display.vSet << 8) + buff[8]);

                display.iSet = buff[9];
                display.iSet = (int) ((display.iSet << 8) + buff[10]);

                display.t = (int) ((buff[11] << 24) + (buff[12] << 16) + (buff[13] << 24) + buff[14]);

                g_Param.vLow = buff[15];
                g_Param.vLow = (int) ((g_Param.vLow << 8) + buff[16]);

                break;
            case MB_READ_M0:
                g_Param.m[0].ovp = buff[3];
                g_Param.m[0].ovp = (int) ((display.ovp << 8) + buff[4]);

                g_Param.m[0].ocp = buff[5];
                g_Param.m[0].ocp = (int) ((display.ocp << 8) + buff[6]);

                g_Param.m[0].vSet = buff[7];
                g_Param.m[0].vSet = (int) ((display.vSet << 8) + buff[8]);

                g_Param.m[0].iSet = buff[9];
                g_Param.m[0].iSet = (int) ((display.iSet << 8) + buff[10]);
                break;
            case MB_READ_M1:
                g_Param.m[1].ovp = buff[3];
                g_Param.m[1].ovp = (int) ((display.ovp << 8) + buff[4]);

                g_Param.m[1].ocp = buff[5];
                g_Param.m[1].ocp = (int) ((display.ocp << 8) + buff[6]);

                g_Param.m[1].vSet = buff[7];
                g_Param.m[1].vSet = (int) ((display.vSet << 8) + buff[8]);

                g_Param.m[1].iSet = buff[9];
                g_Param.m[1].iSet = (int) ((display.iSet << 8) + buff[10]);
                break;
            case MB_READ_M2:
                g_Param.m[2].ovp = buff[3];
                g_Param.m[2].ovp = (int) ((display.ovp << 8) + buff[4]);

                g_Param.m[2].ocp = buff[5];
                g_Param.m[2].ocp = (int) ((display.ocp << 8) + buff[6]);

                g_Param.m[2].vSet = buff[7];
                g_Param.m[2].vSet = (int) ((display.vSet << 8) + buff[8]);

                g_Param.m[2].iSet = buff[9];
                g_Param.m[2].iSet = (int) ((display.iSet << 8) + buff[10]);
                break;
            case MB_READ_M3:
                g_Param.m[3].ovp = buff[3];
                g_Param.m[3].ovp = (int) ((display.ovp << 8) + buff[4]);

                g_Param.m[3].ocp = buff[5];
                g_Param.m[3].ocp = (int) ((display.ocp << 8) + buff[6]);

                g_Param.m[3].vSet = buff[7];
                g_Param.m[3].vSet = (int) ((display.vSet << 8) + buff[8]);

                g_Param.m[3].iSet = buff[9];
                g_Param.m[3].iSet = (int) ((display.iSet << 8) + buff[10]);
                break;
            case MB_READ_M4:
                g_Param.m[4].ovp = buff[3];
                g_Param.m[4].ovp = (int) ((display.ovp << 8) + buff[4]);

                g_Param.m[4].ocp = buff[5];
                g_Param.m[4].ocp = (int) ((display.ocp << 8) + buff[6]);

                g_Param.m[4].vSet = buff[7];
                g_Param.m[4].vSet = (int) ((display.vSet << 8) + buff[8]);

                g_Param.m[4].iSet = buff[9];
                g_Param.m[4].iSet = (int) ((display.iSet << 8) + buff[10]);
                break;

            case 0x12:
                if (buff[3] != 0x80) {
                    res = false;
                }
                break;
            default:
                res = false;
                break;


        }
        return res;

    }

    /**
     * 添加重复操作指令
     * sci为待发送的指令管道   addr为所添加指令的首地址  len 为所添加指令的寄存器或线圈格式
     * stat为所添加指令的功能码
     * */

    private static void sciAddRepeatCmd(MBSci sci,byte cmd){

        if(sci.rtCount>=MB_SCI_MAX_COUNT-1){//超出指令管道最大长度  直接退出
            return;

        }
//        if(len==0){//地址的数据长度为空  直接退出
//            return;
//        }
        for(int i=0;i<sci.rtCount;i++){
            if(sci.cmd[sci.rtCount].command==cmd){
                return;

            }

        }
       // sci.cmd[sci.rtCount].addr=addr;
       // sci.cmd[sci.rtCount].len=len;
        sci.cmd[sci.rtCount].command=cmd;
        sci.cmd[sci.rtCount].res=0;
        sci.rtCount++;

    }

    /**
     * 添加一次性操作指令
     * sci为待发送的指令管道    addr为所添加指令的首地址
     * len 为所添加指令的寄存器或线圈个数  stat所添加指令的功能码
     * */
    private static void sciAddCmd(MBSci sci,byte cmd){
//        if(len==0){//地址的数据长度为空  直接退出
//            return;
//        }

        for(int i=sci.rtCount;i<MB_SCI_MAX_COUNT;i++){
            if(sci.cmd[i].command==0){//将指令载入到空的管道指令上
                //sci.cmd[i].addr=addr;
                //sci.cmd[i].len=len;
                sci.cmd[i].command=cmd;
                sci.cmd[i].res=0;
                break;
            }

        }
    }

    /**
     * 清空重复读取指令集
     * sci为待发送的指令管道
     * */
    private static void sciClearRepeatCmd(MBSci sci){
        sci.rtCount=0;

    }

    /**
     * 清空一次性读取指令集
     * sci为待发送的指令管道
     * */
    private static void sciClearCmd(MBSci sci){
        for(int i=sci.rtCount;i<MB_SCI_MAX_COUNT;i++){
            sci.cmd[i].command=0;
            //sci.cmd[i].addr = -1;
            //sci.cmd[i].len = 0;
            sci.cmd[i].res = 0;

        }

    }

    /**
     * 跳到下一个操作指令
     * sci为待发送的指令管道
     * */

    private static void sciJumbNext(MBSci sci){
        if(sci.index>=sci.rtCount){//非实时读取地址会被清除
            sci.cmd[sci.index].command=0;
            //sci.cmd[sci.index].len = 0;
            //sci.cmd[sci.index].stat = 0;

        }
        do{
            sci.index++;
            if(sci.index>=MB_SCI_MAX_COUNT){//超出指令最大范围
                sci.index=0;
                if(sci.rtCount==0){//如果固定实时读取为空 直接跳出
                    break;
                }

            }
        }while (sci.cmd[sci.index].command==0);
        sci.cmd[sci.index].res=0;//本次返回状态清零

    }

    /**
     * 发送指令调度锁定
     * */
    public static void sciSchedulingLock(){
        sciLock=true;
    }

    /**
     * 发送指令调度解锁
     * */
    public static void sciSchedulingUnlock(){
        sciLock=false;
    }

    /**
     * 待发送的指令管道调度
     * sci为待发送的指令管道
     * rBuf为收到正确的回传数据
     * wBuf为准备发送的指令数据
     * */
    private static void sciScheduling(MBSci sci,byte[] rBuf,byte[] wBuf){
        if(sciLock){//如果别加锁,直接退出
            return;
        }
        if((sci.cmd[sci.index].res!=0)||(sci.count>=sci.maxRepeatCount)){
            sci.count=0;//发送次数清零
            if(sci.cmd[sci.index].res!=0){//如果收到了正常返回
               // receiveDataProcess(rBuf,sci.cmd[sci.index].command);//保存数据
               //rBuf=null;//清空当前接收缓冲区的内容,以防下次重复读取

            }else {
                //参数操作失败
            }
            rBuf=null;//清空当前接收缓冲区的内容,以防下次重复读取
            sciJumbNext(sci);

        }
        wBuf=sendPduPack(gNode,sci.cmd[sci.index]);//发送指令操作
        sci.count++;//发送次数加1

    }

    /**
     * 快速刷新  处理接收到的数据  建议:10ms一下
     * 返回所正确回传数据的功能码,null为回传不正确
     * */

    private  byte mbQuickRefresh(){
        byte res=0;
        if(rBuff!=null){
            sciSchedulingLock();
            if(receiveDataProcess(rBuff,gMBSci.cmd[gMBSci.index].command)==true){
                gMBSci.cmd[gMBSci.index].res=1;//标记  所接收到的数据正确
                res= gMBSci.cmd[gMBSci.index].command;

            }
            rBuff=null;
            sciSchedulingUnlock();

        }
        return res;
    }

    /**
     * 调度间隔时间刷新   建议:50ms以上
     * 返回封装好的协议帧
     * */
    private  void mbSchedRefresh(){
        sciScheduling(gMBSci,rBuff,wBuff);
//        if(wBuff!=null){
//
//        }
        comm.write(wBuff,0,20);
    }

    /**
     * 清空存放一次性的指令空间
     * */
    public  void mbClearCmd(){
        sciClearCmd(gMBSci);
    }

    /**
     * 添加固定刷新(重复)操作指令
     * addr 为地址,stat为功能码
     * */
    public  void mbAddRepeatCmd(byte cmd){
        //for (int i = 0; i < GetAddressValueLength(addr); i++ )
        if(getAddressValue(cmd)==null){//如果所添加的指令没有在MODBUS对应表中定义,直接退出
            return;
        }
        sciAddRepeatCmd(gMBSci,cmd);
    }

    /**
     * 添加一次性操作指令
     */
    public  void mbAddCmd(byte cmd){
        //for (int i = 0; i < GetAddressValueLength(addr); i++)
        //    if (GetAddressValue(addr, stat) == null)        //如果所添加的指令没有在MODBUS对应表中定义 直接退出
        //        return;
        sciAddCmd(gMBSci, cmd);
    }

    /**
     * 串口参数配置
     * commx 为所用到的串口
     * */
    public  void mbConfig(SerialPort commx, byte node, int baud){
        gBaud=baud;
        gNode=node;
        comm=commx;
        sciClearRepeatCmd(gMBSci);
        sciClearCmd(gMBSci);

    }

    /**
     * 读取串口中接收到的数据
     * comm为所用到的串口
     * */
    public  void mbDataReceive(){
        if(comm==null){//如果串口没有被初始化直接退出
            return;

        }
        sciSchedulingLock();
        try {
            Thread.sleep(20);//等待缓存器满
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffLen=comm.BytesToRead;//获取缓存区字节长度
        if (buffLen > MB_MAX_LENGTH)            //如果长度超出范围 直接退出
        {
            sciSchedulingUnlock();
            return;
        }
        comm.Read(buff, 0, buffLen);            //读取数据
        if ((buff[0] == gNode) && (buffLen == 20)) {
            if (sumCheck(buff, buffLen - 1) == buff[19]) {
                rBuff = new byte[buffLen];
              // Array.Copy(buff, rBuff, buffLen);//把buff复制给rBuff,buff是源数据
               System.arraycopy(buff,0, rBuff,0, buffLen);

            }
        }
        sciSchedulingUnlock();
    }

    /**
     * MODBUS的实时刷新任务，在定时器在实时调用此函数
     * 指令发送间隔时间等于实时器乘以10。 例：定时器5ms调用一次  指令发送间隔为50ms。
     * 返回当前功能读取指令回传的功能码
     */

    public  int mbRefresh(){
        if (sciLock)   //如果被加锁 直接退出
            return 0;

        mbRefreshTime++;
        if (mbRefreshTime > 20)
        {
            mbRefreshTime = 0;
            mbSchedRefresh();
        }
        return mbQuickRefresh();
    }

}
