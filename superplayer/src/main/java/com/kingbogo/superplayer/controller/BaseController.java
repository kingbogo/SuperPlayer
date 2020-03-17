package com.kingbogo.superplayer.controller;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kingbogo.superplayer.R;
import com.kingbogo.superplayer.common.IPlayerControl;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.view.SuperIconView;
import com.kingbogo.superplayer.view.SuperTipsView;

import java.util.Formatter;
import java.util.Locale;

/**
 * <p>
 * 控制器基类
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/10
 */
public abstract class BaseController extends FrameLayout implements View.OnClickListener {
    
    public static final String TIME_INIT = "00:00";
    
    protected View mRootView;
    protected IPlayerControl mPlayerControl;
    
    /**
     * 是否已上锁
     */
    protected boolean mIsLocked;
    protected int mDefaultTimeout = 3800;
    
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    
    private SuperTipsView mTipsView;
    private SuperIconView mIconView;
    private FadeOutRunnable mFadeOutRunnable;
    
    protected SuperPlayerState mCurrentPlayerState;
    protected int mDisplayMode = SuperConstants.DISPLAY_MODE_NORMAL;
    protected boolean mIsShowing;
    
    public BaseController(@NonNull Context context) {
        this(context, null);
    }
    
    public BaseController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public BaseController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    
    protected void initView() {
        setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRootView = LayoutInflater.from(getContext()).inflate(onBaseGetLayoutId(), this);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mTipsView = new SuperTipsView(getContext());
        mIconView = new SuperIconView(getContext());
    }
    
    // ------------------------------------------------------------------------ @ abstract
    
    /**
     * 布局文件
     *
     * @return 布局
     */
    protected abstract int onBaseGetLayoutId();
    
    /**
     * 播放器进度变化
     */
    protected abstract void onBasePlayerProgressChanged();
    
    /**
     * 播放器状态变化
     *
     * @param playerState 播放器状态
     */
    protected abstract void onBasePlayerStateChanged(SuperPlayerState playerState);
    
    /**
     * 显示模式变化
     *
     * @param playerMode 显示模式
     */
    protected abstract void onBaseDisplayModeChanged(int playerMode);
    
    /**
     * 设置标题
     *
     * @param title 标题
     */
    protected abstract void onBasePlayerSetTitle(String title);
    
    // ------------------------------------------------------------------------ @ public
    
    /**
     * 外部：设置控制器
     */
    public void setPlayerControl(IPlayerControl playerControl) {
        mPlayerControl = playerControl;
    }
    
    /**
     * 外部：状态变化
     */
    public void onPlayerStateChanged(SuperPlayerState playerState) {
        mCurrentPlayerState = playerState;
        onBasePlayerStateChanged(playerState);
        if (playerState == SuperPlayerState.ERROR) {
            showTipsView(getResources().getString(R.string.super_tips_play_error), getResources().getString(R.string.super_tips_replay));
        } else {
            if (playerState == SuperPlayerState.COMPLETED) {
                showReplayIconView();
            } else {
                hideReplayIconView();
            }
            hideTipsView();
        }
    }
    
    /**
     * 外部：进度变化
     */
    public void onPlayerProgressChanged() {
        onBasePlayerProgressChanged();
    }
    
    /**
     * 外部：显示模式变化
     */
    public void onPlayerDisplayModeChanged(int displayMode) {
        mDisplayMode = displayMode;
        onBaseDisplayModeChanged(displayMode);
    }
    
    /**
     * 外部：设置标题
     */
    public void onPlayerSetTitle(String title) {
        onBasePlayerSetTitle(title);
    }
    
    
    // ------------------------------------------------------------------------ @ protected
    
    /**
     * 显示控制器
     *
     * @param reset       是否重置
     * @param postFadeOut 是否发送FadeOut
     */
    @CallSuper
    protected void showController(boolean reset, boolean postFadeOut) {
        mIsShowing = true;
    }
    
    /**
     * 隐藏控制器
     */
    @CallSuper
    protected void hideController() {
        mIsShowing = false;
    }
    
    /**
     * 显示 tips (比如播放错误)
     */
    protected void showTipsView(String tips, String buttonText) {
        removeView(mTipsView);
        if (!CheckUtil.isEmpty(buttonText)) {
            mTipsView.setTips(tips);
            mTipsView.setButtonTextAndListener(buttonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideTipsView();
                    mPlayerControl.onCtrlReplay();
                }
            });
        } else {
            mTipsView.setTipsAndHideButton(tips);
        }
        addView(mTipsView);
    }
    
    /**
     * 隐藏 tips
     */
    protected void hideTipsView() {
        removeView(mTipsView);
    }
    
    /**
     * 显示 重播按钮
     */
    protected void showReplayIconView() {
        removeView(mIconView);
        mIconView.setIcon(R.drawable.super_ic_action_replay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReplayIconView();
                mPlayerControl.onCtrlReplay();
            }
        });
        addView(mIconView);
    }
    
    /**
     * 隐藏 重播按钮
     */
    protected void hideReplayIconView() {
        removeView(mIconView);
    }
    
    protected FadeOutRunnable getFadeOutRunnable() {
        if (mFadeOutRunnable == null) {
            mFadeOutRunnable = new FadeOutRunnable();
        }
        return mFadeOutRunnable;
    }
    
    /**
     * 格式化时间
     */
    protected String stringForTime(long timeMs) {
        long totalSeconds = timeMs / 1000L;
        
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    
    // ------------------------------------------------------------------------ @ others
    
    private class FadeOutRunnable implements Runnable {
        @Override
        public void run() {
            hideController();
        }
    }
    
}
