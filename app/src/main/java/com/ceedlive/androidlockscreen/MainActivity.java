package com.ceedlive.androidlockscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout mLayoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutLoading = findViewById(R.id.layoutLoading);

        CustomLog.info("MainActivity > onCreate: 진입");
        Screen.getInstance().activate();
        showLoading(false);
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

}
