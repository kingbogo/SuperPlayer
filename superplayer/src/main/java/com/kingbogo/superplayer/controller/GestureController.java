package com.kingbogo.superplayer.controller;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.SuperLogUtil;

/**
 * <p>
 * 手势控制器
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/10
 */
public abstract class GestureController extends BaseController {
    
    private static final String TAG = "GestureController";
    
    private GestureDetector mGestureDetector;
//    private SuperGestureView mGestureView;
    
    private AudioManager mAudioManager;
    
    private int mStreamVolume;
    private float mBrightness;
    private int mPosition;
    private boolean mNeedSeek;
    
    protected boolean mIsNeedGesture;
    
    public GestureController(@NonNull Context context) {
        super(context);
    }
    
    public GestureController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public GestureController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void initView() {
        super.initView();
//        mGestureView = new SuperGestureView(getContext());
//        mGestureView.setVisibility(GONE);
//        addView(mGestureView);
//        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!mGestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
//            if (mGestureView.getVisibility() == VISIBLE) {
//                mGestureView.setVisibility(GONE);
//            }
//            if (mNeedSeek) {
//                mPlayerControl.onCtrlSeekTo(mPosition);
//                mNeedSeek = false;
//            }
//        }
//        return super.onTouchEvent(event);
//    }
    
    /**
     * MyGestureListener
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        
        private boolean mFirstTouch;
        private boolean mChangePosition;
        private boolean mChangeBrightness;
        private boolean mChangeVolume;
        
        @Override
        public boolean onDown(MotionEvent e) {
            SuperLogUtil.v(TAG, "==> onDown()...");
            boolean isFix = mCurrentPlayerState == SuperPlayerState.STOPPED || mCurrentPlayerState == SuperPlayerState.PAUSED
                    || mCurrentPlayerState == SuperPlayerState.COMPLETED;
            if (mIsShowing) {
                //if (!isFix) {
                hideController();
                //}
            } else {
                showController(false, !isFix);
            }
            return super.onDown(e);
        }

//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            SuperLogUtil.v(TAG, "==> onSingleTapConfirmed()...");
//            if (mIsShowing) {
//                hideController();
//            } else {
//                showController(false, true);
//            }
//            return true;
//        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            
            
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            
            
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // SuperLogUtil.v(TAG, "==> onDoubleTap()...");
            
            return super.onDoubleTap(e);
        }
    }
    
}
