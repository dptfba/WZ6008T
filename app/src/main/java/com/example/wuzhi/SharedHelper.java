package com.example.wuzhi;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SharedHelper {
    private Context mContext;
    public SharedHelper(Context mContext){
        this.mContext=mContext;
    }

    //定义一个保存数据的方法
    public void save(String password){
        SharedPreferences sp=mContext.getSharedPreferences("mysp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("password",password);
        editor.commit();
        Toast.makeText(mContext,"信息已写入SharedPreference中",Toast.LENGTH_SHORT).show();

    }

    //定义一个读取SP文件的方法
    public Map<String,String> read(){
        Map<String,String> data=new HashMap<>();
        SharedPreferences sp=mContext.getSharedPreferences("mysp",Context.MODE_PRIVATE);
       // data.put("username",sp.getString("username",""));
        data.put("password",sp.getString("password",""));
        return data;
    }
}
