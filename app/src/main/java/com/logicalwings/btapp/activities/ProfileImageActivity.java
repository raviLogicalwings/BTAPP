package com.logicalwings.btapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.logicalwings.btapp.R;

import java.util.Objects;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ProfileImageActivity extends BaseActivity {

    ImageView profileZoom;
    PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Profile Image");
        }

        initData();

        initUi();

    }

    @Override
    protected void initData() {
        super.initData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }

//        Bundle bundle = getIntent().getExtras();
//        String image = bundle.getString("imagePath");

    }

    @Override
    protected void initUi() {
        super.initUi();

        profileZoom = findViewById(R.id.profileImageZoom);

        photoViewAttacher = new PhotoViewAttacher(profileZoom);
        photoViewAttacher.update();

    }

}
