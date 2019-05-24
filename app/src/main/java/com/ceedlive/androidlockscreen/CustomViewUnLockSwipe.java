package com.ceedlive.androidlockscreen;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class CustomViewUnLockSwipe extends RelativeLayout {
    private OnUnlockListener listenerRight;

    private FrameLayout slideButton;
    private ImageView lockedImage;

    private int thumbWidth = 0;
    private boolean sliding = false;
    private int sliderPosition = 0;
    private int initialSliderPosition = 0;
    private float initialSlidingX = 0f;


    public CustomViewUnLockSwipe(Context context) {
        super(context);
        init(context, null);
    }

    public CustomViewUnLockSwipe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomViewUnLockSwipe(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    void setOnUnlockListenerRight(OnUnlockListener listener) {
        this.listenerRight = listener;
    }

    void reset() {
        lockedImage.setImageResource(R.drawable.ic_aaaa);

        slideButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                CustomLog.info("CustomViewUnLockSwipe > reset: run");

                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);

                final ValueAnimator valueAnimator = ValueAnimator.ofInt(params.leftMargin, 0);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        CustomLog.info("CustomViewUnLockSwipe > reset: run @Override onAnimationUpdate");
                        params.leftMargin = (int) valueAnimator.getAnimatedValue();
                        requestLayout();
                    }
                });
                valueAnimator.setDuration(300);
                valueAnimator.start();
            }
        }, 10);

    }

    private void init(final Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.view_unlock, this, true);

        // Retrieve layout elements
        slideButton = view.findViewById(R.id.swipeButton);
        lockedImage = view.findViewById(R.id.lockedImage);

        view.findViewById(R.id.swipeButton).setVisibility(View.VISIBLE);
        view.findViewById(R.id.lockedImage).setVisibility(View.VISIBLE);

        // Get padding
        //thumbWidth = dpToPx(120); // 60dp + 2*10dp

        if (getViewTreeObserver().isAlive()) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    slideButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CustomLog.info("CustomViewUnLockSwipe > init: onGlobalLayout > run");

                            thumbWidth = slideButton.getWidth();

                            if (view.getWidth() == 0) {
                                view.findViewById(R.id.swipeButton).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.lockedImage).setVisibility(View.INVISIBLE);
                                init(context, null);
                            }

                            sliderPosition = 0;
                            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                                    (ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT);

                            params.setMargins(0, 0, 0, 0);
                            setLayoutParams(params);
                        }
                    }, 10);
                }
            });
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch ( event.getAction() ) {
            // 처음 눌렸을 때
            case MotionEvent.ACTION_DOWN: {
                if (event.getX() >= sliderPosition && event.getX() < sliderPosition + thumbWidth) {
                    lockedImage.setImageResource(R.drawable.ic_aaaa);
                    sliding = true;
                    initialSlidingX = event.getX();
                    initialSliderPosition = sliderPosition;
                }
                break;
            } // end case
            // 누르고 움직였을 때
            case MotionEvent.ACTION_MOVE: {
                sliderPosition = (int) (initialSliderPosition + (event.getX() - initialSlidingX));
                if (sliderPosition <= 0) {
                    sliderPosition = 0;
                }

                if (sliderPosition >= getMeasuredWidth() - (thumbWidth + 20)) {
                    sliderPosition =  getMeasuredWidth() - (thumbWidth + 20);
                } else {
                    int max = getMeasuredWidth() - thumbWidth;
                    int progress = (int) (sliderPosition * 100 / (max * 1.0f));
                }
                CustomLog.error("MotionEvent.ACTION_MOVE - sliderPosition: " + sliderPosition);
                setMarginLeft(sliderPosition);
                break;
            } // end case
            // 누르고 있던 것을 떼었을 때
            case MotionEvent.ACTION_UP: {
                // 롱터치 탐지 로직 마무리
                if (sliderPosition >= getMeasuredWidth() - (thumbWidth + 20)) {
                    if (listenerRight != null) {
                        listenerRight.onUnlock();
                    }
                } else {
                    sliding = false;
                    sliderPosition = 0;
                    reset();
                }
                break;
            }
            case MotionEvent.ACTION_OUTSIDE: {
                // 롱터치 탐지 로직 마무리
                if (sliderPosition >= getMeasuredWidth() - (thumbWidth + 20)) {
                    if (listenerRight != null) {
                        listenerRight.onUnlock();
                    }
                } else {
                    sliding = false;
                    sliderPosition = 0;
                    reset();
                }
                break;
            } // end case
        } // end switch

        // true 를 반환하여 더 이상의 이벤트 처리가 이루어지지 않도록 완료한다.
        return true;
    }

    private void setMarginLeft(final int margin) {
        slideButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                CustomLog.info("CustomViewUnLockSwipe > setMarginLeft: run - margin " + margin);

                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);

                params.setMargins(margin, 0, 0, 0);
                setLayoutParams(params);
            }
        }, 10);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    interface OnUnlockListener {
        void onUnlock();
    }
}
