package com.ceedlive.androidlockscreen;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.CallSuper;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public class Locker {

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

    private Locker.ServiceNotificationInterface mServiceNotificationInterface;
    private Set<LifeCycleListener> lifeCycleListenerSet = Collections.newSetFromMap(new WeakHashMap());


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

    static void init(Context context) {
        CustomLog.info("Locker > init(Context context): 진입");
        if (sLocker == null) {
            synchronized (Locker.class) {
                if (sLocker == null) {
                    CustomLog.info("Locker > init(Context context): if (sLocker == null) {");
                    sLocker = new Locker(context);
                }
            }
        }
        CustomLog.info("sLocker.toString(): " + sLocker.toString());
    }

    void launch() {
        CustomLog.info("Locker > launch: 진입");
        check();
    }

    private void check() {
        // condition
        CustomLog.info("Locker > check: 진입");
        start();
    }

    private boolean isRunning() {
        CustomLog.info("Locker > isRunning: 진입");

        ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE);

        // getRunningServices
        // This method was deprecated in API level 26. As of Build.VERSION_CODES.O, this method is no longer available to third party applications. For backwards compatibility, it will still return the caller's own services.
        Iterator runningServiceInfoIterator = null;
        if (activityManager != null) {
            runningServiceInfoIterator = activityManager.getRunningServices(2147483647).iterator();
        }
        // 2147483647: 32비트 정수의 최대값, 2의 31승 - 1

        ActivityManager.RunningServiceInfo runningServiceInfo = null;
        do {
            if (runningServiceInfoIterator != null) {
                if ( runningServiceInfoIterator.hasNext() ) {
                    runningServiceInfo = (ActivityManager.RunningServiceInfo) runningServiceInfoIterator.next();
                } else {
                    return false;
                }
            }
        } while ( !(runningServiceInfo != null && runningServiceInfo.service.getPackageName().equals(mContext.getPackageName()))
                || !runningServiceInfo.service.getClassName().equals( LockScreenService.class.getName() ) );

        return true;
    }

    void start() {
        CustomLog.info("Locker > start: 진입");
        if ( !this.isRunning() ) {
            Intent intent = new Intent(this.mContext, LockScreenService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CustomLog.info("Locker > start: if - Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");
                // 오레오 이상
                // 모든 서비스를 제한없이 시작할 수 있게 해주지만, 5초 내에 Service.startForeground()를 통해 Notification 과 연결되지 않으면 즉시 해당 서비스를 중지합니다.
                this.mContext.startForegroundService(intent);
            } else {
                CustomLog.info("Locker > start: else");
                this.mContext.startService(intent);
            }

            // TODO START LISTENER

            for (LifeCycleListener lifeCycleListener : this.lifeCycleListenerSet) {
                lifeCycleListener.onStart();
            }
        }
    }

    void stop() {
        CustomLog.info("Locker > stop: 진입");
        if ( this.isRunning() ) {
            Intent intent = new Intent(this.mContext.getApplicationContext(), LockScreenService.class);
            this.mContext.stopService(intent);

            // TODO STOP LISTENER

            for (LifeCycleListener lifeCycleListener : this.lifeCycleListenerSet) {
                lifeCycleListener.onStop();
            }
        }
    }


    // 이 액티비티 플래그를 사용하여 액티비티를 호출하게 되면 새로운 태스크를 생성하여 그 태스크 안에 액티비티를 추가하게 됩니다.
    // 단, 기존에 존재하는 태스크들 중에 생성하려는 액티비티와 동일한 affinity를 가지고 있는 태스크가 있다면 그곳으로 새 액티비티가 들어가게 됩니다.
    // 하나의 어플리케이션 안에서는 모든 액티비티가 기본 affinity 를 가지고 같은 태스크 안에서 동작하는 것이 기본적(물론 변경이 가능합니다)이지만
    // FLAG_ACTIVITY_MULTIPLE_TASK 플래그와 함께 사용하지 않을경우 무조건적으로 태스크가 새로 생성되는것은 아님을 주의하셔야 합니다.
    void showLocker() {
        CustomLog.info("Locker > showLocker: 진입");

        for (Locker.LifeCycleListener lifeCycleListener : lifeCycleListenerSet) {
            boolean isTryShow = lifeCycleListener.onTryShow();
            if (!isTryShow) {
                return;
            }
        }

        Intent intent = new Intent(mContext, LockScreenActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        this.mContext.startActivity(intent);

        for (Locker.LifeCycleListener lifeCycleListener : lifeCycleListenerSet) {
            lifeCycleListener.onShow();
        }

    }

    void handleScreenOff() {
        CustomLog.info("Locker > handleScreenOff: 진입");
        // TODO add listener
//        Iterator var1 = this.u.iterator();
//        while(var1.hasNext()) {
//            Locker.LifeCycleListener var2 = (Locker.LifeCycleListener)var1.next();
//            var2.onScreenOff();
//        }

        CustomLog.info("Locker > handleScreenOff: lifeCycleListenerSet.size(): " + lifeCycleListenerSet.size());

        for (Locker.LifeCycleListener lifeCycleListener: lifeCycleListenerSet) {
            lifeCycleListener.onScreenOff();
        }

    }

    void handleScreenOn() {
        CustomLog.info("Locker > handleScreenOn: 진입");
        // TODO add listener
//        Iterator var1 = this.u.iterator();
//        while(var1.hasNext()) {
//            Locker.LifeCycleListener var2 = (Locker.LifeCycleListener)var1.next();
//            var2.onScreenOn();
//        }

        CustomLog.info("Locker > handleScreenOn: lifeCycleListenerSet.size(): " + lifeCycleListenerSet.size());

        for (Locker.LifeCycleListener lifeCycleListener: lifeCycleListenerSet) {
            lifeCycleListener.onScreenOn();
        }
    }

    public void addLifeCycleListener(Locker.LifeCycleListener lifeCycleListener) {
        CustomLog.info("Locker >  addLifeCycleListener");
        this.lifeCycleListenerSet.add(lifeCycleListener);
    }

    public static class LifeCycleListener {
        public LifeCycleListener() {
        }

        @CallSuper
        public void onStart() {
            CustomLog.info("Locker >  LifeCycleListener: onStart");
        }

        @CallSuper
        public void onStop() {
            CustomLog.info("Locker >  LifeCycleListener: onStop");
        }

        @CallSuper
        public boolean onTryShow() {
            return true;
        }

        @CallSuper
        public void onShow() {
            CustomLog.info("Locker >  LifeCycleListener: onShow");
        }

        @CallSuper
        public void onScreenOn() {
            CustomLog.info("Locker >  LifeCycleListener: onScreenOn");
        }

        @CallSuper
        public void onScreenOff() {
            CustomLog.info("Locker >  LifeCycleListener: onScreenOff");
        }
    }

    public interface ServiceNotificationInterface {
        Notification build();
        boolean isShowAlways();
    }

    public void setServiceNotificationInterface(Locker.ServiceNotificationInterface serviceNotification) {
        CustomLog.info("Locker > setServiceNotificationInterface: 진입");
        this.mServiceNotificationInterface = serviceNotification;
    }

    Locker.ServiceNotificationInterface getServiceNotificationInterface() {
        CustomLog.info("Locker > getServiceNotificationInterface: 진입");
        return this.mServiceNotificationInterface;
    }


}
