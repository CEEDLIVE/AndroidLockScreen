package com.ceedlive.androidlockscreen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Screen {

    private Context mContext;
    private static Screen sScreen;
    private static Notification sNotification;

    private Locker.LifeCycleListener lifeCycleListener = new Locker.LifeCycleListener() {
        public void onStart() {
            super.onStart();
            CustomLog.debug("Screen > lifeCycleListener > lifeCycle onStart");
            Screen.this.test(true);
        }

        public void onStop() {
            super.onStop();
            CustomLog.debug("Screen > lifeCycleListener > lifeCycle onStop");
            Screen.this.test(false);
        }

        public void onScreenOff() {
            super.onScreenOff();
            CustomLog.debug("Screen > lifeCycleListener > lifeCycle onScreenOff");
        }

        public void onScreenOn() {
            super.onScreenOn();
            CustomLog.debug("Screen > lifeCycleListener > lifeCycle onScreenOn");
        }
    };

    private List<OnActivationListener> onActivationListenerList;


    private Screen(Context context) {
        this.mContext = context;
        onActivationListenerList = new ArrayList();
        Locker.getInstance().addLifeCycleListener(lifeCycleListener);
    }

    public static Screen getInstance() {
        if (sScreen != null) {
            return sScreen;
        } else {
            throw new RuntimeException("Screen class not correctly initialized. Please call Screen.init in the CustomApplication class onCreate.");
        }
    }

    public static void init(Context context) {
        CustomLog.info("Screen > init(Context context): 진입");

        // Set Locker
        Locker.init(context);
        Locker.getInstance().setServiceNotificationInterface(new Locker.ServiceNotificationInterface() {
            @Override
            public Notification build() {
                CustomLog.info("Screen > init(Context context): build");
                if (Screen.sNotification == null) {
                    Screen.sNotification = Screen.getInstance().buildNotification();
                }
                return Screen.sNotification;
            }

            @Override
            public boolean isShowAlways() {
                CustomLog.info("Screen > init(Context context): isShowAlways");
                return false;
            }
        });

        // Set Screen
        sScreen = new Screen(context);
    }

    public void launch() {
        // TODO 전처리
        CustomLog.info("Screen > launch: 진입");
        Locker.getInstance().launch();
    }

    void activate() {
        CustomLog.info("Screen > activate: 진입");
        // TODO condition
        Locker.getInstance().start();
    }

    void deactivate() {
        CustomLog.info("Screen > deactivate: 진입");
        // TODO condition
        Locker.getInstance().stop();
    }

    void showScreen() {
        CustomLog.info("Screen > showScreen: 진입");
        Locker.getInstance().showLocker();
    }

    void login() {
        CustomLog.info("Screen > login: 진입");
    }

    void logout() {
        CustomLog.info("Screen > logout: 진입");
    }


    private Notification buildNotification() {
        CustomLog.info("Screen > buildNotification: 진입");

        NotificationCompat.Builder notificationBuilder;
        String id = "AndroidLockScreenLockerService";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CustomLog.info("Screen > buildNotification: Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");

            String name = "Locker Service";
            String description = "Locker Service";

            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription(description);
            notificationChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(this.mContext, id);

        // Notification 객체는 다음을 반드시 포함해야 합니다.
        {
            notificationBuilder
                    .setSmallIcon(R.drawable.ic_aaaa)
                    .setContentTitle("ContentTitle")
                    .setContentText("ContentText");

            // setSmallIcon()이 설정한 작은 아이콘
            // setContentTitle()이 설정한 제목
            // setContentText()이 설정한 세부 텍스트
        }

        notificationBuilder.setWhen(0L);
//        Intent intent;
//        if (this.a.getIntent() != null) {
//            intent = this.a.getIntent();
//        } else {
//            intent = getLaunchIntentForPackage(this.mContext);
////            intent.setFlags(603979776);// 603979776
//        }
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, 0, intent, 0);
//        notificationBuilder.setContentIntent(pendingIntent);
//        if (this.a.getTitle().length() > 0) {
//            notificationBuilder.setContentTitle(this.a.getTitle());
//        }
//
//        if (this.a.getText().length() > 0) {
//            notificationBuilder.setContentText(this.a.getText());
//        }
//
//        if (this.a.getSmallIconResourceId() != 0) {
//            notificationBuilder.setSmallIcon(this.a.getSmallIconResourceId());
//        }
//
//        if (this.a.getLargeIconResourceId() != 0) {
//            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.c.getResources(), this.a.getLargeIconResourceId()));
//        }

        return notificationBuilder.build();
    }

    public interface OnActivationListener {
        void onActivated();

        void onDeactivated();
    }

    private void test(boolean isActivated) {
        for (Screen.OnActivationListener onActivationListener : onActivationListenerList) {
            if (isActivated) {
                onActivationListener.onActivated();
            } else {
                onActivationListener.onDeactivated();
            }
        }
    }

//    public void registerOnActivationListener(Screen.OnActivationListener onActivationListener) {
//        this.unregisterOnActivationListener(onActivationListener);
//        this.onActivationListenerList.add(onActivationListener);
//    }
//
//    public void unregisterOnActivationListener(Screen.OnActivationListener onActivationListener) {
//        Iterator var2 = this.onActivationListenerList.iterator();
//        do {
//            if (!var2.hasNext()) {
//                return;
//            }
//        } while(!((Screen.OnActivationListener)var2.next()).equals(onActivationListener));
//
//        var2.remove();
//    }


    public static Intent getLaunchIntentForPackage(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getLaunchIntentForPackage(context.getPackageName());
    }

    public static int getApplicationIconId(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return applicationInfo.icon;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
