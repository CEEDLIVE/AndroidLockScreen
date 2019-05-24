package com.ceedlive.androidlockscreen;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LockScreenActivity extends AppCompatActivity {

    private ConstraintLayout mLockScreenView;
    private CustomViewUnLockSwipe mSwipeUnLockButton;

    private BroadcastReceiver mScreenOnOffReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        CustomLog.info("LockScreenActivity > onCreate: 진입");

        AdView adView = findViewById(R.id.lockScreenAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5E52A824C274C8491B1CA21E1FD6E82F")
                .build();
        adView.loadAd(adRequest);

        mLockScreenView = findViewById(R.id.lockScreenView);
        mScreenOnOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    CustomLog.info("LockScreenActivity > mScreenOnOffReceiver: ACTION_SCREEN_ON");

                    final String userToken = CustomSharedPreference.getString("TOKEN", "");

                    CustomLog.info("LockScreenActivity > mScreenOnOffReceiver: ACTION_SCREEN_ON - userToken:" + userToken);
                    CustomLog.info("LockScreenActivity > mScreenOnOffReceiver: ACTION_SCREEN_ON - CALL API");

                } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                    CustomLog.info("LockScreenActivity > mScreenOnOffReceiver: ACTION_SCREEN_OFF");

                    final String userToken = CustomSharedPreference.getString("TOKEN", "");
                    CustomLog.info("LockScreenActivity > mScreenOnOffReceiver: ACTION_SCREEN_OFF - userToken:" + userToken);

                }
            }
        };

//        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
//        intentFilter.setPriority(Integer.MAX_VALUE);
//        registerReceiver(mScreenOnOffReceiver, intentFilter);

        mSwipeUnLockButton = findViewById(R.id.lockScreenSwipeUnLockButton);

        tryDismissKeyguard();
    }

    @Override
    protected void onResume() {
        super.onResume();

        CustomLog.info("LockScreenActivity > onResume");

        if (isScreenOn(this)) {
//            this.presenter.stopLandingAction();
//            this.presenter.onCurrentCampaignShow();
//            this.presenter.impressCurrentCampaign();
//            if (this.m != null) {
//                this.m.onShow();
//            }
        }

        setUnlock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomLog.info("LockScreenActivity > onPause: 진입");
//        if (mScreenOnOffReceiver != null) {
//            unregisterReceiver(mScreenOnOffReceiver);
//        }
//        this.o = false;
//        this.presenter.getLockerManager().onPause();
//        this.presenter.setPreventScreenTurnOffTimer(true);
//        this.presenter.stopScreenTurnOffTimer();
//        this.a.onPause();
//        this.presenter.stopClock();
//        this.presenter.stopSlowLandingTracker();
//        this.closeContextMenuDialog();
//        if (this.g != null) {
//            this.g.dismiss();
//        }

    }

    @Override
    public void onBackPressed() {
        CustomLog.info("LockScreenActivity > onBackPressed: 진입");

        if (mLockScreenView.getVisibility() == View.VISIBLE) {
            CustomLog.info("LockScreenActivity > onBackPressed: View.VISIBLE1");
        }

        if (mLockScreenView.getRootView().getVisibility() == View.VISIBLE) {
            CustomLog.info("LockScreenActivity > onBackPressed: View.VISIBLE2");
//            showPopup(false);
            return;
        }
        super.onBackPressed();
    }

    private void setUnlock() {

        CustomLog.info("LockScreenActivity > setUnlock");

        mLockScreenView.setX(0f);

        mSwipeUnLockButton.setOnUnlockListenerRight(new CustomViewUnLockSwipe.OnUnlockListener() {
            @Override
            public void onUnlock() {
                finish();
            }
        });

        mLockScreenView.setOnTouchListener(new UnLock(this, mLockScreenView));
    }

    @TargetApi(Build.VERSION_CODES.O_MR1)
    public void tryDismissKeyguardAndOverOreoMr1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null) {
                keyguardManager.requestDismissKeyguard(this, null);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void tryDismissKeyguardEqualOreo() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null) {
                keyguardManager.requestDismissKeyguard(this, null);
            }
        }
    }

    public void tryDismissKeyguardRemainder() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
    }

    public void tryDismissKeyguard() {
        // 스크린이 켜질 때 액티비티가 보이게끔 설정하는 코드
        // 다음 코드가 있어야 비로소 기존 잠금화면을 대체하는 커스텀 잠금화면 앱으로 사용될 수 있다.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            // 다음 두 개 코드가 Deprecated 되었는데, 의도하지 않은 두 번의 라이프 사이클 이벤트를 방지하기 위해서 라고 한다. 이 코드 대신 사용할 수 있는 코드는 다음과 같다.
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            tryDismissKeyguardAndOverOreoMr1();

        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            // 다음 코드가 Deprecated 되었는데, 이 이유로는 해당 플래그가 있는 액티비티가 포커스 되었을 때
            // Keyguard 가 해제되므로 의도하지 않은 터치를 막을 수 없기 때문이라고 한다.
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            // 대신, KeyguardManager 를 통하여 keyguard 를 dismiss 하는 요청을 해야 한다.
            tryDismissKeyguardEqualOreo();
        } else {
            tryDismissKeyguardRemainder();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static boolean isScreenOn(Context context) {
        return isScreenOn( (PowerManager) context.getSystemService(Context.POWER_SERVICE) );
    }
    public static boolean isScreenOn(PowerManager powerManager) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH ? powerManager.isInteractive() : powerManager.isScreenOn();
    }

}
