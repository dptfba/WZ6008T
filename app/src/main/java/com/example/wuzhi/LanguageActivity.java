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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wuzhi.Utils.LocaleUtils;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_chinese;
    TextView tv_english;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        iv_back=findViewById(R.id.iv_back);
        tv_chinese=findViewById(R.id.tv_chinese);
        tv_english=findViewById(R.id.tv_english);
        //点击返回箭头
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }

        });
        //点击简体中文
        tv_chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为简体中文
                if(LocaleUtils.needUpdateLocale(LanguageActivity.this,LocaleUtils.LOCALE_CHINESE)){
                    LocaleUtils.updateLocale(LanguageActivity.this,LocaleUtils.LOCALE_CHINESE);
                    restartAct();
                }

            }
        });
        //点击英文
        tv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为英语
                if(LocaleUtils.needUpdateLocale(LanguageActivity.this,LocaleUtils.LOCALE_ENGLISH)){
                    LocaleUtils.updateLocale(LanguageActivity.this,LocaleUtils.LOCALE_ENGLISH);
                    restartAct();
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
