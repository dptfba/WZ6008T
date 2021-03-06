package com.example.wuzhi;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;


import com.example.wuzhi.Utils.LocaleUtils;

import com.example.wuzhi.startApp.RestartAppTool;

public class LanguageActivity extends BaseActivity {
    ImageView iv_back;
    Button tv_chinese;//中文
    Button tv_english;//英文
    Button tv_TW;//繁体
    Button btn_ja;//日语


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        iv_back = findViewById(R.id.iv_back);
        tv_chinese = findViewById(R.id.tv_chinese);
        tv_english = findViewById(R.id.tv_english);
        tv_TW = findViewById(R.id.tv_TW);
        btn_ja = findViewById(R.id.btn_ja);

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
                if (LocaleUtils.needUpdateLocale(LanguageActivity.this, LocaleUtils.LOCALE_CHINESE)) {
                    LocaleUtils.updateLocale(LanguageActivity.this, LocaleUtils.LOCALE_CHINESE);
                    restartAct();
                    RestartAppTool.restartAPP(getApplicationContext(), 100);

                }

            }
        });
        //点击英文
        tv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为英语
                if (LocaleUtils.needUpdateLocale(LanguageActivity.this, LocaleUtils.LOCALE_ENGLISH)) {
                    LocaleUtils.updateLocale(LanguageActivity.this, LocaleUtils.LOCALE_ENGLISH);
                    restartAct();
                    RestartAppTool.restartAPP(getApplicationContext(), 100);

                }


            }
        });

        //点击繁体中文
        tv_TW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为繁体
                if (LocaleUtils.needUpdateLocale(LanguageActivity.this, LocaleUtils.TRADITIONAL_CHINESE)) {
                    LocaleUtils.updateLocale(LanguageActivity.this, LocaleUtils.TRADITIONAL_CHINESE);
                    restartAct();
                    RestartAppTool.restartAPP(getApplicationContext(), 100);

                }

            }
        });

        //点击日语
        btn_ja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为日文
                if (LocaleUtils.needUpdateLocale(LanguageActivity.this, LocaleUtils.JAPANESE)) {
                    LocaleUtils.updateLocale(LanguageActivity.this, LocaleUtils.JAPANESE);
                    restartAct();
                    RestartAppTool.restartAPP(getApplicationContext(), 100);//重启app

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
        // 清除Activity退出和进入的动画
        overridePendingTransition(0, 0);

    }

}
