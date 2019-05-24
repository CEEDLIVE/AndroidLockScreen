package com.ceedlive.androidlockscreen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    Button mProfileBtnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfileBtnDone = findViewById(R.id.profileBtnDone);
        mProfileBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone();
            }
        });
    }

    private void onDone() {
        final Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
