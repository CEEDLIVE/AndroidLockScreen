package com.ceedlive.androidlockscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout mLayoutLoading;
    ConstraintLayout mLayoutLogin;
    ConstraintLayout mLayoutLogout;

    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomLog.info("MainActivity > onCreate: 진입");

        mLayoutLoading = findViewById(R.id.layoutLoading);
        mLayoutLogin = findViewById(R.id.layoutLogin);
        mLayoutLogout = findViewById(R.id.layoutLogout);
        mBtnLogin = findViewById(R.id.btnLogin);

        CustomLog.info("MainActivity > onCreate: 진입");
//        Screen.getInstance().activate();

        // FIXME
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoading(false);
            }
        }, 3000);


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CustomLog.info("MainActivity > onStart: 진입");
    }

    @Override
    protected void onResume() {
        super.onResume();

        String token = CustomSharedPreference.getString("TOKEN", "");
        boolean isLogin = !"".equals(token);

        showLogin(isLogin);

        CustomLog.info("MainActivity > onResume: 진입");
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CustomLog.info("MainActivity > onActivityResult: 진입");
    }

    private void showLoading(boolean show) {
        mLayoutLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showLogin(boolean login) {
        mLayoutLogin.setVisibility(login ? View.VISIBLE : View.GONE);
        mLayoutLogout.setVisibility(login ? View.GONE : View.VISIBLE);
    }

    private void onLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
