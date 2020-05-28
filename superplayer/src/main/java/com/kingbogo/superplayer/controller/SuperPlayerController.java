package com.kingbogo.superplayer.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingbogo.superplayer.R;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.SuperLogUtil;

/**
 * <p>
 * 超级播放器的控制器
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/31
 */
public class SuperPlayerController extends GestureController implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "SuperPlayerController";

    // ------------------------------------------ @ Top

    private LinearLayout mTopLl;
    private ImageView mTopBackIv;
    private TextView mTopTitleTv;

    // ------------------------------------------ @ Bottom

    private LinearLayout mBottomLl;
    private ImageView mBottomPlayIv;
    private TextView mBottomCurrentTimeTv;
    private SeekBar mBottomSeekBar;
    private TextView mBottomTotalTimeTv;
    private ImageView mBottomFullIv;
    private ProgressBar mBottomProgress;

    // ------------------------------------------ @ Center

    private FrameLayout mCenterFl;
    private ImageView mCenterPlayIv;
    private ProgressBar mCenterLoadingPb;

    // ------------------------------------------ @ Left

    private ImageView mLeftLockIv;

    // ------------------------------------------ @ Right


    // ------------------------------------------ others

    private boolean mIsTrackingTouch;

    // ------------------------------------------ about child use

    protected boolean mIsNeedShowLock = true;
    protected boolean mIsNeedShowFull = true;
    protected boolean mIsNeedShowBottomProgress = true;


    public SuperPlayerController(@NonNull Context context) {
        super(context);
    }

    public SuperPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperPlayerController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // ------------------------------------------------------------------- @ BaseController


    @Override
    protected void initView() {
        SuperLogUtil.d(TAG, "_initView()...");
        super.initView();
        // Top
        mTopLl = mRootView.findViewById(R.id.super_ctrl_top_ll);
        mTopBackIv = mRootView.findViewById(R.id.super_ctrl_top_back_iv);
        mTopBackIv.setOnClickListener(this);
        mTopTitleTv = mRootView.findViewById(R.id.super_ctrl_top_title_tv);

        // Bottom
        mBottomLl = mRootView.findViewById(R.id.super_ctrl_bottom_ll);
        mBottomPlayIv = mRootView.findViewById(R.id.super_ctrl_bottom_play_iv);
        mBottomPlayIv.setOnClickListener(this);
        mBottomCurrentTimeTv = mRootView.findViewById(R.id.super_ctrl_bottom_current_time_tv);
        mBottomSeekBar = mRootView.findViewById(R.id.super_ctrl_bottom_seek_bar);
        mBottomSeekBar.setPadding(0, 0, 0, 0);
        mBottomSeekBar.setThumbOffset(0);
        mBottomSeekBar.setOnSeekBarChangeListener(this);
        mBottomTotalTimeTv = mRootView.findViewById(R.id.super_ctrl_bottom_total_time_tv);
        mBottomFullIv = mRootView.findViewById(R.id.super_ctrl_bottom_full_screen_iv);
        mBottomFullIv.setOnClickListener(this);
        mBottomProgress = mRootView.findViewById(R.id.super_ctrl_bottom_progress);

        // Center
        mCenterFl = mRootView.findViewById(R.id.super_ctrl_center_fl);
        mCenterPlayIv = mRootView.findViewById(R.id.super_ctrl_center_play_iv);
        mCenterPlayIv.setOnClickListener(this);
        mCenterLoadingPb = mRootView.findViewById(R.id.super_ctrl_center_loading_pb);

        // Left
        mLeftLockIv = mRootView.findViewById(R.id.super_ctrl_left_lock_iv);
    }

    @Override
    protected int onBaseGetLayoutId() {
        return R.layout.view_super_controller;
    }

    @Override
    protected void showController(boolean isAnim, boolean reset, boolean postFadeOut) {
        super.showController(isAnim, reset, postFadeOut);
        SuperLogUtil.d(TAG, "_showController(), reset: " + reset + ", postFadeOut: " + postFadeOut);
        // top
        showTopViews();
        // bottom
        showBottomViews(reset);
        // center
        // hideCenterViews();
        showCenterViews();
        // left
        showLeftViews(reset);
        // face out
        if (postFadeOut) {
            postFadeOutRunnable();
        } else {
            removeFaceOutRunnable();
        }
    }

    @Override
    protected void hideController(boolean isAnim) {
        super.hideController(isAnim);
        SuperLogUtil.d(TAG, "_hideController()...");
        // top
        hideTopViews();
        // bottom
        hideBottomViews();
        // center
        hideCenterViews();
        // left
        hideLeftViews();
    }

    @Override
    protected void onBasePlayerProgressChanged() {
        SuperLogUtil.d(TAG, "_playerProgressChanged(), currentDuration: " + mPlayerControl.onCtrlGetCurrentDuration());
        // 更新进度
        if (mPlayerControl == null || mIsTrackingTouch) {
            return;
        }
        int currentDuration = (int) mPlayerControl.onCtrlGetCurrentDuration();
        int totalDuration = (int) mPlayerControl.onCtrlGetTotalDuration();
        // seekBar
        if (totalDuration > 0) {
            mBottomSeekBar.setEnabled(true);
            int pos = (int) (currentDuration * 1.0 / totalDuration * mBottomSeekBar.getMax());
            mBottomSeekBar.setProgress(pos);
            mBottomProgress.setProgress(pos);
        } else {
            mBottomSeekBar.setEnabled(false);
        }
        int bufferedPercent = mPlayerControl.onCtrlGetBufferedPercentage();
        SuperLogUtil.d(TAG, "_playerProgressChanged(), bufferedPercent: " + bufferedPercent);
        if (bufferedPercent >= 95) {
            //解决缓冲进度不能100%问题
            mBottomSeekBar.setSecondaryProgress(mBottomSeekBar.getMax());
            mBottomProgress.setSecondaryProgress(mBottomProgress.getMax());
        } else {
            mBottomSeekBar.setSecondaryProgress(bufferedPercent);
            mBottomProgress.setSecondaryProgress(bufferedPercent);
        }
        // textView
        mBottomCurrentTimeTv.setText(stringForTime(currentDuration));
        mBottomTotalTimeTv.setText(stringForTime(totalDuration));
    }

    @Override
    protected void onBasePlayerStateChanged(SuperPlayerState playerState) {
        SuperLogUtil.d(TAG, "_playerStateChanged(), playerState: " + playerState);
        switch (playerState) {
            case IDLE:
                mIsLocked = false;
                break;
            case PREPARING:
                showController(false, true, true);
                showCenterViews(false, true);
                break;
            case PREPARED:
                if (mIsNeedShowBottomProgress) {
                    mBottomProgress.setVisibility(VISIBLE);
                    mBottomProgress.setProgress(0);
                    mBottomProgress.setSecondaryProgress(0);
                }
                break;

            case BUFFERING:
                showCenterViews(false, true);
                break;
            case BUFFERED:
                showController(false, false, true);
                showPlayViews(false);
                break;

            case PLAYING:
                showPlayViews(false);
                break;
            case PAUSED:
                showPlayViews(true);
                showController(false, false, false);
                break;

            case COMPLETED:
                showController(false, false, false);
                mPlayerControl.onCtrlSetLock(false);
                break;

            case STOPPED:
            case ERROR:
                showController(false, false, false);
                showCenterViews(true, false);
                showPlayViews(true);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onBaseDisplayModeChanged(int displayMode) {
        SuperLogUtil.d(TAG, "_displayModeChanged(), displayMode: " + displayMode);
        if (displayMode == SuperConstants.DISPLAY_MODE_NORMAL) {
            // TODO 正常模式


        } else if (displayMode == SuperConstants.DISPLAY_MODE_FULLSCREEN) {
            // TODO 全屏模式

        }
    }

    @Override
    protected void onBasePlayerSetTitle(String title) {
        SuperLogUtil.d(TAG, "_playerSetTitle(), title: " + title);
        if (title != null) {
            mTopTitleTv.setText(title);
        } else {
            mTopTitleTv.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.super_ctrl_top_back_iv) {
            // TODO top: 返回

        } else if (id == R.id.super_ctrl_bottom_play_iv || id == R.id.super_ctrl_center_play_iv) {
            SuperLogUtil.d(TAG, "_onClick(), you click Play Button...");
            if (mPlayerControl.onCtrlIsPlaying()) {
                // 正在播放，则暂停
                showPlayViews(true);
                mPlayerControl.onCtrlPause();
            } else {
                // 其它则播放
                if (mCurrentPlayerState == SuperPlayerState.PAUSED) {
                    // 继续播放
                    showController(false, false, true);
                    mPlayerControl.onCtrlResume();
                } else {
                    // 重播
                    mPlayerControl.onCtrlReplay();
                }
            }
        } else if (id == R.id.super_ctrl_bottom_full_screen_iv) {
            // TODO bottom: 全屏

        }
    }

    // ------------------------------------------------------------------- @ OnSeekBarChangeListener

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SuperLogUtil.d(TAG, "_onProgressChanged(), progress: " + progress + ", fromUser: " + fromUser);
        if (!fromUser) {
            return;
        }
        long totalDuration = mPlayerControl.onCtrlGetTotalDuration();
        long seekPosition = (totalDuration * progress) / seekBar.getMax();
        SuperLogUtil.d(TAG, "_onProgressChanged(), totalDuration: " + totalDuration + ", seekPosition: " + seekPosition);
        mBottomCurrentTimeTv.setText(stringForTime((int) seekPosition));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        SuperLogUtil.d(TAG, "_onStartTrackingTouch()...");
        mIsTrackingTouch = true;
        removeFaceOutRunnable();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SuperLogUtil.d(TAG, "_onStopTrackingTouch()... seekBar.getProgress(): " + seekBar.getProgress() + ", seekBar.getMax(): " + seekBar.getMax());
        long totalDuration = mPlayerControl.onCtrlGetTotalDuration();
        long seekPosition = (totalDuration * seekBar.getProgress()) / seekBar.getMax();
        SuperLogUtil.d(TAG, "_onStopTrackingTouch(), totalDuration: " + totalDuration + ", seekPosition: " + seekPosition);
        mPlayerControl.onCtrlSeekTo(seekPosition);
        mIsTrackingTouch = false;
        showController(false, false, true);
    }

    // ------------------------------------------------------------------- @ protected

    /**
     * 显示顶部Views
     */
    protected void showTopViews() {
        mTopLl.setVisibility(VISIBLE);
        mTopTitleTv.setVisibility(VISIBLE);
        if (mDisplayMode == SuperConstants.DISPLAY_MODE_NORMAL) {
            mTopBackIv.setVisibility(GONE);
        } else if (mDisplayMode == SuperConstants.DISPLAY_MODE_FULLSCREEN) {
            mTopBackIv.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏顶部Views
     */
    protected void hideTopViews() {
        mTopLl.setVisibility(GONE);
    }

    /**
     * 显示底部Views
     *
     * @param reset 是否重置
     */
    protected void showBottomViews(boolean reset) {
        mBottomLl.setVisibility(VISIBLE);
        mBottomPlayIv.setVisibility(VISIBLE);
        mBottomCurrentTimeTv.setVisibility(VISIBLE);
        mBottomSeekBar.setVisibility(VISIBLE);
        mBottomTotalTimeTv.setVisibility(VISIBLE);
        mBottomFullIv.setVisibility(mIsNeedShowFull ? VISIBLE : GONE);
        mBottomProgress.setVisibility(mIsNeedShowBottomProgress ? VISIBLE : GONE);
        // reset
        if (reset) {
            mBottomPlayIv.setSelected(false);
            mBottomCurrentTimeTv.setText(TIME_INIT);
            mBottomSeekBar.setEnabled(false);
            mBottomSeekBar.setProgress(0);
            mBottomSeekBar.setSecondaryProgress(0);
            mBottomTotalTimeTv.setText(TIME_INIT);
            if (mIsNeedShowFull) {
                mBottomFullIv.setSelected(false);
            }
            if (mIsNeedShowBottomProgress) {
                mBottomProgress.setProgress(0);
                mBottomProgress.setSecondaryProgress(0);
            }
        } else {
            boolean enable = mPlayerControl.onCtrlIsPlaying() || mCurrentPlayerState == SuperPlayerState.PAUSED
                    || mCurrentPlayerState == SuperPlayerState.COMPLETED;
            // enable
            if (enable) {
                mBottomSeekBar.setEnabled(true);
            } else {
                mBottomSeekBar.setEnabled(false);
            }
        }
    }

    /**
     * 隐藏底部Views
     */
    protected void hideBottomViews() {
        mBottomLl.setVisibility(GONE);
    }

    /**
     * 显示中间Views
     *
     * @param isShowPlay    是否显示播放按钮
     * @param isShowLoading 是否显示进度加载
     */
    protected void showCenterViews(boolean isShowPlay, boolean isShowLoading) {
        mCenterFl.setVisibility(VISIBLE);
        mCenterPlayIv.setVisibility(isShowPlay ? VISIBLE : GONE);
        mCenterLoadingPb.setVisibility(isShowLoading ? VISIBLE : GONE);
    }

    /**
     * 显示中间Views
     */
    protected void showCenterViews() {
        mCenterFl.setVisibility(VISIBLE);
    }

    /**
     * 隐藏中间Views
     */
    protected void hideCenterViews() {
        mCenterFl.setVisibility(GONE);
    }

    /**
     * 显示左边Views
     *
     * @param reset 是否重置
     */
    protected void showLeftViews(boolean reset) {
        mLeftLockIv.setVisibility(mIsNeedShowLock ? VISIBLE : GONE);
        if (reset) {
            if (mIsNeedShowLock) {
                mLeftLockIv.setSelected(false);
            }
        }
    }

    /**
     * 隐藏左边Views
     */
    protected void hideLeftViews() {
        mLeftLockIv.setVisibility(GONE);
    }

    /**
     * 显示播放Views
     *
     * @param isPlayIcon 是否播放图标
     */
    protected void showPlayViews(boolean isPlayIcon) {
        mBottomPlayIv.setVisibility(VISIBLE);
        mBottomPlayIv.setSelected(!isPlayIcon);
        showCenterViews(true, false);
        mCenterPlayIv.setSelected(!isPlayIcon);
    }

}
