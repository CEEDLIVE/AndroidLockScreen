package com.ceedlive.androidlockscreen;

import android.app.Application;
import android.content.Context;
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

        Log.i("CEEDLIVE", "CustomApplication > onCreate");

        Screen.init(this);

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
        Log.i("CEEDLIVE", "CustomApplication > attachBaseContext");
    }

    private void clearApplicationCache(File cacheDir) {
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
}
