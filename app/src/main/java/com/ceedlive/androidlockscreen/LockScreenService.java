package com.ceedlive.androidlockscreen;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class LockScreenService extends Service {

    // 서비스는 쉽게 말하면 메인스레드에서 동작하는 UI가 없는 액티비티
    // UI가 없으므로 라이프사이클은 다음과 같이 동작한다.
    // onCreate() -> onStart() -> onDestory()
    // 메인스레드에서 관리하기 때문에 UI가 종료되어도 살아서 서비스를 계속한다.

    private LockScreenReceiver mLockScreenReceiver;
    private BroadcastReceiver mBroadcastReceiver;


    public LockScreenService() {

    }

    /**
     * Service 객체와 (화면단 Activity 사이에서) 통신(데이터를 주고받을) 때 사용하는 메서드
     * @param intent
     * @return 데이터를 전달할 필요가 없으면 return null
     */
    @Override
    public IBinder onBind(Intent intent) {

        // Service 객체와 (화면단 Activity 사이에서) 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        // reference: https://okky.kr/article/533867

        // 액티비티에서 bindService() 를 실행하면 호출됨
        // 리턴한 IBinder 객체는 서비스와 클라이언트 사이의 인터페이스 정의한다.

        // 다른 컴포넌트가 bindService()를 호출해서 서비스와 연결을 시도하면 이 메소드가 호출됩니다.
        // 이 메소드에서 IBinder를 반환해서 서비스와 컴포넌트가 통신하는데 사용하는 인터페이스를 제공해야 합니다.
        // 만약 시작 타입의 서비스를 구현한다면 null 을 반환하면 됩니다.

        return null;
    }


    // Service 와 Thread 가 사용될 시점을 생각해 보자.
    // Thread 는 앱이 사용자와 상호작용하는 과정에서 UI Thread 가 Block 되지 않기 위한 작업등을 처리하기 위한 Foreground 작업에 적합하고
    // Service 는 앱이 사용자와 상호작용하지 않아도 계속 수행되어야 하는 Background 작업에 적합하다고 볼 수 있다.
    // 물론 Service 내부에서 Thread 가 사용되어야 하지만 큰 틀에서 봤을 때 위와 같은 개념으로 나눌 수 있을 것이다.

    /**
     * 시작 서비스를 구현하고 비동기 태스크 실행을 초기화하기 위한 중요한 메서드
     * 백그라운드에서 실행되는 동작들이 들어가는 곳
     * 서비스가 호출될 때마다 실행
     *
     * onStartCommand() is always called on the main application thread in any service.
     * You cannot be called with onStartCommand() in two threads simultaneously.
     *
     * @param intent 인텐트 : 비동기 실행을 위해 사용되는 데이터, 예를 들면 네트워크 자원의 URL
     * @param flags 전달 메서드 : 시작 요청의 기록을 반영하는 플래그, 0, START_FLAG_REDELIVERY (1), START_FLAG_RETRY(2)
     * @param startId 시작 ID : 런타임에서 제공하는 고유 식별자
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 백그라운드에서 실행되는 동작들이 들어가는 곳입니다.
        // 서비스가 호출될 때마다 실행

        // 다른 컴포넌트가 startService()를 호출해서 서비스가 시작되면 이 메소드가 호출됩니다.
        // 만약 연결된 타입의 서비스를 구현한다면 이 메소드는 재정의 할 필요가 없습니다.

        Log.i("CEEDLIVE", "LockScreenService > onStartCommand: flags:" + flags + " startId: " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("CEEDLIVE", "LockScreenService > onCreate");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundNotification();
        }

        // registerReceiver
        registerLockScreenReceiver();
        registerIdleModeChangedReceiver();
    }

    /**
     * 서비스가 종료될 때 할 작업
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CEEDLIVE", "LockScreenService > onDestroy");
        if (this.mLockScreenReceiver != null) {
            this.unregisterReceiver(this.mLockScreenReceiver);
        }
        if (this.mBroadcastReceiver != null) {
            this.unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    private void startForegroundNotification() {
        this.startForeground(5001, this.buildNotification());
    }

    /**
     *
     * @return
     */
    private Notification buildNotification() {
        Log.i("CEEDLIVE", "LockScreenService > buildNotification");
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && !Locker.getInstance().h().isShowAlways()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return new Notification();
        } else {
            try {
                return Locker.getInstance().getServiceNotificationInterface().build();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return new Notification();
            }
        }
    }

    /**
     *
     */
    private void registerLockScreenReceiver() {
        Log.i("CEEDLIVE", "LockScreenService > registerLockScreenReceiver");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.setPriority(1);
        this.mLockScreenReceiver = new LockScreenReceiver();
        this.registerReceiver(this.mLockScreenReceiver, intentFilter);
    }

    /**
     *
     */
    private void registerIdleModeChangedReceiver() {
        Log.i("CEEDLIVE", "LockScreenService > registerIdleModeChangedReceiver");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    Log.i("CEEDLIVE", "LockScreenService > registerIdleModeChangedReceiver: intent.getAction() - " + intent.getAction());
                    if ( "android.os.action.DEVICE_IDLE_MODE_CHANGED".equals( intent.getAction() ) ) {
                        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                        if ( powerManager.isDeviceIdleMode() ) {
                            Log.i("CEEDLIVE", "LockScreenService > registerIdleModeChangedReceiver: IDLE");
                        } else {
                            Log.i("CEEDLIVE", "LockScreenService > registerIdleModeChangedReceiver: NOT IDLE");
//                                if (!DeviceUtils.isScreenOn(var3)) {
//                                    Locker.getInstance().getCampaignPool().a((Map)null);
//                                }
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
            this.registerReceiver(this.mBroadcastReceiver, intentFilter);
        }
    }

}
