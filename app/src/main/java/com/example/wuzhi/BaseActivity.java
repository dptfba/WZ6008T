package com.example.wuzhi;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wuzhi.Utils.LocaleUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        Context context =languageWork(newBase);
        Log.d("===BaseActivity===",String.valueOf(context));
       super.attachBaseContext(context);

    }

    private Context languageWork(Context context) {
        // 8.0及以上使用createConfigurationContext设置configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("===languageWork>8.0===",String.valueOf(context));
           return updateResources(context);


        } else {
            Log.d("===languageWork<8.0===",String.valueOf(context));
            return context;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale = LocaleUtils.getUserLocale(context);
        if (locale == null) {
            return context;
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }



}
