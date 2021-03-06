package com.kingbogo.superplayer.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingbogo.superplayer.R;
import com.kingbogo.superplayer.common.IPlayerControl;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.util.SuperAnimHelper;
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
            hideController(true);
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
     * @param isAnim      是否动画
     * @param reset       是否重置
     * @param postFadeOut 是否发送FadeOut
     */
    @CallSuper
    protected void showController(boolean isAnim, boolean reset, boolean postFadeOut) {
        mIsShowing = true;
    }

    /**
     * 隐藏控制器
     *
     * @param isAnim 是否动画
     */
    @CallSuper
    protected void hideController(boolean isAnim) {
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

    // ------------------------------------------------------------------------ @ anim

    /**
     * 动画显示/隐藏上边的 View
     *
     * @param topView 上边的 View
     * @param isShow  显示/隐藏
     */
    protected void showHideTopWithAnim(final View topView, boolean isShow) {
        if (isShow) {
            topView.setVisibility(VISIBLE);
            SuperAnimHelper.showTop(topView, null);
        } else {
            SuperAnimHelper.hideTop(topView, new SuperAnimHelper.SuperAnimActionListener() {
                @Override
                public void onSuperAnimEnd() {
                    topView.setVisibility(GONE);
                }
            });
        }
    }

    /**
     * 动画显示/隐藏下边的 View
     *
     * @param bottomView 下边的 View
     * @param isShow     显示/隐藏
     */
    protected void showHideBottomWithAnim(final View bottomView, boolean isShow) {
        if (isShow) {
            bottomView.setVisibility(VISIBLE);
            SuperAnimHelper.showBottom(bottomView, null);
        } else {
            SuperAnimHelper.hideBottom(bottomView, new SuperAnimHelper.SuperAnimActionListener() {
                @Override
                public void onSuperAnimEnd() {
                    bottomView.setVisibility(GONE);
                }
            });
        }
    }

    /**
     * 动画显示/隐藏左边的 View
     *
     * @param leftView 左边的 View
     * @param isShow   显示/隐藏
     */
    protected void showHideLeftWithAnim(final View leftView, boolean isShow) {
        if (isShow) {
            leftView.setVisibility(VISIBLE);
            SuperAnimHelper.showLeft(leftView, null);
        } else {
            SuperAnimHelper.hideLeft(leftView, new SuperAnimHelper.SuperAnimActionListener() {
                @Override
                public void onSuperAnimEnd() {
                    leftView.setVisibility(GONE);
                }
            });
        }
    }

    /**
     * 动画显示/隐藏右边的 View
     *
     * @param rightView 右边的 View
     * @param isShow    显示/隐藏
     */
    protected void showHideRightWithAnim(final View rightView, boolean isShow) {
        if (isShow) {
            rightView.setVisibility(VISIBLE);
            SuperAnimHelper.showRight(rightView, null);
        } else {
            SuperAnimHelper.hideRight(rightView, new SuperAnimHelper.SuperAnimActionListener() {
                @Override
                public void onSuperAnimEnd() {
                    rightView.setVisibility(GONE);
                }
            });
        }
    }

    /**
     * 动画显示/隐藏上边的 View
     *
     * @param centerView 中间的 View
     * @param isShow     显示/隐藏
     */
    protected void showHideCenterWithAnim(final View centerView, boolean isShow) {
        if (isShow) {
            centerView.setVisibility(VISIBLE);
            SuperAnimHelper.showCenter(centerView, null);
        } else {
            SuperAnimHelper.hideCenter(centerView, new SuperAnimHelper.SuperAnimActionListener() {
                @Override
                public void onSuperAnimEnd() {
                    centerView.setVisibility(GONE);
                }
            });
        }
    }


    protected void postFadeOutRunnable() {
        removeFaceOutRunnable();
        if (mDefaultTimeout != 0) {
            postDelayed(getFadeOutRunnable(), mDefaultTimeout);
        }
    }

    protected void removeFaceOutRunnable() {
        removeCallbacks(getFadeOutRunnable());
    }


    // ------------------------------------------------------------------------ @ others

    private class FadeOutRunnable implements Runnable {
        @Override
        public void run() {
            hideController(true);
        }
    }

}
