package com.ceedlive.androidlockscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    Button mLoginBtnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginBtnDone = findViewById(R.id.loginBtnDone);
        mLoginBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone();
            }
        });

        // FIXME
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Screen.getInstance().activate();
//                finish();
//            }
//        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CustomLog.info(String.format(Locale.KOREA, "LoginActivity > onActivityResult - requestCode: %d resultCode: %d", requestCode, resultCode));

        switch (resultCode) {
            case Activity.RESULT_OK:
                CustomLog.info("LoginActivity > onActivityResult: Activity.RESULT_OK");
                Screen.getInstance().activate();
                finish();
                break;
        }
    }


    private void onDone() {

        CustomSharedPreference.putString("TOKEN", "TEST_TOKEN");

        final Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.putExtra("EDIT_MODE", false);
        startActivityForResult(intent, 0x01);
    }





}
