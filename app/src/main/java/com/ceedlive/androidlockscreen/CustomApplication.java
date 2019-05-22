package com.ceedlive.androidlockscreen;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;

import java.io.File;

public class CustomApplication extends Application {

    // Application Class
    // 어플리케이션 컴포넌트들 사이에서 공동으로 멤버들을 사용할 수 있게 해 주는 편리한 공유 클래스를 제공합니다.

    // Application 클래스에 static 으로 선언한 전역변수는 언제 어디서나 접근, 수정이 가능합니다.
    // Application 클래스에 작성한 메서드는 언제 어디서나 호출이 가능합니다.

    // 사용방법
    // 1) Application Class 를 상속받는 Class 를 만든다.
    // 2) AndroidManifest.xml 에 Application Class name 을 추가한다.
    // 3) 어플리케이션 내의 컴포넌트들 사이에서 context 를 이용한 접근이 가능하다. (Data 공유)

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO

        CustomLog.info("CustomApplication > onCreate");

        Screen.init(this);
        createDefaultNotificationChannel();

        // MobileAds 클래스의 정적 메소드 initialize()에 제공된 두 번째 인수는 Ad Mob에 가입할 때 얻은 Ad Mob 애플리케이션 ID 여야 합니다.
        // 이 경우 데모 용도로 Google에서 제공하는 공개 애플리케이션 ID를 사용하고 있습니다.
        // This method should be called as early as possible, and only once per application launch
        MobileAds.initialize(this, getString(R.string.admob_app_id_test));

        // init
        // listener
            // 잠금화면을 사용함으로써 얻을 수 있는 이점, 보상
        // config
        // etc
            // AdMob
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CustomLog.info("CustomApplication > attachBaseContext");
    }

    private void clearApplicationCache(File cacheDir) {
        CustomLog.info("CustomApplication > clearApplicationCache");
        File dir = cacheDir;
        if (dir == null) {
            dir = cacheDir;
        }
        if (dir == null) {
            return;
        }
        File[] children = dir.listFiles();
        try {
            for (int i=0; i<children.length; i++) {
                if ( children[i].isDirectory() ) {
                    clearApplicationCache( children[i] );
                } else {
                    children[i].delete();
                }
            }
        } catch (Exception e) {
            Log.e("e.toString()", e.toString());
        }
    }

    // @RequiresApi 는 해당 함수가 해당 버전에서만 호출 되어야 한다는 것을 IDE 에 알리는 역할을 합니다.
    // 따라서 해당 어노테이션이 지정된 함수를 호출 하는 경우 에러가 발생하게 됩니다.
    // 버전에 따라 Warning 이 발생 하는 경우도 있을 수 있습니다.

    // @TargetApi 는 해당 함수가 해당 버전에서만 호출 된다는 것을 IDE 에 알리는 역할을 합니다.
    // 따라서 호출 하는 쪽에서는 어떠한 피드백도 없는 것을 볼 수 있습니다.
    @TargetApi(Build.VERSION_CODES.O)
    private void createDefaultNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String id = "LockScreenService";
            final String name = "LockScreen Service";
            final int priority = NotificationManager.IMPORTANCE_MIN;
            final String description = "LockScreen Service";

            final NotificationChannel notificationChannel = new NotificationChannel(id, name, priority);
            notificationChannel.setShowBadge(false);
            notificationChannel.setDescription(description);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        }
    }

}
