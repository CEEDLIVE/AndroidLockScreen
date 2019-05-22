package com.ceedlive.androidlockscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.i("CEEDLIVE", "SplashActivity > onCreate: 진입");

        // Screen.init 이 선행되어야 함
        Screen.getInstance().launch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CEEDLIVE", "SplashActivity > onPause: 진입");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CEEDLIVE", "SplashActivity > onResume: 진입");
    }

}
