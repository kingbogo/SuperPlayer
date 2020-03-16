package com.kingbogo.superplayer.player;

import android.content.Context;

import com.kingbogo.superplayer.common.IPlayer;
import com.kingbogo.superplayer.common.IRenderView;
import com.kingbogo.superplayer.common.ISuperPlayerView;
import com.kingbogo.superplayer.listener.MediaPlayerListener;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.player.core.ExoPlayer;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.util.SuperLogUtil;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/29
 */
public class MediaPlayer implements MediaPlayerListener {

    private static final String TAG = "MediaPlayer";

    private IPlayer mPlayer;
    private ISuperPlayerView mSuperPlayerView;

    protected SuperPlayerModel mCurrentPlayerModel;

    private SuperPlayerState mCurrentPlayState = SuperPlayerState.IDLE;

    /**
     * [构造]
     */
    public MediaPlayer() {
    }

    // ------------------------------------------------------------- public

    public IPlayer getPlayer() {
        return mPlayer;
    }

    public void initPlayer(Context context) {
        if (mPlayer == null) {
            mPlayer = new ExoPlayer(context);
            mPlayer.initPlayer();
        }
        openMediaPlayerListener();
    }

    public void openMediaPlayerListener() {
        if (mPlayer != null) {
            mPlayer.setMediaPlayerListener(this);
        }
    }

    public SuperPlayerModel getCurrentPlayerModel() {
        return mCurrentPlayerModel;
    }

    public void setCurrentPlayerModel(SuperPlayerModel playerModel) {
        mCurrentPlayerModel = playerModel;
    }

