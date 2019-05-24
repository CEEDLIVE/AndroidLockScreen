package com.ceedlive.androidlockscreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // FIXME
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainActivity.class); // 다음 넘어갈 클래스 지정
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); // 다음 화면으로 넘어간다

                // 액티비티를 넘어간 후 이전 액티비티를 삭제하고 싶다면 다음의 명령어를 사용한다.
                finish();
            }
        }, 3000);

    }

}
