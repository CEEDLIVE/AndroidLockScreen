package com.ceedlive.androidlockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver {

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
        int callState = telephonyManager.getCallState();
        switch (callState) {
            case TelephonyManager.CALL_STATE_IDLE:
                isPhoneIdle = true;// 정상
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                isPhoneIdle = false;// 전화벨이 울리는 경우
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                isPhoneIdle = false;// 통화중
                break;
        }
        Log.i("CEEDLIVE", "LockScreenReceiver > onReceive: isPhoneIdle - " + isPhoneIdle);

        if ( "android.intent.action.SCREEN_OFF".equals( intent.getAction() ) ) {
            Log.i("CEEDLIVE", "LockScreenReceiver > onReceive: ACTION_SCREEN_OFF");
            Locker.getInstance().handleScreenOff();
            if (isPhoneIdle) {
                Locker.getInstance().showLocker();
            }
        } else if ( "android.intent.action.SCREEN_ON".equals( intent.getAction() ) ) {
            Log.i("CEEDLIVE", "LockScreenReceiver > onReceive: ACTION_SCREEN_ON");
            Locker.getInstance().handleScreenOn();
        }

    }
}
