package com.example.wuzhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuzhi.Esptouch.ByteUtil;
import com.example.wuzhi.Esptouch.EsptouchTask;
import com.example.wuzhi.Esptouch.IEsptouchListener;
import com.example.wuzhi.Esptouch.IEsptouchResult;
import com.example.wuzhi.Esptouch.IEsptouchTask;
import com.example.wuzhi.Esptouch.LocationManagerCompat;
import com.example.wuzhi.Esptouch.TouchNetUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NetworkActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = NetworkActivity.class.getSimpleName();
    MainActivity mainActivity;

    private static final int REQUEST_PERMISSION = 0x01;


    private TextView mApSsidTV;//显示WiFi名称的文本框
    private TextView mApBssidTV;//显示Mac地址的文本框
    private EditText mApPasswordET;//WiFi密码编辑框
    private EditText mDeviceCountET;//设备数量编辑框
    private RadioGroup mPackageModeGroup;//单选按钮组
    private TextView mMessageTV;//提示信息文本框
    private Button mConfirmBtn;//确定按钮

    private EsptouchAsyncTask4 mTask;//异步任务加载数据类

    private boolean mReceiverRegistered = false;//是否接收注册

    //广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            assert wifiManager != null;// assert 断言

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(wifiManager.getConnectionInfo());
                    break;


            }
        }
    };
    private boolean mDestroyed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mainActivity=new MainActivity();

        mApSsidTV = findViewById(R.id.ap_ssid_text);//wifi名称文本框
        mApBssidTV = findViewById(R.id.ap_bssid_text);//Mac地址文本框
        mApPasswordET = findViewById(R.id.ap_password_edit);//密码编辑框
        mDeviceCountET = findViewById(R.id.device_count_edit);//设备数量编辑框
        mDeviceCountET.setText("1");
        mPackageModeGroup = findViewById(R.id.package_mode_group);//单选按钮组
        mMessageTV = findViewById(R.id.message);//信息文本框
        mConfirmBtn = findViewById(R.id.confirm_btn);//确定按钮
        mConfirmBtn.setEnabled(false);
        mConfirmBtn.setOnClickListener(this);


        //判断父Activity是否为空,不为空设置导航图表显示
        if (NavUtils.getParentActivityName(NetworkActivity.this) != null) {
            //显示向左的箭头图标
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        //如果SDK大于等于28
        if (isSDKAtLeastP()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_PERMISSION);

            } else {
                registerBroadcastReceiver();
            }

        } else {
            registerBroadcastReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!mDestroyed) {
                    registerBroadcastReceiver();
                }

            }
            return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDestroyed = true;
        if (mReceiverRegistered) {
            unregisterReceiver(mReceiver);
        }
    }

    //判断SDK是不是大于28
    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    //注册广播
    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        registerReceiver(mReceiver, filter);
        mReceiverRegistered = true;
    }

    //onWifiChanged()方法
    private void onWifiChanged(WifiInfo info) {
        boolean disconnected = info == null
                || info.getNetworkId() == -1
                || "<unknown ssid>".equals(info.getSSID());
        if (disconnected) {
            mApSsidTV.setText("");
            mApSsidTV.setTag(null);
            mApBssidTV.setTag("");
            mMessageTV.setText(R.string.no_wifi_connection);
            mConfirmBtn.setEnabled(false);

            if (isSDKAtLeastP()) {
                checkLocation();
            }
            if (mTask != null) {
                mTask.cancelEsptouch();
                mTask = null;
                new AlertDialog.Builder(NetworkActivity.this)
                        .setMessage(R.string.configure_wifi_change_message)
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();

            }

        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            mApSsidTV.setText(ssid);
            mApSsidTV.setTag(ByteUtil.getBytesByString(ssid));
            byte[] ssidOriginalData = TouchNetUtil.getOriginalSsidBytes(info);
            mApSsidTV.setTag(ssidOriginalData);

            String bssid = info.getBSSID();
            mApBssidTV.setText(bssid);

            mConfirmBtn.setEnabled(true);
            mMessageTV.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequency = info.getFrequency();
                if (frequency > 4900 && frequency < 5900) {
                    // Connected 5G wifi. Device does not support 5G连接5G wifi。设备不支持5G
                    mMessageTV.setText(R.string.wifi_5g_message);

                }

            }
        }

    }

    private void checkLocation() {
        boolean enable;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enable = locationManager != null && LocationManagerCompat.isLocationEnabled(locationManager);
        if (!enable) {
            mMessageTV.setText(R.string.location_disable_message);

        }
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmBtn) {
            byte[] ssid = mApSsidTV.getTag() == null ? ByteUtil.getBytesByString(mApSsidTV.getText().toString())
                    : (byte[]) mApSsidTV.getTag();
            byte[] password = ByteUtil.getBytesByString(mApPasswordET.getText().toString());
            byte[] bssid = TouchNetUtil.parseBssid2bytes(mApBssidTV.getText().toString());
            byte[] deviceCount = mDeviceCountET.getText().toString().getBytes();
            byte[] broadcast = {(byte) (mPackageModeGroup.getCheckedRadioButtonId() == R.id.package_broadcast
                    ? 1 : 0)};
            //byte[] ip = ByteUtil.getBytesByString( mainActivity.tv_ip.getText().toString());
            byte[] ip = ByteUtil.getBytesByString(" 192.168.1.1");
            //byte[] ip =

            if (mTask != null) {
                mTask.cancelEsptouch();
            }
            mTask = new EsptouchAsyncTask4(this);
            mTask.execute(ip, bssid, password, deviceCount, broadcast);


            //mTask.execute(ssid, bssid, password, deviceCount, broadcast);

        }

    }

    //异步任务类加载数据
    private static class EsptouchAsyncTask4 extends AsyncTask<byte[], IEsptouchResult, List<IEsptouchResult>> {
        private WeakReference<NetworkActivity> mActivity;//弱引用 当前activity类 类型

        private final Object mLock = new Object();
        private ProgressDialog mProgressDialog;
        private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;

        EsptouchAsyncTask4(NetworkActivity activity) {
            mActivity = new WeakReference<>(activity);

        }

        void cancelEsptouch() {
            cancel(true);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (mResultDialog != null) {
                mResultDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        /***
         * 重写onPreExecute()方法
         * 这里是最终用户调用Excute时的接口，当任务执行之前开始调用此方法，可以在这里显示进度对话框。
         * 在线程上调用start()之前执行的语句.运行在UI线程
         * */
        @Override
        protected void onPreExecute() {
            Activity activity = mActivity.get();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage(activity.getString(R.string.configuring_message));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }

                    }

                }
            });

            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getText(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            synchronized (mLock) {
                                if (mEsptouchTask != null) {
                                    mEsptouchTask.interrupt();
                                }
                            }
                        }
                    });
            mProgressDialog.show();

        }

        /**
         * 重写onProgressUpdate(Progress…) 
         *   可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
         **/

        @Override
        protected void onProgressUpdate(IEsptouchResult... values) {
            Context context = mActivity.get();
            if (context != null) {
                IEsptouchResult result = values[0];
                Log.i(TAG, "EspTouchResult" + result);
                String text = result.getBssid() + "正在已连接wifi";
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... bytes) {
            NetworkActivity activity = mActivity.get();
            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = bytes[0];
                byte[] apBssid = bytes[1];
                byte[] apPassword = bytes[2];
                byte[] deviceCountData = bytes[3];
                byte[] broadcastData = bytes[4];
                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
                Context context = activity.getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(this::publishProgress);//publishProgress用来刷新,这个方法调用之后
                //会自动调用onProgressUpdate方法,将对ui是实时更新重写在onProgressUpdate方法中即可

            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        /***
         * 重写onPostExecute(),运行在UI线程即在主线程中执行.从线程调用View上的runOnUiThread()或post(),
         * 提供要在主应用程序线程上执行的
         * */
        @Override
        protected void onPostExecute(List<IEsptouchResult> iEsptouchResults) {
            NetworkActivity activity = mActivity.get();
            activity.mTask = null;
            mProgressDialog.dismiss();
            if (iEsptouchResults == null) {
                mResultDialog = new AlertDialog.Builder(activity)
                        .setMessage(R.string.configure_result_failed_port)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                mResultDialog.setCanceledOnTouchOutside(false);
                return;
            }
            //检查任务是否被取消,是否没有收到结果
            IEsptouchResult firstResult = iEsptouchResults.get(0);
            if (firstResult.isCancelled()) {
                return;
            }

            //该任务收到了一些结果,包括取消了.在收到足够的结果之前执行
            if (!firstResult.isSuc()) {
                mResultDialog = new AlertDialog.Builder(activity)
                        .setMessage(R.string.configure_result_failed)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                mResultDialog.setCanceledOnTouchOutside(false);
                return;

            }

            ArrayList<CharSequence> resultMsgList = new ArrayList<>(iEsptouchResults.size());
            for (IEsptouchResult touchResult : iEsptouchResults) {
                String message = activity.getString(R.string.configure_result_success_item,
                        touchResult.getBssid(), touchResult.getInetAddress().getHostAddress());
                resultMsgList.add(message);

            }
            CharSequence[] items=new CharSequence[resultMsgList.size()];
            mResultDialog=new AlertDialog.Builder(activity)
                    .setTitle(R.string.configure_result_success)
                    .setItems(resultMsgList.toArray(items),null)
                    .setPositiveButton(android.R.string.ok,null)
                    .show();
            mResultDialog.setCanceledOnTouchOutside(false);

        }
    }
}
