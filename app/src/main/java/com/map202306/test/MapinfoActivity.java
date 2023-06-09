/*
package com.map202306.test;

import static java.lang.Math.round;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowMetrics;

public class MapinfoActivity extends Activity {

    private View mapInfoName;
    private View mapInfoAddr;
    private String name;
    private String addr;

    @Override
    protected void onResume() {
        super.onResume();
        mapInfoName = findViewById(R.id.map_info_name2);
        mapInfoAddr  = findViewById(R.id.map_info_address);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        addr = intent.getStringExtra("address");

        getMapinfoName.setText(name);
        getMapinfoAddr.setText(address);

        initLayout();
    }

    private void initLayout() {
        int width;
        int height;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 버전
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            width = windowMetrics.getBounds().width();
            height = windowMetrics.getBounds().height();
        } else { // API Level 30 이전 버전
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        }
        getWindow().setLayout((int) round(width * 0.9), (int)round(height * 0.22));
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}

*/
