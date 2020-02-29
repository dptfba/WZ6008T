package com.example.wuzhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wuzhi.Utils.LocaleUtils;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    ListView languageListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        languageListView=findViewById(R.id.lv_language);


        //判断父Activity是否为空,不为空设置导航图表显示
        if (NavUtils.getParentActivityName(LanguageActivity.this) != null) {
            //显示向左的箭头图标
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        //点击listView列表项,选择语言
        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //切换为简体中文
                        if(LocaleUtils.needUpdateLocale(LanguageActivity.this,LocaleUtils.LOCALE_CHINESE)){
                            LocaleUtils.updateLocale(LanguageActivity.this,LocaleUtils.LOCALE_CHINESE);
                            restartAct();
                        }

                        break;
                    case 1:
                        //切换为英语
                        if(LocaleUtils.needUpdateLocale(LanguageActivity.this,LocaleUtils.LOCALE_ENGLISH)){
                            LocaleUtils.updateLocale(LanguageActivity.this,LocaleUtils.LOCALE_ENGLISH);
                            restartAct();
                        }
                        break;
                        default:
                            break;
                }



            }
        });

    }
    /**
     * 重启当前Activity
     */
    private void restartAct() {
        finish();
        Intent _Intent = new Intent(this, MainActivity.class);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);

    }



}
