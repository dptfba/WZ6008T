package com.example.wuzhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    TextView tv_connect, tv_save;//toolbar上的连接和存储


    TextView tv_ip;//IP地址显示文本框

    LineChart lineChart;//折线表,存线集合
    int[] Vout=new int[100];//假设的电压数组



    Chronometer chronometer_bootTime;//开机时间
    Chronometer chronometer_writeTime;//记录时间
    int miss1=0;
    int miss2=0;
    private boolean isPause=false;//用于判断是否为暂停状态
    Button btn_pause;//暂停按钮
    Button btn_clear;//清除按钮


    ImageButton btn_switch;
    private boolean isOn=false;//用于判断是否是开机状态

    ServerSocket serverSocket;//创建ServerSocket对象
    Socket clicksSocket;//连接通道,创建Socke对象
    InputStream inputStream;//创建输入数据流
    OutputStream outputStream;//创建输出数据流
    byte[] TcpSendData=new byte[1024];//用来缓存发送的数据
    int TcpSendDataLen=0;//发送的数据个数





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav);

        tv_connect = findViewById(R.id.tv_connect);//连接
        tv_save = findViewById(R.id.tv_save);//存储


        chronometer_bootTime=findViewById(R.id.chronometer_bootTime);
        chronometer_writeTime=findViewById(R.id.chronometer_writeTime);
        startChronometer();//开机计时
        writeChronometer();//记录时间计时
        btn_pause = findViewById(R.id.btn_startOrPause);//暂停或开始按钮
        btn_clear = findViewById(R.id.btn_clear);//暂停按钮

        btn_switch=findViewById(R.id.btn_switch);//开关按钮

        //假设的对电压的for循环
        for (int i = 0; i <100; i++)
        {
            Vout[i] =0;
        }


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

                if(!isPause){//暂停计时
                    btn_pause.setText(getString(R.string.btnTextStart));
                    chronometer_writeTime.stop();
                }else {//继续计时
                    btn_pause.setText(getString(R.string.btnTextStop));
                    chronometer_writeTime.start();
                }
                isPause=!isPause;
            }

        });

        //清除按钮
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer_writeTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        miss2=0;
                        chronometer.setText(FormatMiss(miss2));
                    }
                });

                chronometer_writeTime.stop();
                chronometer_writeTime.setBase(SystemClock.elapsedRealtime());

                if(btn_pause.getText()==getString(R.string.btnTextStop)){
                    btn_pause.setText(getString(R.string.btnTextStart));

                    isPause=!isPause;

                }

            }
        });


        /**开机按钮键**/
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOn){//关闭状态下
                    btn_switch.setImageResource(R.drawable.btn_on);

                }else {//打开状态下

                    btn_switch.setImageResource(R.drawable.btn_off);
                }
                isOn=!isOn;


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

        /**点击连接和存储的点击事件**/
        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了连接", Toast.LENGTH_LONG).show();
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了存储", Toast.LENGTH_LONG).show();
            }
        });


        navigationView.setItemIconTintList(null);//设置让图标图片可视化

        //单击头部或者菜单出现信息,首先引用头部文件
        //定义一个view
        View view=navigationView.getHeaderView(0);
        tv_ip=view.findViewById(R.id.tv_ip);//IP地址显示文本框
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
                        Intent intent2 = new Intent(MainActivity.this,NetworkActivity.class);
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
                                        Toast.makeText(MainActivity.this, getString(R.string.address_dialog_title)+":"+editText
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
            Vout[i] = Vout[i-1];
        }
        Vout[0] = 5;//接受电压值

        values1.clear();
        for (int i = 0; i < 100; i++) {
            /**真正的数据也是封装在Entry里,修改(int) (Math.random() * 65) + 40这部分**/
            values1.add(new Entry(i,  (Vout[i])));
        }



        LineDataSet d1 = new LineDataSet(values1, this.getString(R.string.lineChart_label1));//第一条折线
        d1.setLineWidth(1.5f);//设置线的宽度
        d1.setCircleRadius(2.0f);//设置小圆圈的尺寸
        d1.setHighLightColor(Color.rgb(244, 117, 117));//设置高亮的颜色
        d1.setDrawValues(false);//是否显示小圆圈对应的数值
        d1.setDrawCircleHole(false);//设置曲线值的圆点是否是空心


        //折线2
        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 99; i > 0; i--) {
            /**真正的数据也是封装在Entry里,修改(int) (Math.random() * 65) + 40这部分**/
            Vout[i] = Vout[i-1];
        }
        values1.clear();
        for (int i = 0; i < 6; i++) {
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
     *
     * 获取WIFI下ip地址
     */
    private String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }


    //时分秒显示时间格式方法
    public static String FormatMiss(int miss){
        String hh=miss/3600>9?miss/3600+"":"0"+miss/3600;
        String mm=(miss%3600)/60>9?(miss%3600)/60+"":"0"+(miss%3600)/60;
        String ss=(miss%3600)%60>9?(miss%3600)%60+"":"0"+(miss%3600)%60;
        return hh+":"+mm+":"+ss;
    }

    //APP开启时间开始计时的方法
    public void startChronometer(){
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
    public void writeChronometer(){
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
    public static String bytyToHexstr(byte[] bytes){

        String str_msg="";
        for(int i=0;i<bytes.length;i++){
            str_msg=str_msg+String.format("%02X",bytes[i])+"";

        }
        return str_msg;
    }

    //发送时,把获取到的字符串转换为16进制
    /**
     * 添上格式,实际上咱获取的文本框里面的都是字符串,咱需要把字符串转化为  如"33"==>0x33
     * 将已十六进制编码后的字符串src,以每两个字符分割转换为16进制形式
     * 如:"2B44EED9"--> byte[]{0x2B,0x44,0xEF0xD9}
     *
     * **/
    public static byte[] HexString2Bytes(String str){
        StringBuilder sb=null;
        String src=null;
        if((str.length()%2)!=0){//数据不是偶数
            sb=new StringBuilder(str);//构造一个StringBuilder对象
            sb.insert(str.length()-1,"0");//在指定的位置1,插入指定的字符串
            src=sb.toString();

        }else {
            src=str;
        }
      //  Log.e("error","str.length"+str.length());
        byte[] ret=new byte[src.length()/2];
        byte[] tmp=src.getBytes();
        for(int i=0;i<tmp.length/2;i++){
            ret[i]=uniteBytes(tmp[i*2],tmp[i*2+1]);

        }
        return ret;

    }

    //将两个ASCII字符合成一个字节;如:"EF"-->0xEF.Byte.decode()将String解码为 Byte
    public static byte uniteBytes(byte src0,byte src1){

        try{
            byte _b0=Byte.decode("0x"+new String(new byte[]{src0})).byteValue();//.byteValue()转换为byte类型的数
            // 该方法的作用是以byte类型返回该 Integer 的值。只取低八位的值，高位不要。
            _b0= (byte) (_b0<<4);//左移4位
            byte _b1=Byte.decode("0x"+new String(new  byte[]{src1})).byteValue();
            byte ret= (byte) (_b0^_b1);//按位异或运算符(^)是二元运算符，要化为二进制才能进行计算
            return ret;

        }catch (Exception e){
            //TODO:handle exception
        }

        return 0;
    }

    /**
     * CRC检验值
     * @param modbusdata
     * @param length
     * @return CRC检验值
     */
    protected int crc16_modbus(byte[] modbusdata, int length)
    {
        int i=0, j=0;
        int crc = 0xffff;//有的用0,有的用0xff
        try
        {
            for (i = 0; i < length; i++)
            {
                //注意这里要&0xff,因为byte是-128~127,&0xff 就是0x0000 0000 0000 0000  0000 0000 1111 1111
                crc ^= (modbusdata[i]&(0xff));
                for (j = 0; j < 8; j++)
                {
                    if ((crc & 0x01) == 1)
                    {
                        crc = (crc >> 1) ;
                        crc = crc ^ 0xa001;
                    }
                    else
                    {
                        crc >>= 1;
                    }
                }
            }
        }
        catch (Exception e)
        {

        }

        return crc;
    }

    /**
     * CRC校验正确标志
     * @param modbusdata
     * @param length
     * @return 0-failed 1-success
     */
    protected int crc16_flage(byte[] modbusdata, int length)
    {
        int Receive_CRC = 0, calculation = 0;//接收到的CRC,计算的CRC

        Receive_CRC = crc16_modbus(modbusdata, length);
        calculation = modbusdata[length];
        calculation <<= 8;
        calculation += modbusdata[length+1];
        if (calculation != Receive_CRC)
        {
            return 0;
        }
        return 1;
    }



    /**
     * CRC校验正确标志
     * @param modbusdata
     * @param length
     * @return 0-failed 1-success
     */
    protected int crc32_flage(byte[] modbusdata, int length)
    {
        int Receive_CRC = 0, calculation = 0;//接收到的CRC,计算的CRC

        Receive_CRC = crc16_modbus(modbusdata, length);
        calculation = modbusdata[length];
        calculation <<= 8;
        calculation += modbusdata[length+1];
        if (calculation != Receive_CRC)
        {
            return 0;
        }
        return 1;
    }

}
//范玉鹏更新 ces suug  ss