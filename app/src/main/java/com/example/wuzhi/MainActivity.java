package com.example.wuzhi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuzhi.Utils.ExcelUtils;
import com.example.wuzhi.Utils.LocaleUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    UpdateManager updateManager;//APP自动更新类


    String tag = "=======err";
    private DecimalFormat mDecimalFormat = new DecimalFormat("#.00");//格式化显示浮点数位两位小数

    private long eixtTime = 0;//存在时间


    //侧滑抽屉控件
    NavigationView navigationView;//导航菜单
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    Button btn_connect;//toolbar上的连接
    TextView tv_save;//toolbar上的存储


    TextView tv_ip;//IP地址显示文本框

    LineChart lineChart;//折线表,存线集合


    Chronometer chronometer_bootTime;//开机时间
    // Chronometer chronometer_writeTime;//记录时间
    TextView tv_writeTime;//记录时间
    int miss1 = 0;
    int miss2 = 0;
    private boolean isPause = false;//用于判断是否为暂停状态
    Button btn_pause;//暂停按钮
    Button btn_clear;//清除按钮


    ImageButton btn_switch;//开机关机开关
    private boolean isOn = false;//用于判断是否是开机状态

    TextView tv_voltage;//输出电压
    TextView tv_current;//输入电流
    TextView tv_power;//输出功率
    TextView tv_energy;//能量
    TextView tv_capacity;//容量
    EditText et_setU;//U_SET
    EditText et_setI;//I_SET
    EditText et_ovp;//ovp
    EditText et_ocp;//ocp
    Button btn_stateButton;//状态按钮
    Button btn_hintButton;//CC CV按钮
    TextView tv_inputVoltage;//输入电压


    //数据通信
    boolean MonitorFlage = false;//连接按钮循环显示连接和断开
    boolean MonitorConnectFlage = true;//监听任务
    private ServerSocket serverSocket = null;//创建ServerSocket对象
    int socketPort = 18041;
    Socket socket = null;//连接通道,创建Socket对象
    OutputStream outputStream;//获取输出流
    InputStream inputStream;//获取输入流
    ThreadMonitorConnect threadMonitorConnect = new ThreadMonitorConnect();//监听连接的线程
    ThreadSendData threadSendData = new ThreadSendData();//发送数据线程

    boolean threadReadDataFlage = false;//接收数据任务运行控制
    boolean threadSendDataFlage = false;//发送数据任务运行控制
    boolean sendDataFlage = false;//可以发送数据

    byte[] sendBuffer = new byte[2048];//存储发送的数据
    byte[] ReadBuffer = new byte[2048];//存储接收的数据
    int ReadBufferLenght = 0;//接收到数据的长度
    int sendDataCnt = 0;//控制发送数据的个数


    ArrayList<Socket> arrayListSockets = new ArrayList<>();//存储连接的Socket
    private List<String> listClient = new ArrayList<>();//字符串集合

    private SharedPreferences sharedPreferences;//存储数据
    private SharedPreferences.Editor editor;//存储数据

    String portSaveData = "";
    String sendDataString = "";

    Thread mthreadSendData;//记下发送任务,便于停止
    Thread mthreadMonitorConnect;//记下监听任务,便于停止

    byte[] sendByteArray = {(byte) 0xAA, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,};
    int sendFlag = 0;//记录发送数据变量


    //设备地址选择保存
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor1;
    String addressStr;


    private Timer timer = new Timer();//定时器
    private TimerTask timerTask = null;//定时任务

    MyHandler mHandler;//handler
    Runnable runnable;


    /**
     * Excel表格相关
     **/
    private String excelFilePath = "";

    private String[] colNames = new String[]{"电流", "电压", "电流", "电压", "电流", "电压",};//Excel表格每列列头标题
    String[] pess = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_main);

        wifiOpen();//打开wifi的方法

        //软件更新的检查调用
        // updateManager=new UpdateManager(MainActivity.this);
        //  updateManager.checkUpdateInfo();


        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav);

        btn_connect = findViewById(R.id.btn_connect);//连接
        tv_save = findViewById(R.id.tv_save);//存储


        chronometer_bootTime = findViewById(R.id.chronometer_bootTime);
        // chronometer_writeTime = findViewById(R.id.chronometer_writeTime);
        tv_writeTime = findViewById(R.id.tv_writeTime);
        startChronometer();//开机计时
        //  writeChronometer();//记录时间计时
        btn_pause = findViewById(R.id.btn_startOrPause);//暂停或开始按钮
        btn_clear = findViewById(R.id.btn_clear);//暂停按钮

        btn_switch = findViewById(R.id.btn_switch);//开关按钮


        tv_voltage = findViewById(R.id.tv_voltage);//输出电压
        tv_current = findViewById(R.id.tv_current);//输入电流
        tv_power = findViewById(R.id.tv_power);//输出功率
        tv_energy = findViewById(R.id.tv_energy);//能量
        tv_capacity = findViewById(R.id.tv_capacity);//容量
        et_setU = findViewById(R.id.et_setU);//U_SET
        et_setI = findViewById(R.id.et_setI);//I_SET
        et_ovp = findViewById(R.id.et_ovp);//ovp
        et_ocp = findViewById(R.id.et_ocp);//ocp
        btn_stateButton = findViewById(R.id.btn_stateButton);//状态按钮
        btn_hintButton = findViewById(R.id.btn_hintButton);//CC CV按钮
        tv_inputVoltage = findViewById(R.id.tv_inputVoltage);//输入电压

        mHandler = new MyHandler();


        initLineChart();//初始化折线图方法
        statrtMonitor();//启动服务器监听方法


        //设备地址选择保存
        sharedPreferences1 = getSharedPreferences("addressName", Context.MODE_PRIVATE);
        addressStr = sharedPreferences1.getString("address", null);


        /**下面是字体大小自适应部分,适合英文状态**/
        TextView tv_voltage_title = findViewById(R.id.tv_voltage_title);
        TextView tv_current_title = findViewById(R.id.tv_current_title);
        autoSizeText(tv_voltage_title);
        autoSizeText(tv_current_title);


        //toolbar设置
        toolbar.setTitle(R.string.app_title);//设置主标题
        toolbar.setSubtitle(R.string.app_subtitle);//设置子标题
        toolbar.setTitleTextColor(Color.WHITE);//设置主标题字体颜色
        toolbar.setSubtitleTextColor(Color.WHITE);//设置子标题字体颜色
        toolbar.setBackgroundColor(Color.BLACK);//设置toolbar背景颜色
        //让app支持actionBar
        setSupportActionBar(toolbar);

        /**导航图标**/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//菜单图标显示
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);//设置菜单图标的图片


        navigationView.setItemIconTintList(null);//设置让图标图片可视化

        //单击头部或者菜单出现信息,首先引用头部文件
        //定义一个view
        View view = navigationView.getHeaderView(0);
        tv_ip = view.findViewById(R.id.tv_ip);//IP地址显示文本框
        /** ImageView imageView=view.findViewById(R.id.iv_head_logo);
         imageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        Toast.makeText(MainActivity.this,"你选择了头部文件",Toast.LENGTH_LONG).show();
        }
        });**/

        //头部文件中IP地址显示文本框设置文字
        tv_ip.setText(getLocalIpAddress());


        //点击菜单事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //点击菜单各项时:
                switch (menuItem.getItemId()) {
                    case R.id.item_net://智能配网菜单项
                        //判断侧滑界面是否打开
                        boolean open1 = drawerLayout.isDrawerOpen(GravityCompat.START);
                        //如果打开,就关闭
                        if (open1 == true) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        Intent intent2 = new Intent(MainActivity.this, NetworkActivity.class);

                        //把ip文本框的内容赋值给String类型的message
                        String message = tv_ip.getText().toString();
                        //给message起一个名字,并传给另外一个activity
                        intent2.putExtra("ip_message", message);
                        startActivity(intent2);
                        break;
                    case R.id.item_address://地址选择
                        //弹出带编辑框的AlertDialog
                        drawerLayout.closeDrawer(GravityCompat.START);//关闭navigationView滑动出来
                        //编辑框
                        final EditText editText = new EditText(MainActivity.this);
                        //如果获取到的内容不为空,则设置文本显示
                        if (addressStr != null) {
                            editText.setText(addressStr);
                        }

                        new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.address_dialog_title))
                                .setView(editText)
                                .setPositiveButton(getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //先获取编辑框内容
                                        addressStr = editText.getText().toString();
                                        //按下确定键时,提交保存编辑框内容
                                        editor1 = sharedPreferences1.edit();
                                        editor1.putString("address", addressStr);
                                        editor1.commit();//提交修改

                                        Toast.makeText(MainActivity.this, getString(R.string.address_dialog_title) + ":" + addressStr,
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }).setNegativeButton(getString(R.string.negativeButton), null).show();


                        break;
                    case R.id.item_language://切换语言
                        Intent intent1 = new Intent(MainActivity.this, LanguageActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.item_about://点击关于页面的菜单项
                        //判断侧滑界面是否打开
                        boolean open2 = drawerLayout.isDrawerOpen(GravityCompat.START);
                        //如果打开,就关闭
                        if (open2 == true) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;


                }

                return true;
            }
        });


        /*************界面数据通信部分***********/

        sharedPreferences = MainActivity.this.getSharedPreferences("portSaveData", MODE_PRIVATE);
        portSaveData = sharedPreferences.getString("portData", "18041");
        sendDataString = sharedPreferences.getString("sendData", "");


        //点击连接发送数据
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("====点击连接if语句外面===", "socket=" + socket);
                if (socket != null) {
                    if (btn_connect.getText().toString().equals("断开")) {
                        stopTimer();//停止定时器
                        btn_connect.setText("连接");

                    } else {
                        btn_connect.setText("断开");
                        startTimerToSend();//开启定时器发送数据
                        Toast.makeText(MainActivity.this, getString(R.string.address_dialog_title) + ":" + addressStr,
                                Toast.LENGTH_SHORT).show();
                    }

                }


            }

        });


        /**暂停记录,和清除记录部分**/
        //暂停或开始按钮
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPause) {//暂停计时
                    btn_pause.setText(getString(R.string.btnTextStart));
                } else {//继续计时
                    btn_pause.setText(getString(R.string.btnTextStop));
                }
                isPause = !isPause;
            }

        });

        //清除按钮
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_writeTime.setText("00:00:00");

                if (btn_pause.getText() == getString(R.string.btnTextStop)) {
                    btn_pause.setText(getString(R.string.btnTextStart));

                    isPause = !isPause;

                }

            }
        });


        /**开机按钮键**/
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOn) {//关闭状态下
                    btn_switch.setImageResource(R.drawable.btn_on);

                } else {//打开状态下

                    btn_switch.setImageResource(R.drawable.btn_off);
                }
                isOn = !isOn;


            }
        });

        /**点击存储把数据导出到Excel表格**/
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });


    }


    /**
     * 判断是否打开wifi 并且打开的方法
     **/
    private void wifiOpen() {
        if (isWifiOpened() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("WIFI未连接,请先打开WIFI");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();
        }
    }

    /**
     * 判断手机是否打开wifi
     **/
    private boolean isWifiOpened() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    //点击菜单导航图标,让滑块显示

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    /**
     * 启动监听方法
     **/

    private void statrtMonitor() {
        if (MonitorFlage == false) {
            try {
                serverSocket = new ServerSocket(socketPort);
                MonitorConnectFlage = true;
                threadMonitorConnect.start();//监听线程开启
                mthreadMonitorConnect = threadMonitorConnect;//记下监听任务
                MonitorFlage = true;
            } catch (IOException e1) {
                // Toast.makeText(getApplicationContext(),"提示\r\n监听出错,请检查端口号",Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                for (Socket sk : arrayListSockets) {
                    try {
                        sk.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }

            } catch (Exception e) {

            }
            MonitorConnectFlage = false;
            try {
                mthreadMonitorConnect.interrupt();//只是改变中断状态,不会中断一个正在运行的线程
            } catch (Exception e) {
                e.printStackTrace();
            }
            MonitorFlage = false;
            threadSendDataFlage = false;//关闭发送的任务

            try {
                mthreadSendData.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            listClient.clear();

        }

    }

    /**
     * 监听连接线程
     **/
    private class ThreadMonitorConnect extends Thread {
        boolean mThreadMonitorConnectFlag = true;

        @Override
        public void run() {
            while (mThreadMonitorConnectFlag && MonitorConnectFlage) {
                try {
                    socket = serverSocket.accept();//等待客户端连接

                    arrayListSockets.add(socket);//添加socket
                    String mString = (socket.getInetAddress() + ":" + socket.getPort()).replace("/", " ");
                    listClient.add(mString);
                    sendHandleMsg(mHandler, "ConState", "new");

                    threadReadDataFlage = true;//接收任务
                    ThreadReadData threadReadData = new ThreadReadData(socket, mString);
                    threadReadData.start();//开启接收数据线程

                    if (threadSendDataFlage == false) {
                        threadSendDataFlage = true;//发送任务
                        ThreadSendData threadSendData = new ThreadSendData();
                        threadSendData.start();//开启发送数据线程
                        mthreadSendData = threadSendData;

                    }

                } catch (Exception e) {
                    try {
                        for (Socket sk : arrayListSockets) {
                            try {
                                sk.close();

                            } catch (Exception e1) {
                                e1.printStackTrace();

                            }

                        }

                    } catch (Exception e2) {

                    }
                    mThreadMonitorConnectFlag = false;
                    MonitorConnectFlage = false;
                    MonitorFlage = false;
                    sendHandleMsg(mHandler, "ConState", "ConError");//向Handler发送消息
                }
            }
        }
    }

    /**
     * 接收数据任务线程
     **/

    private class ThreadReadData extends Thread {
        boolean mThreadReadDataFlage = true;
        String mStringSocketMsg = "";//存储连接的信息,方便删除
        private byte[] ReadBuffer0 = new byte[1024];//数据缓存区,存储接收到的数据
        private Socket mySocket;//获取传进来的Socket

        public ThreadReadData(Socket socket, String socketMsg) {
            mySocket = socket;
            mStringSocketMsg = socketMsg;
            try {
                inputStream = mySocket.getInputStream();//获取输入流
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            sendHandleMsg(mHandler, "Toast", mStringSocketMsg + "连接");//向Handler发送消息

        }

        @Override
        public void run() {
            while (mThreadReadDataFlage && threadReadDataFlage) {
                try {
                    ReadBufferLenght = inputStream.read(ReadBuffer);//服务器断开会返回-1

                    ReadBuffer0 = new byte[ReadBufferLenght];//存储接收到的数据
                    for (int i = 0; i < ReadBufferLenght; i++) {
                        ReadBuffer0[i] = ReadBuffer[i];
                    }
                    sendHandleMsg(mHandler, "ReadData", ReadBuffer0);//向Handler发送消息

                    if (ReadBufferLenght == -1) {
                        mThreadReadDataFlage = false;
                        threadReadDataFlage = false;//关掉接收任务
                        threadSendDataFlage = false;//关掉发送任务
                        socket.close();
                        sendHandleMsg(mHandler, "ConClose", mStringSocketMsg);//向Handler发送消息
                        sendHandleMsg(mHandler, "Toast", mStringSocketMsg + "断开");//向Handler发送消息
                        try {
                            arrayListSockets.remove(mySocket);
                        } catch (Exception e) {

                        }
                    }

                } catch (Exception e) {
                    if (mThreadReadDataFlage) {
                        mThreadReadDataFlage = false;
                        threadReadDataFlage = false;//关掉接收任务
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        sendHandleMsg(mHandler, "ConClose", mStringSocketMsg);//向Handler发送消息
                        sendHandleMsg(mHandler, "Toast", mStringSocketMsg + "断开");//向Handler发送消息
                        try {
                            arrayListSockets.remove(mySocket);
                        } catch (Exception e1) {

                        }

                    }
                }
            }
        }
    }


    /**
     * 用线程实现每隔一段时间自动执行发送代码,开启定时器
     **/
    private void startTimerToSend() {
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (MonitorFlage) {
                            switch (sendFlag) {
                                case 0:
                                    sendByteArray[2] = 0x20;//命令字
                                    sendByteArray[3] = 0x01;
                                    sendDataToClient(sendByteArray);//发送到客户端数据的方法
                                    sendFlag = 1;
                                    break;
                                case 1:
                                    sendByteArray[3] = 0x00;
                                    sendByteArray[2] = 0x29;//命令字
                                    sendDataToClient(sendByteArray);//发送到客户端数据的方法
                                    sendFlag = 2;

                                    break;
                                case 2:
                                    sendByteArray[2] = 0x2A;//命令字
                                    sendDataToClient(sendByteArray);
                                    sendFlag = 3;
                                    break;
                                case 3:
                                    sendByteArray[2] = 0x2C;//命令字
                                    sendDataToClient(sendByteArray);
                                    sendFlag = 4;
                                    break;
                                case 4:
                                    sendFlag = 1;
                                    break;

                            }
                        }

                    } catch (Exception e) {
                        threadReadDataFlage = false;//关掉接收任务,预防产生多的任务
                        threadSendDataFlage = false;//关掉发送任务,预防产生多的任务
                        try {
                            mthreadSendData.interrupt();
                        } catch (Exception e2) {
                        }
                        sendDataCnt = 0;

                    }

                }
            };
            if (timer != null && timerTask != null) {
                timer.schedule(timerTask, 1000, 2000);//这里schedule也可用scheduleAtFixedRate
            }
        }
    }

    /**
     * 停止定时器
     **/
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;

        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;

        }
    }

    /**
     * 发送到客户端数据的方法
     **/
    private byte[] sendDataToClient(byte[] bytes) {

        bytes = sendByteArray;

        for (int i = 0; i < bytes.length; i++) {
            sendBuffer[i] = bytes[i];
        }
        sendDataCnt = bytes.length;
        try {
            outputStream.write(sendBuffer, 0, sendDataCnt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 存储发送的数据
        sendDataCnt = 0;//清零发送的个数
        return bytes;

    }

    /**
     * 发送数据任务线程
     **/
    class ThreadSendData extends Thread {
        private boolean mThreadFlage = true;

        @Override
        public void run() {
            while (mThreadFlage && threadSendDataFlage) {
                if (sendDataCnt > 0) {//要发送的数据个数大于0
                    sendDataFlage = false;
                    try {
                        for (Socket sk : arrayListSockets) {
                            outputStream = sk.getOutputStream();
                            sendDataFlage = true;
                            break;
                        }

                    } catch (Exception e) {
                        //发送失败
                        sendDataCnt = 0;

                    }

                }

            }
        }
    }

    /**
     * Handler
     **/
    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            /** 连接和断开**/
            String string = bundle.getString("ConState");//连接和断开
            try {
                if (string.equals("new")) {
                    btn_connect.setEnabled(true);//能点击操作
                    Log.d("===handler中====", "我是" + socket);
                } else if (string.equals("ConError")) {

                    Toast.makeText(getApplicationContext(), "连接出错,请重新启动应用", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                // TODO: handle exception
            }

            /***有连接断开了*/
            string = bundle.getString("ConClose");//有连接断开了
            if (string != null) {
                try {
                    listClient.remove(string);
                    btn_connect.setText("连接");
                    btn_connect.setEnabled(false);//不能点击操作
                    //  Toast.makeText(getApplicationContext(),string+"连接断开了",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this,MainActivity.class));
                } catch (Exception e) {

                }

            }

            /**显示消息**/
            string = bundle.getString("Toast");//显示消息
            if (string != null) {
                try {
                    Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }


            /**接收到的消息**/

            byte[] ReadByte = bundle.getByteArray("ReadData");//接收到的字节数组

            if (ReadByte != null && ReadByte.length == 20) {

                switch (ReadByte[2]) {//根据命令字判断
                    case 0x20:
                        sendFlag = 1;
                        break;
                    case 0x29:
                        //输入电压
                        int inputVoltageValue = ReadByte[3] << 8 + ReadByte[4];
                        tv_inputVoltage.setText(Integer.toString(inputVoltageValue));
                        //输出电压
                        int value = ReadByte[5] << 8 + ReadByte[6];
                        tv_voltage.setText(Integer.toString(value));
                        //输出电流
                        int currentValue = ReadByte[7] << 8 + ReadByte[8];
                        tv_current.setText(Integer.toString(currentValue));
                        //功率
                        int powerValue = ReadByte[9] << 8 + ReadByte[10];
                        tv_power.setText(Integer.toString(powerValue));
                        sendFlag = 2;
                        break;
                    case 0x2A:
                        //时间

                        //能量
                        int energyValue = ReadByte[8] << 8 + ReadByte[9] << 4 + ReadByte[10];
                        tv_energy.setText(Integer.toString(energyValue));

                        //容量
                        int capacityValue = ReadByte[11] << 8 + ReadByte[12] << 4 + ReadByte[13];
                        tv_capacity.setText(Integer.toString(capacityValue));
                        sendFlag = 3;
                        break;
                    case 0x2C:
                        //设置电压
                        int setUValue = ReadByte[7] << 8 + ReadByte[8];
                        et_setU.setText(Integer.toString(setUValue));

                        //设置电流
                        int setIValue = ReadByte[9] << 8 + ReadByte[10];
                        et_setI.setText(Integer.toString(setIValue));
                        sendFlag = 4;
                        break;
                }
            }

        }

    }


    /**
     * 向handle发送消息方法
     **/

    private void sendHandleMsg(Handler handler, String key, String Msg) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(key, Msg);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * 发送的是byte
     **/

    private void sendHandleMsg(Handler handler, String key, byte[] byt) {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putByteArray(key, byt);
        msg.setData(bundle);
        handler.sendMessage(msg);

    }

    /**
     * 初始化折线图
     **/
    private void initLineChart() {
        lineChart = findViewById(R.id.lineChart);//实例化图表

        lineChart.fitScreen();//设置自适应屏幕
        lineChart.setDrawGridBackground(false);//是否展示网格线
        lineChart.setDrawBorders(true);//设置显示边界
        lineChart.setTouchEnabled(true);//是否有触摸事件
        lineChart.setDragEnabled(true);//可拖拽
        lineChart.setScaleEnabled(true);//可缩放


        //设置折线图右下角描述,""为不添加
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        //设置折线图左下角的图例标签,false为不显示
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setFormSize(10f);//设置图例大小
        legend.setTextColor(Color.WHITE);//设置图例文字颜色
        legend.setTextSize(12f);//设置图例文字大小
        legend.setXEntrySpace(20f);//设置在水平轴上的间隙
        legend.setFormToTextSpace(10f);//图例与文字间的距离


        //获取x轴
        XAxis xAxis = lineChart.getXAxis();
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //是否设置x轴的网格线
        xAxis.setDrawGridLines(true);
        //是否设置x轴的轴线
        xAxis.setDrawAxisLine(true);
        //设置x轴的坐标字体的大小
        xAxis.setTextSize(14f);
        //设置x轴的坐标字体的颜色
        xAxis.setTextColor(Color.WHITE);
        //设置x轴的最小值
        xAxis.setAxisMinimum(0);
        //设置x轴的最大值
        xAxis.setAxisMaximum(6);
        //设置x轴的刻度数量,第二个参数表示是否评价分配
        // xAxis.setLabelCount(20,false);
        //设置x轴坐标之间的最小间隔
        xAxis.setGranularity(1f);

        //获取y轴
        YAxis leftAxis = lineChart.getAxisLeft();//左侧y轴
        //参数1:左边y轴提供的区间个数,参数2:是否均匀分布,false为均匀
        leftAxis.setLabelCount(5, false);
        //设置左边y轴的字体颜色
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMinimum(0.0f);//设置左侧y轴的最小值
        leftAxis.setAxisMaximum(1.2f);//设置左侧y轴的最大值
        //设置左边y轴数据显示格式
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                DecimalFormat df = new DecimalFormat("#.00");
                if (value < 1.0f) {
                    return "0" + df.format(value) + "V";
                }
                return "" + df.format(value) + "V";
            }
        });

        YAxis rightAxis = lineChart.getAxisRight();//右侧y轴
        rightAxis.setLabelCount(5, false);//y轴网格线
        //设置右边y轴的字体颜色
        rightAxis.setTextColor(Color.WHITE);
        //设置右边y轴数据显示格式
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                DecimalFormat df = new DecimalFormat("#.000");
                if (value < 1.0f) {
                    return "0" + df.format(value) + "A";
                }
                return "" + df.format(value) + "A";
            }
        });

        //设置y轴坐标之间的最小间隔
        //leftAxis.setGranularity(0.2f);
        rightAxis.setAxisMinimum(0.00f);//设置右侧y轴的最小值
        rightAxis.setAxisMaximum(1.20f);//设置右侧y轴的最大值


        //提供折线数据(获取到的数据)
        LineData lineData = generateDataLine(1);
        lineChart.setData(lineData);

        //刷新数据
        lineChart.invalidate();

    }


    /**
     * 图表的数据方法generateDataLine,修改数据在方法中就行
     *
     * @param cnt
     * @return
     */


    //ArrayList<Float> floats=new ArrayList<>()  //此处为存放的数值，数值为1个小数点 如21.5等等
    // float[] currentValues = {0.1f, 0.5f, 0.7f, 0.6f, 0.2f, 0.2f, 0.5f};//电流

    float[] voltageValues = {0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f};//电压

    // 折线,折线点的数据方法
    private LineData generateDataLine(int cnt) {

        //折线1
        ArrayList<Float> currentValues = new ArrayList<>();
        currentValues.add(1.0f);
        currentValues.add(0.9f);
        currentValues.add(0.7f);
        currentValues.add(0.4f);
        currentValues.add(0.6f);
        currentValues.add(0.1f);
        currentValues.add(0.2f);
        currentValues.add(0.1f);
        currentValues.add(0, 0.0f);
        currentValues.remove(currentValues.size() - 1);
        ArrayList<Entry> values1 = new ArrayList<>();
        //提供折线中的点的数据
        for (int i = 0; i < currentValues.size(); i++) {
            values1.add(new Entry(i, currentValues.get(i)));
        }

        // values1.clear();

        LineDataSet d1 = new LineDataSet(values1, this.getString(R.string.lineChart_label1));//第一条折线
        d1.setLineWidth(1.5f);//设置线的宽度
        d1.setCircleRadius(2.0f);//设置小圆圈的尺寸
        d1.setHighLightColor(Color.rgb(244, 117, 117));//设置高亮的颜色
        d1.setDrawValues(false);//是否显示小圆圈对应的数值
        d1.setDrawCircleHole(false);//设置曲线值的圆点是否是空心


        //折线2
        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            values2.add(new Entry(i, voltageValues[i]));
        }


        LineDataSet d2 = new LineDataSet(values2, this.getString(R.string.lineChart_label2));
        d2.setLineWidth(1.5f);
        d2.setCircleRadius(2.0f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);

    }

    //字体大小自适应的方法
    public void autoSizeText(TextView textView) {

        // 参数： TextView textView, int autoSizeTextType
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        // 参数：TextView textView, int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView,
                10, 18, 1, TypedValue.COMPLEX_UNIT_SP);


    }

    /**
     * 获取WIFI下ip地址
     */
    private String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }


    //时分秒显示时间格式方法
    public static String FormatMiss(int miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    //APP开启时间开始计时的方法
    public void startChronometer() {
        chronometer_bootTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                miss1++;
                chronometer.setText(FormatMiss(miss1));
            }
        });
        chronometer_bootTime.start();
    }


    //16进制字节数组转换为16进制字符串
    public static String bytyToHexstr(byte[] bytes) {

        String str_msg = "";
        for (int i = 0; i < bytes.length; i++) {
            str_msg = str_msg + String.format("%02X", bytes[i]) + "";

        }
        return str_msg;
    }

    //发送时,把获取到的字符串转换为16进制

    /**
     * 添上格式,实际上咱获取的文本框里面的都是字符串,咱需要把字符串转化为  如"33"==>0x33
     * 将已十六进制编码后的字符串src,以每两个字符分割转换为16进制形式
     * 如:"2B44EED9"--> byte[]{0x2B,0x44,0xEF0xD9}
     **/
    public static byte[] HexString2Bytes(String str) {
        StringBuilder sb = null;
        String src = null;
        if ((str.length() % 2) != 0) {//数据不是偶数
            sb = new StringBuilder(str);//构造一个StringBuilder对象
            sb.insert(str.length() - 1, "0");//在指定的位置1,插入指定的字符串
            src = sb.toString();

        } else {
            src = str;
        }
        //  Log.e("error","str.length"+str.length());
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);

        }
        return ret;

    }

    //将两个ASCII字符合成一个字节;如:"EF"-->0xEF.Byte.decode()将String解码为 Byte
    public static byte uniteBytes(byte src0, byte src1) {

        try {
            byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();//.byteValue()转换为byte类型的数
            // 该方法的作用是以byte类型返回该 Integer 的值。只取低八位的值，高位不要。
            _b0 = (byte) (_b0 << 4);//左移4位
            byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
            byte ret = (byte) (_b0 ^ _b1);//按位异或运算符(^)是二元运算符，要化为二进制才能进行计算
            return ret;

        } catch (Exception e) {
            //TODO:handle exception
        }

        return 0;
    }

    /**
     * CRC检验值
     *
     * @param modbusdata
     * @param length
     * @return CRC检验值
     */
    protected int crc16_modbus(byte[] modbusdata, int length) {
        int i = 0, j = 0;
        int crc = 0xffff;//有的用0,有的用0xff
        try {
            for (i = 0; i < length; i++) {
                //注意这里要&0xff,因为byte是-128~127,&0xff 就是0x0000 0000 0000 0000  0000 0000 1111 1111
                crc ^= (modbusdata[i] & (0xff));
                for (j = 0; j < 8; j++) {
                    if ((crc & 0x01) == 1) {
                        crc = (crc >> 1);
                        crc = crc ^ 0xa001;
                    } else {
                        crc >>= 1;
                    }
                }
            }
        } catch (Exception e) {

        }

        return crc;
    }

    /**
     * CRC检验值
     *
     * @param modbusdata
     * @param length
     * @return CRC检验值
     */
    protected byte CheckSum(byte[] modbusdata, int length) {
        int i = 0, j = 0;
        byte sum = 0x00;//有的用0,有的用0xff
        try {
            for (i = 0; i < length; i++) {
                //注意这里要&0xff,因为byte是-128~127,&0xff 就是0x0000 0000 0000 0000  0000 0000 1111 1111
                sum += modbusdata[i];
            }
        } catch (Exception e) {

        }

        return sum;
    }

    /**
     * CRC校验正确标志
     *
     * @param modbusdata
     * @param length
     * @return 0-failed 1-success
     */
    protected int crc16_flage(byte[] modbusdata, int length) {
        int Receive_CRC = 0, calculation = 0;//接收到的CRC,计算的CRC

        Receive_CRC = crc16_modbus(modbusdata, length);
        calculation = modbusdata[length];
        calculation <<= 8;
        calculation += modbusdata[length + 1];
        if (calculation != Receive_CRC) {
            return 0;
        }
        return 1;
    }


    /**
     * CRC校验正确标志
     *
     * @param modbusdata
     * @param length
     * @return 0-failed 1-success
     */
    protected int crc42_flage(byte[] modbusdata, int length) {
        int Receive_CRC = 0, calculation = 0;//接收到的CRC,计算的CRC

        Receive_CRC = crc16_modbus(modbusdata, length);
        calculation = modbusdata[length];
        calculation <<= 8;
        calculation += modbusdata[length + 1];
        if (calculation != Receive_CRC) {
            return 0;
        }
        return 1;
    }

    //返回键
    @Override
    public void onBackPressed() {
        //判断侧滑界面是否打开
        boolean open = drawerLayout.isDrawerOpen(GravityCompat.START);
        //如果打开,就关闭
        if (open == true) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        //连点两次关闭app
        if ((System.currentTimeMillis() - eixtTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            eixtTime = System.currentTimeMillis();
        } else {
            //彻底关闭整个app
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);

        }
    }


    /**==============对表格的导出操作================**/


    /**
     * 导出表格的操作
     * 新的运行时权限机制"只在应用程序的targetSdkVersion>=23时生效，并且只在6.0系统之上有这种机制，
     * 在低于6.0的系统上应用程序和以前一样不受影响。
     * 当前应用程序的targetSdkVersion小于23（为22），系统会默认其尚未适配新的运行时权限机制，
     * 安装后将和以前一样不受影响：即用户在安装应用程序的时候默认允许所有被申明的权限
     **/
    private void export() {
        if (this.getApplicationInfo().targetSdkVersion >= 23 && Build.VERSION.SDK_INT >= 23) {
            requestPermission();

        } else {
            writeExcel();
        }
    }

    /**
     * 动态请求读写权限
     **/
    private void requestPermission() {
        if (!checkPermission()) {//如果没有权限则请求权限再写
            ActivityCompat.requestPermissions(this, pess, 100);
        } else {//如果有权限则直接写
            writeExcel();

        }
    }

    /**
     * 检测权限
     **/
    private boolean checkPermission() {
        for (String permission : pess) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //只要有一个权限没有被授予,则直接返回false
                return false;

            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            boolean isAllGranted = true;
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;

                }

            }
            if (isAllGranted) {//请求到权限了,写Excel
                writeExcel();
            } else {//权限被拒绝,不能写
                Toast.makeText(this, "读写手机存储权限被禁止,请在权限管理中心手动打开权限",
                        Toast.LENGTH_LONG).show();

            }

        }
    }

    /**
     * 将数据写入Excel表格
     **/
    private void writeExcel() {
        if (getExternalStoragePath() == null) {
            return;
        }
        //SD卡指定文件夹
        excelFilePath = getExternalStoragePath() + "/Excel/mine.xls";//Excel是子文件夹,mine.xls是表格文件
        // excelFilePath=getExternalFilesDir("Excel")+"mine.xls";
        if (checkFile(excelFilePath)) {
            deleteByPath(excelFilePath);//如果文件存在则先删除原有的文件
            //  Toast.makeText(this,"删除了",Toast.LENGTH_LONG).show();

        }
        File file = new File(getExternalStoragePath() + "/Excel");

        makeDir(file);
        ExcelUtils.initExcel(excelFilePath, "电压电流数据表格", colNames);//需要写入权限
        ExcelUtils.writeObjListToExcel(getTraveData(), excelFilePath, this);//把数据写入表格getTraveData()数据方法
    }

    /**
     * 根据路径生成文件夹
     **/

    public static void makeDir(File filePath) {
        if (!filePath.getParentFile().exists()) {
            makeDir(filePath.getParentFile());
        }
        filePath.mkdir();
    }

    /**
     * 获取外部存储路径
     **/
    public String getExternalStoragePath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = getExternalFilesDir(null);
            return sdDir.toString();

        } else {
            Toast.makeText(this, "找不到外部存储路径,读写手机存储权限被禁止,请在权限管理中心手动打开权限",
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * 测试数据
     **/
    public ArrayList<ArrayList<String>> getTraveData() {
        // String s="测试string";//这里是数据内容
        int value = 22;
        ArrayList<ArrayList<String>> datas = new ArrayList<>();
        ArrayList<String> data = null;
        for (int i = 0; i < 8; i++) {//列
            data = new ArrayList<>();
            data.clear();
            for (int j = 0; j < 6; j++) {//行
                // data.add(s+j);
                data.add(String.valueOf(value) + j);

            }
            datas.add(data);

        }
        return datas;

    }

    /**
     * 根据文件路径检查文件是否存在,需要读取权限
     * filePath 文件路径
     * true 存在
     **/
    private boolean checkFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * 根据文件路径删除文件
     * filePath
     **/
    private void deleteByPath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();

            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        tv_ip.setText(getLocalIpAddress());


    }

    //停止计时器,handler
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(runnable);
        super.onDestroy();
    }

}
