package com.example.wuzhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ActionBar;
import android.os.Bundle;

/**关于界面activity**/
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //判断父Activity是否为空,不为空设置导航图表显示
        if(NavUtils.getParentActivityName(AboutActivity.this)!=null){
            //显示向左的箭头图标
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        }
    }
    /**去配置文件配置
     * android:label="">
     * <meta-data android:name="android.support.PARENT_ACTIVITY" android:value=".MainActivity"/>
     * **/
}
