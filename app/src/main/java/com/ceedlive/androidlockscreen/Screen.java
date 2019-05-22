package com.ceedlive.androidlockscreen;

import android.content.Context;
import android.util.Log;

public class Screen {

    private final String LOG_TAG = "Screen";
    private Context mContext;
    private static Screen sScreen;


    private Screen(Context context) {
        this.mContext = context;
    }

    public static Screen getInstance() {
        if (sScreen != null) {
            return sScreen;
        } else {
            throw new RuntimeException("Screen class not correctly initialized. Please call Screen.init in the Application class onCreate.");
        }
    }

    public static void init(Context context) {
        Log.i("CEEDLIVE", "Screen > init(Context context): 진입");
        Locker.init(context);
        sScreen = new Screen(context);
    }

    public void launch() {
        // TODO 전처리
        Log.i("CEEDLIVE", "Screen > launch: 진입");
        Locker.getInstance().launch();
    }

    void activate() {
        Log.i("CEEDLIVE", "Screen > activate: 진입");
        // TODO condition
        Locker.getInstance().start();
    }

    void deactivate() {
        Log.i("CEEDLIVE", "Screen > deactivate: 진입");
        // TODO condition
        Locker.getInstance().stop();
    }

    void showScreen() {
        Log.i("CEEDLIVE", "Screen > showScreen: 진입");
        Locker.getInstance().showLocker();
    }

    void login() {
        Log.i("CEEDLIVE", "Screen > login: 진입");
    }

    void logout() {
        Log.i("CEEDLIVE", "Screen > logout: 진입");
    }

}
