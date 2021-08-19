package com.example.unimpdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import io.dcloud.feature.sdk.DCUniMPSDK;

public class TestPageActivity extends Activity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        FrameLayout rootView = new FrameLayout(this);
        Button button = new Button(this);
        button.setText("点击我跳转小程序");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DCUniMPSDK.getInstance().startApp(context,"__UNI__04E3A11",  MySplashView.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        rootView.addView(button);
        setContentView(rootView);
    }
}
