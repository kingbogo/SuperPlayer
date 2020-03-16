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
import com.kingbogo.superplayer.common.IMediaQueue;
import com.kingbogo.superplayer.common.IPlayerControl;
import com.kingbogo.superplayer.common.IRenderView;
import com.kingbogo.superplayer.common.ISuperPlayerView;
import com.kingbogo.superplayer.common.MediaQueue;
import com.kingbogo.superplayer.controller.BaseController;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.listener.SuperPlayerQueueListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.player.MediaPlayer;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.util.SuperLogUtil;

import java.util.List;

/**
 * <p>
 * 超级播放器View（视频）
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/12
 */
public class SuperPlayerView extends FrameLayout implements ISuperPlayerView, IPlayerControl, IMediaQueue.MediaQueueListener {
    
    private static final String TAG = "SuperPlayerView";
    
    private FrameLayout mRootView;
    private FrameLayout mPlayerContainerView;
    private ImageView mHolderIv;
    
    private MediaPlayer mMediaPlayer;
    private IRenderView mRenderView;
    
    private ProgressRunnable mProgressRunnable;
    
    /** 控制器 */
    private BaseController mPlayerController;
    private boolean mIsLocked;
    private boolean mIsFullScreen;
    
    private IMediaQueue mMediaQueue;
    private SuperPlayerQueueListener mMediaQueueListener;
    
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
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_super_player, this);
        mPlayerContainerView = mRootView.findViewById(R.id.super_view_container);
        mHolderIv = mRootView.findViewById(R.id.super_view_holder_iv);
    }
    
    // ------------------------------------------------------------- @ Public
    
    /**
     * 设置播放对象
     */
    public void setSuperPlayerModel(SuperPlayerModel playerModel) {
        initPlayer();
        mMediaPlayer.setCurrentPlayerModel(playerModel);
    }
    
    /**
     * 播放（点播）
     */
    public void playWithUrl(String url) {
        playWithModel(new SuperPlayerModel(url));
    }
    
    /**
     * 播放（点播）
     */
    public void playWithUrl(String url, SuperPlayerListener playerListener) {
        SuperPlayerModel playerModel = new SuperPlayerModel(url);
        playerModel.setPlayerListener(playerListener);
        playWithModel(playerModel);
    }
    
    /**
     * 播放（点播）
     */
    public void playWithModel(SuperPlayerModel playerModel) {
        SuperLogUtil.v(TAG, "_playWithModel()......");
        setVisibility(VISIBLE);
        // init
        stop(false);
        initPlayer();
        // config
        setRenderMode(playerModel.renderMode);
        if (mPlayerController != null) {
            playerModel.isNeedProgressCallback = true;
            playerModel.isGoneAfterComplete = false;
        }
        // play
        boolean isSuccess = mMediaPlayer.playWithModel(playerModel);
        if (isSuccess) {
            // progress callback
            postProgressRunnable();
        }
        // controller
        if (mPlayerController != null) {
            mPlayerController.onPlayerModeChanged(playerModel.playerMode);
            mPlayerController.onPlayerSetTitle(playerModel.title);
        }
    }
    
    /**
     * 播放一个列表
     *
     * @param modelList 列表
     */
    public void playWithModelList(List<SuperPlayerModel> modelList) {
        playWithModelList(modelList, null);
    }
    
    /**
     * 播放一个列表
     *
     * @param modelList          列表
     * @param mediaQueueListener 对列事件
     */
    public void playWithModelList(List<SuperPlayerModel> modelList,
                                  SuperPlayerQueueListener mediaQueueListener) {
        playWithModelList(modelList, 0, SuperConstants.MEDIA_QUEUE_MODE_LIST_ONCE, mediaQueueListener);
    }
    
    /**
     * 播放一个列表
     *
     * @param modelList          列表
     * @param playIndex          播放序列：默认传0:从第1个开始播放
     * @param queueLoopMode      播放的循环模式 @
     * @param mediaQueueListener 对列事件
     */
    public void playWithModelList(List<SuperPlayerModel> modelList, int playIndex,
                                  @IMediaQueue.MediaQueueLoopMode int queueLoopMode,
                                  SuperPlayerQueueListener mediaQueueListener) {
        SuperLogUtil.d(TAG, "_playWithModelList()......");
        if (!CheckUtil.isEmpty(modelList)) {
            mMediaQueueListener = mediaQueueListener;
            if (mMediaQueue == null) {
                mMediaQueue = new MediaQueue();
            }
            mMediaQueue.setListener(this);
            mMediaQueue.update(modelList);
            mMediaQueue.setQueueLoopMode(queueLoopMode);
            if (playIndex < 0) {
                playIndex = 0;
            }
            int maxIndex = modelList.size() - 1;
            if (playIndex > maxIndex) {
                playIndex = maxIndex;
            }
            mMediaPlayer.setMediaQueue(mMediaQueue);
            mMediaQueue.skipToIndex(playIndex);
        }
    }
    
    /**
     * 重播
     */
    public void replay() {
        SuperLogUtil.v(TAG, "_replay()......");
        if (mMediaPlayer != null) {
            playWithModel(mMediaPlayer.getCurrentPlayerModel());
        }
    }
    
    /**
     * 重播：seek方式
     */
    public void replay4Seek() {
        if (mMediaPlayer != null) {
            mMediaPlayer.replay4Seek();
        }
    }
    
    /**
     * 设置进度。单位：s
     */
    public void seek(long seekTime) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seek(seekTime);
        }
    }
    
    /**
     * 设置进度。单位：ms
     */
    public void seek4Ms(long seekTime) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seek4Ms(seekTime);
        }
    }
    
    /**
     * 继续播放
     */
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.resume();
        }
    }
    
    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }
    
    /**
     * 停止播放
     *
     * @param isClearFrame 清除帖画面
     */
    public void stopPlay(boolean isClearFrame) {
        stop(isClearFrame);
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
            mMediaPlayer.setMute(isMute);
        }
    }
    
    /**
     * 是否静音
     */
    public boolean isMute() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isMute();
        }
        return false;
    }
    
    /**
     * 设置音量
     */
    public void setVolume(float volume) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(volume);
        }
    }
    
    /**
     * @return 获取音量
     */
    public float getVolume() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVolume();
        }
        return 0.0f;
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
     * 设置播放速度
     */
    public void setSpeed(float speed) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSpeed(speed);
        }
    }
    
    /**
     * 生命周期：继续
     */
    public void onResume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.onResume();
        }
    }
    
    /**
     * 生命周期：暂停
     */
    public void onPause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.onPause();
        }
    }
    
    /**
     * 释放资源
     */
    public void release() {
        mMediaQueueListener = null;
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        if (mPlayerController != null) {
            // TODO
            
        }
        if (mMediaQueue != null) {
            mMediaQueue.destroy();
        }
        removeProgressRunnable();
        releaseRenderView();
    }
    
    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            SuperLogUtil.d(TAG, "_isPlaying()... playState: " + mMediaPlayer.getPlayState());
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
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
     * 缓冲进度百分比
     */
    public int getBufferedPercentage() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getBufferedPercentage();
        }
        return 0;
    }
    
    /**
     * @return 获取当前播放状态
     */
    public SuperPlayerState getPlayState() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getPlayState();
        } else {
            return SuperPlayerState.IDLE;
        }
    }
    
    /** 显示占位图 */
    public void showHolderImage() {
        if (mMediaPlayer != null) {
            SuperPlayerModel playerModel = mMediaPlayer.getCurrentPlayerModel();
            if (playerModel != null && playerModel.holderImageResId != 0) {
                mHolderIv.setImageResource(playerModel.holderImageResId);
                mHolderIv.setVisibility(VISIBLE);
            }
        }
    }
    
    /** 显示占位图 */
    public void showHolderImage(@DrawableRes int imageResId) {
        if (imageResId != 0) {
            if (mMediaPlayer != null) {
                SuperPlayerModel playerModel = mMediaPlayer.getCurrentPlayerModel();
                if (playerModel != null) {
                    playerModel.holderImageResId = imageResId;
                }
                mHolderIv.setImageResource(imageResId);
                mHolderIv.setVisibility(VISIBLE);
            }
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
    
    /**
     * 音频会话ID
     */
    public int getAudioSessionId() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getAudioSessionId();
        }
        return 0;
    }
    
    /**
     * 设置控制器
     */
    public void setPlayerController(BaseController baseController) {
        mRootView.removeView(mPlayerController);
        mPlayerController = baseController;
        if (mPlayerController != null) {
            mPlayerController.setPlayerControl(this);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRootView.addView(mPlayerController, layoutParams);
        }
    }
    
    // ------------------------------------------------------------- @ protected
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            release();
        } catch (Exception e) {
            SuperLogUtil.e(e);
        } catch (Error e) {
            SuperLogUtil.e(e.getCause());
        }
    }
    
    // ------------------------------------------------------------- @ private
    
    /**
     * 初始化播放器
     */
    private void initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.initPlayer(getContext());
            mMediaPlayer.setSuperPlayerView(this);
        } else {
            mMediaPlayer.openMediaPlayerListener();
        }
        
        if (mRenderView == null) {
            mRenderView = new TextureRenderView(getContext(), mMediaPlayer.getPlayer());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            mPlayerContainerView.addView(mRenderView.getRendView(), 0, layoutParams);
        }
    }
    
    private void stop(boolean clearFrame) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop(clearFrame);
        }
        removeProgressRunnable();
    }
    
    private void releaseRenderView() {
        if (mRenderView != null) {
            mPlayerContainerView.removeView(mRenderView.getRendView());
            mRenderView.release();
            mRenderView = null;
        }
    }
    
    private void setPlayProgressCallback() {
        if (mMediaPlayer != null) {
            SuperPlayerModel playerModel = mMediaPlayer.getCurrentPlayerModel();
            if (playerModel != null) {
                SuperPlayerListener playerListener = mMediaPlayer.getSuperPlayerListener();
                if (playerListener != null) {
                    playerListener.onSuperPlayerProgress(playerModel, mMediaPlayer.getCurrentDuration(), mMediaPlayer.getTotalDuration());
                }
                if (mPlayerController != null) {
                    mPlayerController.onPlayerProgressChanged();
                }
            }
        }
    }
    
    private void postProgressRunnable() {
        SuperLogUtil.v(TAG, "_postProgressRunnable()...");
        if (mMediaPlayer != null) {
            SuperPlayerModel playerModel = mMediaPlayer.getCurrentPlayerModel();
            if (playerModel != null && playerModel.isNeedProgressCallback) {
                if (mProgressRunnable == null) {
                    mProgressRunnable = new SuperPlayerView.ProgressRunnable();
                }
                removeProgressRunnable();
                post(mProgressRunnable);
            }
        }
    }
    
    private void removeProgressRunnable() {
        SuperLogUtil.v(TAG, "_removeProgressRunnable()...");
        if (mProgressRunnable != null) {
            removeCallbacks(mProgressRunnable);
        }
    }
    
    // ------------------------------------------------------------- @ ISuperPlayerView
    
    @Override
    public void onPlayerStateChanged(SuperPlayerState playerState) {
        if (playerState == SuperPlayerState.COMPLETED) {
            if (mMediaPlayer != null) {
                SuperPlayerModel playerModel = mMediaPlayer.getCurrentPlayerModel();
                if (playerModel != null && playerModel.isGoneAfterComplete) {
                    setVisibility(GONE);
                }
            }
            removeProgressRunnable();
            // 完成时再回调一次进度
            setPlayProgressCallback();
            
        } else if (playerState == SuperPlayerState.ERROR) {
            removeProgressRunnable();
        }
        if (mPlayerController != null) {
            mPlayerController.onPlayerStateChanged(playerState);
        }
    }
    
    @Override
    public IRenderView getRenderView() {
        return mRenderView;
    }
    
    // ------------------------------------------------------------- @ IPlayerControl
    
    @Override
    public void onCtrlResume() {
        SuperLogUtil.d(TAG, "_onCtrlResume()...");
        resume();
    }
    
    @Override
    public void onCtrlPause() {
        SuperLogUtil.d(TAG, "_onCtrlPause()...");
        pause();
    }
    
    @Override
    public void onCtrlReplay() {
        SuperLogUtil.d(TAG, "_onCtrlReplay()...");
        replay();
    }
    
    @Override
    public long onCtrlGetCurrentDuration() {
        return getCurrentDuration();
    }
    
    @Override
    public long onCtrlGetTotalDuration() {
        return getTotalDuration();
    }
    
    @Override
    public int onCtrlGetBufferedPercentage() {
        return getBufferedPercentage();
    }
    
    @Override
    public void onCtrlSeekTo(long timeMs) {
        SuperLogUtil.d(TAG, "_onCtrlSeekTo()...");
        seek4Ms(timeMs);
    }
    
    @Override
    public boolean onCtrlIsPlaying() {
        SuperLogUtil.d(TAG, "_onCtrlIsPlaying()...");
        return isPlaying();
    }
    
    @Override
    public void onCtrlSetMute(boolean isMute) {
        SuperLogUtil.d(TAG, "_onCtrlSetMute()...");
        setMute(isMute);
    }
    
    @Override
    public boolean onCtrlIsMute() {
        return isMute();
    }
    
    @Override
    public void onCtrlSetRenderMode(int renderMode) {
        setRenderMode(renderMode);
    }
    
    @Override
    public void onCtrlSetSpeed(float speed) {
        SuperLogUtil.d(TAG, "_onCtrlSetSpeed()...");
        setSpeed(speed);
    }
    
    @Override
    public void onCtrlSetLock(boolean isLock) {
        mIsLocked = isLock;
    }
    
    @Override
    public void onCtrlFullScreen(boolean isFull) {
        mIsFullScreen = isFull;
        if (isFull) {
            // TODO 全屏
            
        } else {
            // TODO 退出全屏
            
        }
    }
    
    @Override
    public boolean onCtrlIsFullScreen() {
        return mIsFullScreen;
    }
    
    
    // ------------------------------------------------------------- @ IMediaQueue.MediaQueueListener
    
    @Override
    public void onQueueCurrentIndexUpdate(int index, SuperPlayerModel playerModel) {
        SuperLogUtil.i(TAG, "_onQueueCurrentIndexUpdate(), index: " + index + ", playerModel: " + playerModel);
        if (playerModel != null) {
            playWithModel(playerModel);
        }
        if (mMediaQueueListener != null) {
            mMediaQueueListener.onSuperPlayerQueueIndexUpdate(index, playerModel);
        }
    }
    
    
    // ------------------------------------------------------------- @.@
    
    /**
     * ProgressRunnable
     */
    private class ProgressRunnable implements Runnable {
        @Override
        public void run() {
            if (mMediaPlayer != null) {
                SuperPlayerModel playerModel = mMediaPlayer.getCurrentPlayerModel();
                SuperPlayerState playerState = mMediaPlayer.getPlayState();
                if (playerModel != null) {
                    if (playerState == SuperPlayerState.PREPARING || playerState == SuperPlayerState.PREPARED
                            || playerState == SuperPlayerState.PLAYING || playerState == SuperPlayerState.PAUSED
                            || playerState == SuperPlayerState.BUFFERING || playerState == SuperPlayerState.BUFFERED) {
                        long currentDuration = mMediaPlayer.getCurrentDuration();
                        long totalDuration = mMediaPlayer.getTotalDuration();
                        if (totalDuration > 0) {
                            setPlayProgressCallback();
                        }
                        long delayMillis = 1000;
                        if (playerState != SuperPlayerState.PAUSED) {
                            delayMillis = 1000 - (currentDuration % 1000);
                        }
                        postDelayed(mProgressRunnable, delayMillis);
                    }
                }
            }
        }
    }
    
}
