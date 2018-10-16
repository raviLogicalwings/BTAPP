package com.logicalwings.btapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.LibFile;
import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends AppCompatActivity {

    protected LibFile libFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        initData();

        initUi();
    }

    private void initData() {
        libFile = LibFile.getInstance(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }
    }

    private void initUi() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (libFile.getBoolean(AppConstants.IS_LOGIN)) {
                    Intent homeIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    ActivityCompat.finishAffinity(SplashScreenActivity.this);
                    startActivity(homeIntent);
                } else {
                    Intent intentLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    ActivityCompat.finishAffinity(SplashScreenActivity.this);
                    startActivity(intentLogin);
                }
            }
        }, AppConstants.SPLASH_TIME_OUT);
    }
}
