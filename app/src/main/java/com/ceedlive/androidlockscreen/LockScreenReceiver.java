package com.ceedlive.androidlockscreen;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.CALL_STATE_RINGING;

public class LockScreenReceiver extends BroadcastReceiver {

    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;


    /**
     * 인텐트를 받으면 onReceive() 메소드가 자동으로 호출 된다.
     * BroadcastReceiver 는 Intent 를 받았을 때의 처리를 onReceive 에서 구현합니다.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isPhoneIdle = true;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int callState = 0;
        if (telephonyManager != null) {
            callState = telephonyManager.getCallState();
        }
        switch (callState) {
            case CALL_STATE_IDLE:
                isPhoneIdle = true;// 정상
                break;
            case CALL_STATE_RINGING:
                isPhoneIdle = false;// 전화벨이 울리는 경우
                break;
            case CALL_STATE_OFFHOOK:
                isPhoneIdle = false;// 통화중
                break;
        }

        CustomLog.info("LockScreenReceiver > onReceive: isPhoneIdle - " + isPhoneIdle);
        CustomLog.info("LockScreenReceiver > onReceive: intent.getAction() - " + intent.getAction());

        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            CustomLog.info("LockScreenReceiver > onReceive: ACTION_SCREEN_OFF");
            Locker.getInstance().handleScreenOff();
            CustomLog.info("LockScreenReceiver > onReceive: isPhoneIdle - " + isPhoneIdle);
            if (isPhoneIdle) {
//                Locker.getInstance().showLocker();

//                if (km == null) {
//                    CustomLog.info("LockScreenReceiver > onReceive: KeyguardManager is null");
//                    km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//                }
//
//                if (keyLock == null) {
//                    CustomLog.info("LockScreenReceiver > onReceive: newKeyguardLock is null");
//                    keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
//                }
//
//                disableKeyguard();

                Intent i = new Intent(context, LockScreenActivity.class);
                i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                context.startActivity(i);
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            CustomLog.info("LockScreenReceiver > onReceive: ACTION_SCREEN_ON");
            Locker.getInstance().handleScreenOn();
        }

    }

    public void disableKeyguard() {
        // Missing permissions required by KeyguardLock.disableKeyguard: android.permission.DISABLE_KEYGUARD
        keyLock.disableKeyguard();
    }

}
