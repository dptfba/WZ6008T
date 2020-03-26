package com.example.wuzhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wuzhi.Utils.LocaleUtils;
import com.example.wuzhi.startApp.RestartAppTool;

public class LanguageActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_chinese;
    TextView tv_english;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        iv_back = findViewById(R.id.iv_back);
        tv_chinese = findViewById(R.id.tv_chinese);
        tv_english = findViewById(R.id.tv_english);
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
                    // LocaleUtils.saveUserLocale(LanguageActivity.this,LocaleUtils.LOCALE_CHINESE);
                    LocaleUtils.updateLocale(LanguageActivity.this, LocaleUtils.LOCALE_CHINESE);
                    LocaleUtils.saveUserLocale(LanguageActivity.this, LocaleUtils.LOCALE_CHINESE);

                    finish();
                    RestartAppTool.restartAPP(getApplicationContext(), 100);
                }

               // RestartAppTool.restartAPP(getApplicationContext(), 100);

            }
        });
        //点击英文
        tv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换为英语
                if (LocaleUtils.needUpdateLocale(LanguageActivity.this, LocaleUtils.LOCALE_ENGLISH)) {
                    // LocaleUtils.saveUserLocale(LanguageActivity.this, LocaleUtils.LOCALE_ENGLISH);
                    LocaleUtils.updateLocale(LanguageActivity.this, LocaleUtils.LOCALE_ENGLISH);
                    LocaleUtils.saveUserLocale(LanguageActivity.this, LocaleUtils.LOCALE_ENGLISH);

                    finish();
                   RestartAppTool.restartAPP(getApplicationContext(), 100);
                }
              //  RestartAppTool.restartAPP(getApplicationContext(), 100);

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
        //重启APP
        // RestartAppTool.restartAPP(getApplicationContext(), 100);


    }


}
