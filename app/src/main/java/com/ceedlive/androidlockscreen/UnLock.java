package com.ceedlive.androidlockscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;


public class UnLock implements View.OnTouchListener  {
    private float firstTouchX = 0f;
    private float layoutPrevX = 0f;
    private float lastLayoutX = 0f;
    private float layoutInPrevX = 0f;
    private boolean isLockOpen = false;
    private int touchMoveX = 0;

    private Context mContext;
    private ConstraintLayout mLockScreenView;

    public UnLock(Context context, ConstraintLayout lockScreenView) {
        mContext = context;
        mLockScreenView = lockScreenView;
    }

    void onFinish() {

    }

    void onTouched() {

    }

    void onMoved(int x) {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch ( event.getAction() ) {
            // 처음 눌렸을 때
            case MotionEvent.ACTION_DOWN: {
                firstTouchX = event.getX() + 5f;
                layoutPrevX = mLockScreenView.getX();
                isLockOpen = true;

                onTouched();
                break;
            } // end case
            // 누르고 움직였을 때
            case MotionEvent.ACTION_MOVE: {
                if (isLockOpen) {
                    touchMoveX = (int) (event.getX() - firstTouchX);
                    if (mLockScreenView.getX() >= 0) {
                        mLockScreenView.setX( (layoutPrevX + touchMoveX) );
                        if (mLockScreenView.getX() < 0) {
                            mLockScreenView.setX(0f);
                        }

                        lastLayoutX = mLockScreenView.getX();
                        onMoved(touchMoveX / 10);
                    }
                } else {
                    return false;
                }
                break;
            } // end case
            // 누르고 있던 것을 떼었을 때
            case MotionEvent.ACTION_UP: {
                // 롱터치 탐지 로직 마무리
                if (isLockOpen) {
                    mLockScreenView.setX(lastLayoutX);
                    mLockScreenView.setY(0f);
                }
                isLockOpen = false;
                firstTouchX = 0f;
                layoutPrevX = 0f;
                layoutInPrevX = 0f;
                touchMoveX = 0;
                lastLayoutX = 0f;
                break;
            } // end case
        } // end switch

        return false;
    }
}
