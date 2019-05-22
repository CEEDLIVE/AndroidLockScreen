package com.ceedlive.androidlockscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    ConstraintLayout mConstraintLayout;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mConstraintLayout = findViewById(R.id.layoutSplash);
        mProgressBar = findViewById(R.id.progressBar);

        CustomLog.info("SplashActivity > onCreate: 진입");

        // Screen.init 이 선행되어야 함
        Screen.getInstance().launch();

        // Login
//        Screen.getInstance().activate();
    }

    // 홈 버튼 클릭 시 진입
    @Override
    protected void onPause() {
        super.onPause();
        CustomLog.info("SplashActivity > onPause: 진입");
    }

    // 최근 사용한 앱 목록에서 해당 앱 터치 시 진입
    @Override
    protected void onResume() {
        super.onResume();
        CustomLog.info("SplashActivity > onResume: 진입");
        mConstraintLayout.postDelayed(runnableGoNext, 10);
    }

    private Runnable runnableGoNext = new Runnable() {
        @Override public void run() {
            mProgressBar.setProgress(mProgressBar.getProgress() + 1);
            if (mProgressBar.getProgress() < mProgressBar.getMax()) {
                mConstraintLayout.postDelayed(runnableGoNext, 10);
            } else {
                final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    };

}
