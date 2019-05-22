package com.ceedlive.androidlockscreen;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Iterator;

public class Locker {

    private final String LOG_TAG = "Locker";
    private Context mContext;

    // volatile을 선언함으로써 JVM은 해당 변수에 대한 모든 읽기 연산을 항상 메인 메모리에서 부터 읽어가도록 보장해 줍니다.
    // 그리고 변수에 대한 모든 write 역시 항상 메인 메모리에 기록되도록 해 줍니다.

    // 두 쓰레드가 공유 변수에 대한 읽기와 쓰기 연산이 있을 경우 volatile 키워드로는 충분하지 않습니다.
    // 이 경우 synchronization 를 통해 변수의 읽기 쓰기 연산의 원자성(atomic)을 보장해 줘야 합니다.
    // 하지만 한 쓰레드에서 volatile 변수의 값을 읽고 쓰고, 다른 쓰레드에서는 오직 변수 값을 읽기만 할 경우,
    // 그러면 읽는 쓰레드에서는 volatile 변수의 가장 최근에 쓰여진 값을 보는 것을 보장할 수 있습니다.
    // volatile 없이는 이를 보장해 줄 수 없습니다.

    // volatile 변수에 대한 읽기와 쓰기는 변수를 메인 메모리로 부터 읽거나 쓰게 됩니다.
    // 메인 메모리에 읽고 쓰는것은 CPU 캐시보다 더 비싸다고 할 수 있습니다.
    // 또한 volatile 변수는 성능을 개선 기법인 명령(instruction)들의 재배치를 방지하기 때문에 변수의 가시성을 강제할 필요가 있는 경우에만 volatile 변수를 사용하는 것이 좋습니다.
    private static volatile Locker sLocker;

    private Locker.ServiceNotificationInterface serviceNotificationInterface;


    private Locker(Context context) {
        this.mContext = context;
    }

    public static Locker getInstance() {
        if (sLocker == null) {
            throw new NullPointerException("Must call init first!");
        } else {
            return sLocker;
        }
    }

    public static void init(Context context) {
        Log.i("CEEDLIVE", "Locker > init(Context context): 진입");
        if (sLocker == null) {
            synchronized (Locker.class) {
                if (sLocker == null) {
                    Log.i("CEEDLIVE", "Locker > init(Context context): if (sLocker == null) {");
                    sLocker = new Locker(context);
                }
            }
        }
        Log.i("Locker", sLocker.toString());
    }

    void launch() {
        Log.i("CEEDLIVE", "Locker > launch: 진입");
        check();
    }

    void check() {
        // condition
        Log.i("CEEDLIVE", "Locker > check: 진입");
        start();
    }

    boolean isRunning() {
        Log.i("CEEDLIVE", "Locker > isRunning: 진입");

        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        // getRunningServices
        // This method was deprecated in API level 26. As of Build.VERSION_CODES.O, this method is no longer available to third party applications. For backwards compatibility, it will still return the caller's own services.
        Iterator runningServiceInfoIterator = activityManager.getRunningServices(2147483647).iterator();
        // 2147483647: 32비트 정수의 최대값, 2의 31승 - 1

        ActivityManager.RunningServiceInfo runningServiceInfo;
        do {
            if ( !runningServiceInfoIterator.hasNext() ) {
                return false;
            }
            runningServiceInfo = (ActivityManager.RunningServiceInfo) runningServiceInfoIterator.next();
        } while ( !runningServiceInfo.service.getPackageName().equals( mContext.getPackageName() )
                || !runningServiceInfo.service.getClassName().equals( LockScreenService.class.getName() ) );

        return true;
    }

    void start() {
        Log.i("CEEDLIVE", "Locker > start: 진입");
        if ( !isRunning() ) {
            Intent intent = new Intent(mContext.getApplicationContext(), LockScreenService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 오레오 이상
                // 모든 서비스를 제한없이 시작할 수 있게 해주지만, 5초 내에 Service.startForeground()를 통해 Notification 과 연결되지 않으면 즉시 해당 서비스를 중지합니다.
                mContext.startForegroundService(intent);
            } else {
                mContext.startService(intent);
            }
            // TODO START LISTENER
        }
    }

    void stop() {
        Log.i("CEEDLIVE", "Locker > stop: 진입");
        if ( this.isRunning() ) {
            Intent intent = new Intent(mContext.getApplicationContext(), LockScreenService.class);
            mContext.stopService(intent);
            // TODO STOP LISTENER
        }
    }

    void showLocker() {
        Log.i("CEEDLIVE", "Locker > showLocker: 진입");
        Intent intent = new Intent(mContext, LockScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public interface ServiceNotificationInterface {
        Notification build();
        boolean isShowAlways();
    }

    Locker.ServiceNotificationInterface getServiceNotificationInterface() {
        return this.serviceNotificationInterface;
    }



    void handleScreenOff() {
        Log.i("CEEDLIVE", "Locker > handleScreenOff: 진입");
        // TODO add listener
//        Iterator var1 = this.u.iterator();
//        while(var1.hasNext()) {
//            Locker.LifeCycleListener var2 = (Locker.LifeCycleListener)var1.next();
//            var2.onScreenOff();
//        }
    }

    void handleScreenOn() {
        Log.i("CEEDLIVE", "Locker > handleScreenOn: 진입");
        // TODO add listener
//        Iterator var1 = this.u.iterator();
//        while(var1.hasNext()) {
//            Locker.LifeCycleListener var2 = (Locker.LifeCycleListener)var1.next();
//            var2.onScreenOn();
//        }
    }

}
