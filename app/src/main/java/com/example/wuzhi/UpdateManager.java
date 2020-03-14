package com.example.wuzhi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UpdateManager {

    private Context mContext;//应用程序Content

    //提示语句
    private String updateMsg="有最新的软件包,点击下载";

    //返回的安装包url.就是下载安装包的网络路径
    private String apkUrl;

    private Dialog noticeDialog;//提示有软件更新的对话框

    private Dialog downloadDialog;//下载对话框

    //下载包安装路径
    private static final String savePath="/sdcard/wuzhi/";//保存apk的文件夹

    private static String saveFileName;//如 savePath+"wuzhi.apk"

    /**进度条与通知UI刷新的handler和msg常量**/

    private ProgressBar mProgress;//进度条

    private static final int DOWN_UPDATE=1;//更新进度条常量
    private static final int DOWN_OVER=2;//下载完成常量

    private int progress;//下载进度

    private Thread downLoadThread;//下载线程

    private boolean interceptFlag=false;

    //通知处理刷新界面的handler
    private android.os.Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE://更新进度
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER://下载完成,安装

                    installApk();
                    break;
                default:
                    break;
            }
        }

    };

    //构造方法
    public UpdateManager(Context context){
        this.mContext=context;
        this.apkUrl=apkUrl;
        this.saveFileName=savePath+saveFileName;

    }
    
    //外部接口,显示更新程序对话框让主Activity调用
    public void checkUpdateInfo(){
        showNoticeDialog();
    }

    //显示通知对话框方法
    private void showNoticeDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//dismiss可以在任何线程，并且销毁了dialog对象。
                showDownloadDialog();

            }
        });
        builder.setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               // System.exit(0);
            }
        });
        noticeDialog=builder.create();
        noticeDialog.show();
    }


    private void showDownloadDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");

        final LayoutInflater inflater=LayoutInflater.from(mContext);
        View v=inflater.inflate(R.layout.progress,null);
        mProgress=v.findViewById(R.id.progress);

        builder.setView(v);//设置对话框的内容为一个view
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag=true;
               // System.exit(0);//关闭APP
            }
        });

        downloadDialog=builder.create();
        downloadDialog.show();

        downloadApk();
    }

    //下载线程
    private final Runnable mdownApkRunnable=new Runnable() {
        @Override
        public void run() {
            try {

                URL url=new URL(apkUrl);//安装包路径,服务器接口
                //创建一条连接
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                //开始连接
                conn.connect();
                //获得连接的总长度
                int length=conn.getContentLength();
                //获得一个输入流,写人文件
                InputStream is=conn.getInputStream();
                //新建文件或者获取文件
                File file=new File(savePath);
                //判断文件是否存在,不存在就创建目录
                if(!file.exists()){
                    file.mkdir();
                }
                //文件名
                String apkFile=saveFileName;
                //创建文件
                File ApkFile=new File(apkFile);
                //创建
                FileOutputStream fos=new FileOutputStream(ApkFile);

                int count=0;
                byte buf[]=new byte[1024];

                do{
                    //将连接的输入流存储到buf数组里面
                    int numread=is.read(buf);
                    count+=numread;
                    progress=(int)((float)(count/length)*100);//进度条进度
                    //1是执行更新进度条,这里设置为1
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if(numread<=0){
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);

                }while (!interceptFlag);//点击取消就停止下载

                fos.close();
                is.close();

            }catch (MalformedURLException e){
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };


    /**下载安装包方法**/
    private void downloadApk(){
        downLoadThread=new Thread(mdownApkRunnable);
        downLoadThread.start();

    }

    /**获取安装包方法**/
    private void installApk(){
        //获取安装包文件
        File apkFile=new File(saveFileName);
        if(!apkFile.exists()){//如果文件不存在
            return;
        }
        //用于显示用户的数据
        Intent intent=new Intent(Intent.ACTION_VIEW);
        //对跳转的传输的数据和类型进行设置
        intent.setDataAndType(Uri.parse("file://"+apkFile.toString()),"application/vnd.android.package-archive");
                                                   //File,toString()会返回路径信息
        mContext.startActivity(intent);

        //销毁app
        System.exit(0);

    }




}
