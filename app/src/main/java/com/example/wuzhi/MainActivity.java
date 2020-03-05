package com.example.wuzhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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

import com.example.wuzhi.Utils.LocaleUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String tag = "=======err";

    private long eixtTime = 0;//存在时间


    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    TextView tv_connect, tv_save;//toolbar上的连接和存储


    TextView tv_ip;//IP地址显示文本框

    LineChart lineChart;//折线表,存线集合
    int[] Vout = new int[100];//假设的电压数组


    Chronometer chronometer_bootTime;//开机时间
    Chronometer chronometer_writeTime;//记录时间
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

    final Timer timer = new Timer();
    boolean ConnectFlage = true;//连接标志
    Socket socket;//创建socket对象
    InputStream inputStream;//获取输入流
    OutputStream outputStream;//获得输出流
    ThreadConnectService threadConnectService = new ThreadConnectService();//建立一个连接任务的对象
    ThreadReadData threadReadData = new ThreadReadData();//接收数据的任务
    boolean threadReadDataFlage = false;//接收数据任务一直运行控制
    boolean threadSendDataFlage = false;//发送数据任务一直运行控制
    byte[] ReadBuffer = new byte[2048];//存储接收到的数据
    byte[] SendBuffer = new byte[2048];//存储发送的数据
    int ReadBufferLength = 0;//接收到数据的长度
    int SendDataCnt = 0;//控制发送数据的个数

    Thread mthreadConnectSerice;//记录连接任务
    Thread mthreadSendData;//记录发送任务
    Thread mthreadReadData;//记录接收任务


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav);

        tv_connect = findViewById(R.id.tv_connect);//连接
        tv_save = findViewById(R.id.tv_save);//存储


        chronometer_bootTime = findViewById(R.id.chronometer_bootTime);
        chronometer_writeTime = findViewById(R.id.chronometer_writeTime);
        startChronometer();//开机计时
        writeChronometer();//记录时间计时
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


        /**界面数据通信部分**/

        //连接按钮的点击事件
        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectFlage) {//如果连接成功
                    try {
                        threadConnectService.start();//启动连接服务器的线程任务
                        mthreadConnectSerice = threadConnectService;
                        Toast.makeText(getApplicationContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();

                        autoSendData();//自动发送数据


                    } catch (Exception e) {

                    }
                } else {
                    ConnectFlage = true;
                    threadSendDataFlage = false;//关掉发送任务,预防产生多的任务
                    SendDataCnt = 0;
                    threadReadDataFlage = false;//关掉接收任务,预防产生多的任务
                    Toast.makeText(getApplicationContext(), "请连接服务器", Toast.LENGTH_SHORT).show();

                    try {
                        mthreadConnectSerice.interrupt();
                    } catch (Exception e) {
                    }
                    try {
                        mthreadSendData.interrupt();
                    } catch (Exception e2) {
                    }
                    try {
                        mthreadReadData.interrupt();
                    } catch (Exception e2) {
                    }
                    try {
                        socket.close();//关闭socket
                        inputStream.close();//关闭数据流
                    } catch (Exception e) {
                        // TODO: handle exception
                    }


                }

            }


        });


        /**暂停记录,和清除记录部分**/
        //暂停或开始按钮
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer_writeTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        miss2++;
                        chronometer.setText(FormatMiss(miss2));
                    }
                });

                chronometer_writeTime.start();

                if (!isPause) {//暂停计时
                    btn_pause.setText(getString(R.string.btnTextStart));
                    chronometer_writeTime.stop();
                } else {//继续计时
                    btn_pause.setText(getString(R.string.btnTextStop));
                    chronometer_writeTime.start();
                }
                isPause = !isPause;
            }

        });

        //清除按钮
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer_writeTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        miss2 = 0;
                        chronometer.setText(FormatMiss(miss2));
                    }
                });

                chronometer_writeTime.stop();
                chronometer_writeTime.setBase(SystemClock.elapsedRealtime());

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


        /**点击存储的点击事件**/
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了存储", Toast.LENGTH_LONG).show();
            }
        });


        /**下面是字体大小自适应部分**/
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
                        Intent intent2 = new Intent(MainActivity.this, NetworkActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.item_address://地址选择
                        //弹出带编辑框的AlertDialog
                        drawerLayout.closeDrawer(GravityCompat.START);//关闭navigationView滑动出来

                        final EditText editText = new EditText(MainActivity.this);
                        new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.address_dialog_title))
                                .setView(editText)
                                .setPositiveButton(getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //按下确定键
                                        Toast.makeText(MainActivity.this, getString(R.string.address_dialog_title) + ":" + editText
                                                        .getText().toString(),
                                                Toast.LENGTH_SHORT).show();
                                        // restartAct();

                                    }
                                }).setNegativeButton(getString(R.string.negativeButton), null).show();

                        break;
                    case R.id.item_language://切换语言
                        Intent intent1 = new Intent(MainActivity.this, LanguageActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.item_about://点击关于页面的菜单项
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;

                }

                return true;
            }
        });


        /**下面是图表部分**/
        lineChart = findViewById(R.id.lineChart);//实例化图表
        lineChart.fitScreen();//设置自适应屏幕

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

        //设置显示边界
        lineChart.setDrawBorders(true);

        //是否绘制网格背景
        lineChart.setDrawGridBackground(false);

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
        xAxis.setAxisMinimum(0f);
        //设置x轴的最大值
        xAxis.setAxisMaximum(5f);

        //获取y轴
        YAxis leftAxis = lineChart.getAxisLeft();//左侧y轴
        //参数1:左边y轴提供的区间个数,参数2:是否均匀分布,false为均匀
        leftAxis.setLabelCount(5, false);
        //设置左边y轴的字体颜色
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = lineChart.getAxisRight();//右侧y轴
        rightAxis.setLabelCount(5, false);//y轴网格线
        //设置右边y轴的字体颜色
        rightAxis.setTextColor(Color.WHITE);

        leftAxis.setAxisMinimum(0.00f);//设置左侧y轴的最小值
        leftAxis.setAxisMaximum(2.00f);//设置左侧y轴的最大值
        rightAxis.setAxisMinimum(0.000f);//设置右侧y轴的最小值
        rightAxis.setAxisMaximum(2.000f);//设置右侧y轴的最大值

        //提供折线数据(获取到的数据)
        LineData lineData = generateDataLine(1);
        lineChart.setData(lineData);

        //刷新数据
        lineChart.invalidate();


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
     * 连接服务器的任务线程
     **/
    class ThreadConnectService extends Thread {

        @Override
        public void run() {


            try {

                String ip = "192.168.1.115";
                int port = 9999;//端口号

                socket = new Socket(ip, port);//创建连接地址和端口号

                ConnectFlage = false;//是控制连接按钮显示的
                sendHandleMsg(mHandler, "ConState", "ConOK");//向Handle发送连接成功的消息

                inputStream = socket.getInputStream();//获得通道的数据流
                outputStream = socket.getOutputStream();//获得通道的输出流
                threadReadDataFlage = true;//一直接受数据
                threadSendDataFlage = true;//一直循环的判断是否发送数据


                threadReadData = new ThreadReadData();
                threadReadData.start();//接收数据线程开启
                mthreadReadData = threadReadData;


            } catch (Exception e) {
                Log.e(tag, e.toString());

            }
        }

    }

    /**
     * 用线程实现每隔一段时间自动执行发送代码
     **/
    private void autoSendData() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                try {

                    //发送第一条数据 输入电压
                    String SendVoltageStr = "AA 00 20 01 40 5D C0 00 00 00 00 00 00 00 00 00 00 00 00 28";
                    byte[] SendBuffer0 = HexString2Bytes(SendVoltageStr.replace(" ", ""));//16进制发送
                    for (int i = 0; i < SendBuffer0.length; i++) {
                        SendBuffer[i] = SendBuffer0[i];

                    }
                    SendDataCnt = SendBuffer0.length;
                    outputStream.write(SendBuffer, 0, SendDataCnt);//写数据,发送数据
                    SendDataCnt = 0;//清零发送的个数

                    //发送第二条数据 输出电流
                    String SendCurrentStr = "AA 00 21 01 40 5D C0 00 00 00 00 00 00 00 00 00 00 00 00 28";
                    byte[] SendBuffer1 = HexString2Bytes(SendCurrentStr.replace(" ", ""));//16进制发送
                    for (int i = 0; i < SendBuffer1.length; i++) {
                        SendBuffer[i] = SendBuffer1[i];
                    }
                    SendDataCnt = SendBuffer1.length;
                    outputStream.write(SendBuffer, 0, SendDataCnt);//byte[] SendBuffer=new byte[2048];
                    // 存储发送的数据
                    SendDataCnt = 0;//清零发送的个数


                    //发送第三条数据 输出功率
                    String SendPowerStr = "AA 00 22 01 40 5D C0 00 00 00 00 00 00 00 00 00 00 00 00 28";
                    byte[] SendBuffer2 = HexString2Bytes(SendPowerStr.replace(" ", ""));//16进制发送
                    for (int i = 0; i < SendBuffer2.length; i++) {
                        SendBuffer[i] = SendBuffer2[i];
                    }
                    SendDataCnt = SendBuffer2.length;
                    outputStream.write(SendBuffer, 0, SendDataCnt);//byte[] SendBuffer=new byte[2048];
                    // 存储发送的数据
                    SendDataCnt = 0;//清零发送的个数

                    //发送第四条数据 U_SET
                    String SendSetUStr = "AA 00 23 01 40 5D C0 00 00 00 00 00 00 00 00 00 00 00 00 28";
                    byte[] SendBuffer3 = HexString2Bytes(SendSetUStr.replace(" ", ""));//16进制发送
                    for (int i = 0; i < SendBuffer3.length; i++) {
                        SendBuffer[i] = SendBuffer3[i];
                    }
                    SendDataCnt = SendBuffer3.length;
                    outputStream.write(SendBuffer, 0, SendDataCnt);//byte[] SendBuffer=new byte[2048];
                    // 存储发送的数据
                    SendDataCnt = 0;//清零发送的个数


                } catch (Exception e) {
                    sendHandleMsg(mHandler, "ConState", "ConNo");//向Handle发送消息
                    threadReadDataFlage = false;//关掉接收任务,预防产生多的任务
                    threadSendDataFlage = false;//关掉发送任务,预防产生多的任务
                    try {
                        mthreadSendData.interrupt();
                    } catch (Exception e2) {
                    }
                    SendDataCnt = 0;


                }
            }

        }, 1000, 2000);
    }

    /**
     * 接收数据的任务
     **/
    class ThreadReadData extends Thread {

        boolean mThreadReadDataFlage = true;

        @Override
        public void run() {
            while (mThreadReadDataFlage && threadReadDataFlage) {
                try {
                    ReadBufferLength = inputStream.read(ReadBuffer);//服务器断开会返回-1,ReadBufferLenght是读取数据的长度
                    byte[] ReadBuffer0 = new byte[ReadBufferLength];//存储接收到的数据
                    for (int i = 0; i < ReadBufferLength; i++) {
                        ReadBuffer0[i] = ReadBuffer[i];
                    }

                    sendHandleMsg(mHandler, "ReadData", ReadBuffer0);//向Handle发送消息


                    if (ReadBufferLength == -1) {
                        mThreadReadDataFlage = false;
                        threadReadDataFlage = false;//关掉接收任务,预防产生多的任务
                        ConnectFlage = true;
                        threadSendDataFlage = false;//关掉发送任务,预防产生多的任务
                        SendDataCnt = 0;

                        sendHandleMsg(mHandler, "ConState", "ConNO");//向Handle发送消息

                    }


                } catch (Exception e) {
                    mThreadReadDataFlage = false;
                    threadReadDataFlage = false;//关掉接收任务,预防产生多的任务
                    try {
                        mthreadReadData.interrupt();
                    } catch (Exception e2) {
                    }
                    SendDataCnt = 0;

                }

            }

        }
    }

    /**
     * Handler
     **/
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String string = bundle.getString("ConState");
            try {
                if (string.equals("ConOK")) {
                    Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                } else if (string.equals("ConNO")) {
                    Toast.makeText(getApplicationContext(), "与服务器断开连接", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
            }


            byte[] ReadByte = bundle.getByteArray("ReadData");//接收到的字节数组

            if (ReadByte != null) {
                if ((ReadByte[0] == 0xAA) && (ReadByte.length == 20)) {

                    switch (ReadByte[2]) {//根据命令字判断
                        case 29:
                            int value = ReadByte[3] << 8 + ReadByte[4];
                            tv_voltage.setText(Integer.toString(value));
                            break;
                        case 42:
                            int value1 = ReadByte[3] << 8 + ReadByte[4];
                            tv_current.setText(Integer.toString(value1));

                            break;
                    }


                }

                //tv_voltage.setText( bytyToHexstr(ReadByte));
            }
        }
    };


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
     * 数据方法generateDataLine,修改数据在方法中就行
     *
     * @param cnt
     * @return
     */
    // 折线,折线点的数据方法
    private LineData generateDataLine(int cnt) {

        //折线1
        ArrayList<Entry> values1 = new ArrayList<>();
        //提供折线中的点的数据
        for (int i = 99; i > 0; i--) {
            Vout[i] = Vout[i - 1];
        }
        Vout[0] = 5;//接受电压值
        Vout[0] = 5;//接受电压值

        values1.clear();
        for (int i = 0; i < 10; i++) {
            /**真正的数据也是封装在Entry里,修改(int) (Math.random() * 65) + 40这部分**/
            values1.add(new Entry(i, (Vout[i])));
        }


        LineDataSet d1 = new LineDataSet(values1, this.getString(R.string.lineChart_label1));//第一条折线
        d1.setLineWidth(1.5f);//设置线的宽度
        d1.setCircleRadius(2.0f);//设置小圆圈的尺寸
        d1.setHighLightColor(Color.rgb(244, 117, 117));//设置高亮的颜色
        d1.setDrawValues(false);//是否显示小圆圈对应的数值
        d1.setDrawCircleHole(false);//设置曲线值的圆点是否是空心


        //折线2
        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            /**真正的数据也是封装在Entry里,修改(int) (Math.random() * 65) + 40这部分**/
            values2.add(new Entry(i, values1.get(i).getY() - 3));
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

    //记录时间计时的方法
    public void writeChronometer() {
        chronometer_writeTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                miss2++;
                chronometer.setText(FormatMiss(miss2));
            }
        });
        chronometer_writeTime.start();
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
/**
    @Override
    public void onBackPressed() {
        //判断侧滑界面是否打开
        boolean open = drawerLayout.isDrawerOpen(GravityCompat.START);
        //如果打开,就关闭
        if (open = true) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        //连点两次关闭app
        if ((System.currentTimeMillis() - eixtTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            eixtTime = System.currentTimeMillis();
        } else {
            //彻底关闭整个app
            int currentVersion= Build.VERSION.SDK_INT;
            if(currentVersion> Build.VERSION_CODES.ECLAIR_MR1){
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);
            }else {
                ActivityManager am= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.restartPackage(getPackageName());

            }


        }


    }
    **/


      @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //判断侧滑界面是否打开
        boolean open = drawerLayout.isDrawerOpen(GravityCompat.START);
        //如果打开,就关闭
        if (open = true) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - eixtTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                eixtTime = System.currentTimeMillis();
            } else {
                //彻底关闭整个app
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
