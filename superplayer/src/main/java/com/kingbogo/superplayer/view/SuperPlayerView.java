package com.kingbogo.superplayer.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kingbogo.superplayer.R;
import com.kingbogo.superplayer.common.IMediaPlayer;
import com.kingbogo.superplayer.common.IRenderView;
import com.kingbogo.superplayer.listener.MediaPlayerListener;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.Constants;
import com.kingbogo.superplayer.model.PlayerState;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.player.SuperPlayer;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.util.LogUtil;

/**
 * <p>
 * 超级播放器View
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/12
 */
public class SuperPlayerView extends FrameLayout implements MediaPlayerListener {

    private FrameLayout mPlayerContainerView;
    private ImageView mHolderIv;

    private IMediaPlayer mMediaPlayer;
    private IRenderView mRenderView;

    private SuperPlayerModel mCurrentPlayerModel;

    private PlayerState mCurrentPlayState = PlayerState.IDLE;

    private ProgressRunnable mProgressRunnable;

    public SuperPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public SuperPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        FrameLayout rootView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_super_player, null);
        mPlayerContainerView = rootView.findViewById(R.id.super_view_container);
        mHolderIv = rootView.findViewById(R.id.super_view_holder_iv);

        removeAllViews();
        rootView.removeView(mPlayerContainerView);
        rootView.removeView(mHolderIv);

        addView(mPlayerContainerView);
        addView(mHolderIv);
    }

    // ------------------------------------------------------------- @ Public

    /**
     * 播放（点播）
     */
    public void playWithUrl(String url) {
        playWithModel(new SuperPlayerModel(url));
    }

    /**
     * 播放（点播）
     */
    public void playWithModel(SuperPlayerModel playerModel) {
        if (playerModel == null || CheckUtil.isEmpty(playerModel.url)) {
            LogUtil.w("_playWithModel(), playerModel is null OR playerModel.url is null...");
            setPlayState(PlayerState.ERROR);
            return;
        }
        mCurrentPlayerModel = playerModel;
        setVisibility(VISIBLE);
        // init
        stop(false);
        initPlayer(getContext());
        // config
        setLoop(playerModel.isLoop);
        setMute(playerModel.isMute);
        setRenderMode(playerModel.renderMode);
        // play
        mMediaPlayer.startPlay(playerModel.url);
        setPlayState(PlayerState.PREPARING);
        // progress callback
        postProgressRunnable();
    }

    /**
     * 重播
     */
    public void replay() {
        playWithModel(mCurrentPlayerModel);
    }

    /**
     * 重播：seek方式
     */
    public void replay4Seek() {
        if (mMediaPlayer != null) {
            mMediaPlayer.replay();
        }
    }

    /**
     * 设置进度。单位：ms
     */
    public void seek(long seekTime) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(seekTime);
            mMediaPlayer.resume();
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.resume();
            setPlayState(PlayerState.PLAYING);
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            setPlayState(PlayerState.PAUSED);
        }
    }

    /**
     * 停止播放
     *
     * @param isClearFrame 清除帖画面
     */
    public void stopPlay(boolean isClearFrame) {
        stop(isClearFrame);
        setPlayState(PlayerState.STOPPED);
        if (isClearFrame) {
            setVisibility(GONE);
            releaseRenderView();
        }
    }

    /**
     * 是否循环播放
     */
    public void setLoop(boolean isLoop) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLoop(isLoop);
        }
    }

    /**
     * 设置是否静音
     *
     * @param isMute 是否静音
     */
    public void setMute(boolean isMute) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(isMute ? 0.0f : 1.0f);
        }
    }

    /**
     * 设备渲染模式
     *
     * @param renderMode 渲染模式
     */
    public void setRenderMode(int renderMode) {
        if (mRenderView != null) {
            mRenderView.setRenderMode(renderMode);
        }
    }

    /**
     * 生命周期：继续
     */
    public void onResume() {
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.isPauseAfterLockScreen) {
            resume();
        }
    }

    /**
     * 生命周期：暂停
     */
    public void onPause() {
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.isPauseAfterLockScreen) {
            pause();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mCurrentPlayerModel != null) {
            mCurrentPlayerModel.setPlayerListener(null);
        }
        stop(true);
        releaseMediaPlayer();
        releaseRenderView();
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 视频总长。单位：ms
     */
    public long getTotalDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getTotalDuration();
        }
        return 0;
    }

    /**
     * 当前播放进度。单位：ms
     */
    public long getCurrentDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentDuration();
        }
        return 0;
    }

    /**
     * 缓冲进度。单位：ms
     */
    public long getBufferedDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getBufferedDuration();
        }
        return 0;
    }

    /**
     * @return 获取当前播放状态
     */
    public PlayerState getPlayState() {
        return mCurrentPlayState;
    }

    /** 显示占位图 */
    public void showHolderImage() {
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.holderImageResId != 0) {
            mHolderIv.setImageResource(mCurrentPlayerModel.holderImageResId);
            mHolderIv.setVisibility(VISIBLE);
        }
    }

    /** 显示占位图 */
    public void showHolderImage(@DrawableRes int imageResId) {
        if (imageResId != 0) {
            if (mCurrentPlayerModel != null) {
                mCurrentPlayerModel.holderImageResId = imageResId;
            }
            mHolderIv.setImageResource(imageResId);
            mHolderIv.setVisibility(VISIBLE);
        }
    }

    /** 隐藏占位图 */
    public void hideHolderImage() {
        mHolderIv.setVisibility(GONE);
    }

    /**
     * 视频宽
     */
    public int getVideoWidth() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getWidth();
        }
        return 0;
    }

    /**
     * 视频高
     */
    public int getVideoHeight() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getHeight();
        }
        return 0;
    }

    // ------------------------------------------------------------- @ protected

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            release();
        } catch (Exception e) {
        } catch (Error e) {
        }
    }

    // ------------------------------------------------------------- @ private

    /**
     * 初始化播放器
     */
    private void initPlayer(Context context) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new SuperPlayer(context);
            mMediaPlayer.initPlayer();
        }
        if (mRenderView == null) {
            mRenderView = new TextureRenderView(context, mMediaPlayer);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            mPlayerContainerView.addView(mRenderView.getRendView(), 0, layoutParams);
        }

        mMediaPlayer.setMediaPlayerListener(this);
    }

    private SuperPlayerListener getSuperPlayerListener() {
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.getPlayerListener() != null) {
            return mCurrentPlayerModel.getPlayerListener();
        }
        return null;
    }

    private void stop(boolean clearFrame) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setMediaPlayerListener(null);
            mMediaPlayer.stop(clearFrame);
            removeProgressRunnable();
        }
    }

    private void setPlayState(PlayerState playState) {
        if (mCurrentPlayState != playState) {
            mCurrentPlayState = playState;
            SuperPlayerListener playerListener = getSuperPlayerListener();
            if (playerListener != null) {
                playerListener.onSuperPlayerStateChanged(mCurrentPlayerModel, playState);
            }
        }
    }

    private void setPlayProgressCallback() {
        if (mCurrentPlayerModel != null && mMediaPlayer != null) {
            SuperPlayerListener playerListener = getSuperPlayerListener();
            if (playerListener != null) {
                playerListener.onSuperPlayerProgress(mCurrentPlayerModel, mMediaPlayer.getCurrentDuration(), mMediaPlayer.getTotalDuration());
            }
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void releaseRenderView() {
        if (mRenderView != null) {
            mPlayerContainerView.removeView(mRenderView.getRendView());
            mRenderView.release();
            mRenderView = null;
        }
    }

    private void postProgressRunnable() {
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.isNeedProgressCallback) {
            if (mProgressRunnable == null) {
                mProgressRunnable = new ProgressRunnable();
            }
            removeProgressRunnable();
            post(mProgressRunnable);
        }
    }

    private void removeProgressRunnable() {
        if (mProgressRunnable != null) {
            removeCallbacks(mProgressRunnable);
        }
    }

    // ------------------------------------------------------------- @ MediaPlayerListener

    @Override
    public void onInfo(int what, int extra) {
        if (what == Constants.MEDIA_INFO_BUFFERING_START) {
            setPlayState(PlayerState.BUFFERING);
        } else if (what == Constants.MEDIA_INFO_BUFFERING_END) {
            setPlayState(PlayerState.BUFFERED);
        } else if (what == Constants.MEDIA_INFO_VIDEO_RENDER_START) {
            setPlayState(PlayerState.PLAYING);
        } else if (what == Constants.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            if (mRenderView != null) {
                mRenderView.setVideoRotation(extra);
            }
        }
    }

    @Override
    public void onPrepared() {
        setPlayState(PlayerState.PREPARED);
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.startPlayPosition > 0) {
            seek(mCurrentPlayerModel.startPlayPosition);
        }
    }

    @Override
    public void onCompletion() {
        setPlayState(PlayerState.COMPLETED);
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.isGoneAfterComplete) {
            setVisibility(GONE);
        }
        removeProgressRunnable();
    }

    @Override
    public void onError() {
        setPlayState(PlayerState.ERROR);
        removeProgressRunnable();
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        if (mRenderView != null) {
            mRenderView.setRenderMode(mCurrentPlayerModel.renderMode);
            mRenderView.setVideoSize(width, height);
        }
    }

    // ------------------------------------------------------------- @.@

    /**
     * ProgressRunnable
     */
    private class ProgressRunnable implements Runnable {
        @Override
        public void run() {
            if (mCurrentPlayerModel != null && mMediaPlayer != null) {
                if (mCurrentPlayState == PlayerState.PREPARING || mCurrentPlayState == PlayerState.PREPARED
                        || mCurrentPlayState == PlayerState.PLAYING || mCurrentPlayState == PlayerState.PAUSED
                        || mCurrentPlayState == PlayerState.BUFFERING || mCurrentPlayState == PlayerState.BUFFERED) {
                    long currentDuration = mMediaPlayer.getCurrentDuration();
                    long totalDuration = mMediaPlayer.getTotalDuration();
                    if (totalDuration > 0) {
                        setPlayProgressCallback();
                    }

                    long delayMillis = 1000;
                    if (mCurrentPlayState != PlayerState.PAUSED) {
                        delayMillis = 1000 - (currentDuration % 1000);
                    }
                    postDelayed(mProgressRunnable, delayMillis);
                }
            }
        }
    }

}