    public SuperPlayerListener getSuperPlayerListener() {
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.getPlayerListener() != null) {
            return mCurrentPlayerModel.getPlayerListener();
        }
        return null;
    }

    public void setSuperPlayerView(ISuperPlayerView superPlayerView) {
        mSuperPlayerView = superPlayerView;
    }

    // ------------------------------------------------------------- function

    /**
     * 播放
     */
    public boolean playWithModel(SuperPlayerModel playerModel) {
        if (mPlayer == null) {
            SuperLogUtil.w("_playWithModel(), mPlayer is not Init...");
            setPlayState(SuperPlayerState.ERROR);
            return false;
        }
        if (playerModel == null || CheckUtil.isEmpty(playerModel.url)) {
            SuperLogUtil.w("_playWithModel(), playerModel is null OR playerModel.url is null...");
            setPlayState(SuperPlayerState.ERROR);
            return false;
        }
        mCurrentPlayerModel = playerModel;
        // config
        setLoop(playerModel.isLoop);
        setMute(playerModel.isMute);
        // play
        mPlayer.startPlay(playerModel.url);
        setPlayState(SuperPlayerState.PREPARING);
        return true;
    }

    /**
     * 重播：seek方式
     */
    public void replay4Seek() {
        if (mPlayer != null) {
            mPlayer.replay();
        }
    }

    /**
     * 设置进度。单位：s
     */
    public void seek(long seekTime) {
        seek4Ms(seekTime * 1000L);
    }

    /**
     * 设置进度。单位：ms
     */
    public void seek4Ms(long seekTime) {
        if (mPlayer != null) {
            mPlayer.seekTo(seekTime);
            mPlayer.resume();
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        SuperLogUtil.d(TAG, "_resume()...");
        if (mPlayer != null) {
            if (isPlaying() || mCurrentPlayState == SuperPlayerState.PAUSED) {
                mPlayer.resume();
                setPlayState(SuperPlayerState.PLAYING);
            }
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        SuperLogUtil.d(TAG, "_pause()...");
        if (mPlayer != null && isPlaying()) {
            mPlayer.pause();
            setPlayState(SuperPlayerState.PAUSED);
        }
    }


    /**
     * 停止
     */
    public void stop(boolean clearFrame) {
        if (mPlayer != null) {
            mPlayer.setMediaPlayerListener(null);
            mPlayer.stop(clearFrame);
            setPlayState(SuperPlayerState.STOPPED);
        }
    }


    /**
     * 是否循环播放
     */
    public void setLoop(boolean isLoop) {
        if (mPlayer != null) {
            mPlayer.setLoop(isLoop);
        }
    }

    /**
     * 设置是否静音
     *
     * @param isMute 是否静音
     */
    public void setMute(boolean isMute) {
        if (mPlayer != null) {
            mPlayer.setVolume(isMute ? 0.0f : 1.0f);
        }
    }

    /**
     * 是否静音
     */
    public boolean isMute() {
        if (mPlayer != null) {
            return mPlayer.getVolume() == 0.0f;
        }
        return false;
    }

    /**
     * 设置音量
     */
    public void setVolume(float volume) {
        if (mPlayer != null) {
            mPlayer.setVolume(volume);
        }
    }

    /**
     * @return 获取音量
     */
    public float getVolume() {
        if (mPlayer != null) {
            return mPlayer.getVolume();
        } else {
            return 0.0f;
        }
    }

    /**
     * 设置播放速度
     */
    public void setSpeed(float speed) {
        if (mPlayer != null) {
            mPlayer.setSpeed(speed);
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
        if (mSuperPlayerView != null) {
            mSuperPlayerView = null;
        }
        if (mCurrentPlayerModel != null) {
            mCurrentPlayerModel.setPlayerListener(null);
            mCurrentPlayerModel = null;
        }
        stop(true);
        releaseMediaPlayer();
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        SuperLogUtil.d(TAG, "_isPlaying(), mPlayer: " + mPlayer + ", mCurrentPlayState: " + mCurrentPlayState);
        if (mPlayer != null && mPlayer.isPlaying()) {
            return true;
        } else if (mCurrentPlayState != null) {
            return mCurrentPlayState == SuperPlayerState.PREPARING || mCurrentPlayState == SuperPlayerState.PREPARED
                    || mCurrentPlayState == SuperPlayerState.PLAYING || mCurrentPlayState == SuperPlayerState.BUFFERING
                    || mCurrentPlayState == SuperPlayerState.BUFFERED;
        } else {
            return false;
        }
    }

    /**
     * 视频总长。单位：ms
     */
    public long getTotalDuration() {
        if (mPlayer != null) {
            return mPlayer.getTotalDuration();
        }
        return 0L;
    }

    /**
     * 当前播放进度。单位：ms
     */
    public long getCurrentDuration() {
        if (mPlayer != null) {
            return mPlayer.getCurrentDuration();
        }
        return 0L;
    }

    /**
     * 缓冲进度。单位：ms
     */
    public long getBufferedDuration() {
        if (mPlayer != null) {
            return mPlayer.getBufferedDuration();
        }
        return 0L;
    }

    /**
     * 缓冲进度百分比
     */
    public int getBufferedPercentage() {
        if (mPlayer != null) {
            return mPlayer.getBufferedPercentage();
        }
        return 0;
    }

    /**
     * @return 获取当前播放状态
     */
    public SuperPlayerState getPlayState() {
        return mCurrentPlayState;
    }

    /**
     * 音频会话ID
     */
    public int getAudioSessionId() {
        if (mPlayer != null) {
            return mPlayer.getAudioSessionId();
        }
        return 0;
    }

    /**
     * 宽
     */
    public int getWidth() {
        if (mPlayer != null) {
            return mPlayer.getWidth();
        }
        return 0;
    }

    /**
     * 高
     */
    public int getHeight() {
        if (mPlayer != null) {
            return mPlayer.getHeight();
        }
        return 0;
    }

    // ------------------------------------------------------------- private

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void setPlayState(SuperPlayerState playState) {
        if (mCurrentPlayState != playState) {
            mCurrentPlayState = playState;
            SuperPlayerListener playerListener = getSuperPlayerListener();
            if (playerListener != null) {
                playerListener.onSuperPlayerStateChanged(mCurrentPlayerModel, playState);
            }
            if (mSuperPlayerView != null) {
                mSuperPlayerView.onPlayerStateChanged(playState);
            }
        }
    }

    // ------------------------------------------------------------- @ MediaPlayerListener

    @Override
    public void onInfo(int what, int extra) {
        SuperLogUtil.v(TAG, "_onInfo(), what: " + what + ", extra: " + extra);
        if (what == SuperConstants.MEDIA_INFO_BUFFERING_START) {
            setPlayState(SuperPlayerState.BUFFERING);
        } else if (what == SuperConstants.MEDIA_INFO_BUFFERING_END) {
            setPlayState(SuperPlayerState.BUFFERED);
        } else if (what == SuperConstants.MEDIA_INFO_VIDEO_RENDER_START) {
            setPlayState(SuperPlayerState.PLAYING);
        } else if (what == SuperConstants.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            if (mSuperPlayerView != null && mSuperPlayerView.getRenderView() != null) {
                mSuperPlayerView.getRenderView().setVideoRotation(extra);
            }
        }
    }

    @Override
    public void onPrepared() {
        setPlayState(SuperPlayerState.PREPARED);
        if (mCurrentPlayerModel != null && mCurrentPlayerModel.startPlayPositionMs > 0) {
            seek4Ms(mCurrentPlayerModel.startPlayPositionMs);
        }
    }

    @Override
    public void onCompletion() {
        setPlayState(SuperPlayerState.COMPLETED);
    }

    @Override
    public void onError(int errorCode) {
        setPlayState(SuperPlayerState.ERROR);
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        if (mSuperPlayerView != null && mSuperPlayerView.getRenderView() != null) {
            IRenderView renderView = mSuperPlayerView.getRenderView();
            renderView.setRenderMode(mCurrentPlayerModel.renderMode);
            renderView.setVideoSize(width, height);
        }
    }

}
