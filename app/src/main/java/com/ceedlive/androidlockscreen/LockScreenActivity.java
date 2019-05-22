package com.ceedlive.androidlockscreen;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LockScreenActivity extends AppCompatActivity {

    ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        CustomLog.info("LockScreenActivity > onCreate: 진입");

//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("5E52A824C274C8491B1CA21E1FD6E82F")
//                .build();
//        mAdView.loadAd(adRequest);

        mConstraintLayout = findViewById(R.id.lockScreenView);
    }

    @Override
    public void onBackPressed() {
        CustomLog.info("LockScreenActivity > onBackPressed: 진입");

        if (mConstraintLayout.getVisibility() == View.VISIBLE) {
            CustomLog.info("LockScreenActivity > onBackPressed: View.VISIBLE1");
        }

        if (mConstraintLayout.getRootView().getVisibility() == View.VISIBLE) {
            CustomLog.info("LockScreenActivity > onBackPressed: View.VISIBLE2");
//            showPopup(false);
            return;
        }
        super.onBackPressed();
    }
}
