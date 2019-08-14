package com.kingbogo.superplayer.player;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.kingbogo.superplayer.common.IMediaPlayer;
import com.kingbogo.superplayer.listener.MediaPlayerListener;
import com.kingbogo.superplayer.model.SuperConstants;

/**
 * <p>
 * 超级播放器
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/9
 */
public class SuperPlayer implements IMediaPlayer, Player.EventListener, VideoListener {

    private Context mContext;
    private SimpleExoPlayer mPlayer;

    private DataSource.Factory mDataSourceFactory;

    private Surface mSurface;
    private int mLastPlaybackState;
    private boolean mLastPlayWhenReady;
    private int mVideoWidth;
    private int mVideoHeight;

    private boolean mIsPreparing;
    private boolean mIsBuffering;

    private MediaPlayerListener mPlayerListener;

    /**
     * [构造]
     *
     * @param context 上下文
     */
    public SuperPlayer(Context context) {
        mContext = context.getApplicationContext();
        mLastPlaybackState = Player.STATE_IDLE;
    }

    // -------------------------------------------------------- @ IMediaPlayer

    @Override
    public void initPlayer() {
        mDataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, mContext.getPackageName()));
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext);
        // 注册事件
        mPlayer.addListener(this);
        mPlayer.addVideoListener(this);
    }

    @Override
    public void startPlay(String url) {
        if (mPlayer != null) {
            if (mSurface != null) {
                mPlayer.setVideoSurface(mSurface);
            }
            MediaSource videoSource = getMediaSource(url);
            mIsPreparing = true;
            mPlayer.prepare(videoSource);
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void replay() {
        seekTo(0);
        resume();
    }

    @Override
    public void seekTo(long position) {
        if (mPlayer != null) {
            mPlayer.seekTo(position);
        }
    }

    @Override
    public void resume() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void pause() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void stop(boolean isClearFrame) {
        if (mPlayer != null) {
            mPlayer.stop(isClearFrame);
        }
    }

    @Override
    public void setLoop(boolean isLoop) {
        if (mPlayer != null) {
            mPlayer.setRepeatMode(isLoop ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
        }
    }

    @Override
    public void setSpeed(float speed) {
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        if (mPlayer != null) {
            mPlayer.setPlaybackParameters(playbackParameters);
        }
    }

    @Override
    public void setVolume(float audioVolume) {
        if (mPlayer != null) {
            mPlayer.setVolume(audioVolume);
        }
    }

    @Override
    public float getVolume() {
        if (mPlayer != null) {
            return mPlayer.getVolume();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isPlaying() {
        if (mPlayer == null) {
            return false;
        }
        int playState = mPlayer.getPlaybackState();
        return playState == Player.STATE_BUFFERING || playState == Player.STATE_READY;
    }

    @Override
    public boolean isLoop() {
        if (mPlayer == null) {
            return false;
        }
        return mPlayer.getRepeatMode() == Player.REPEAT_MODE_ALL;
    }

    @Override
    public long getTotalDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getDuration();
    }

    @Override
    public long getCurrentDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getCurrentPosition();

    }

    @Override
    public long getBufferedDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getBufferedPosition();
    }

    @Override
    public void reset() {
        release();
        initPlayer();
    }

    @Override
    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer.removeListener(this);
            mPlayer.removeVideoListener(this);
            mPlayer = null;
        }

        mDataSourceFactory = null;

        mSurface = null;
        mLastPlaybackState = Player.STATE_IDLE;
        mLastPlayWhenReady = false;
        mIsPreparing = false;
        mIsBuffering = false;
        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    @Override
    public int getWidth() {
        return mVideoWidth;
    }

    @Override
    public int getHeight() {
        return mVideoHeight;
    }

    @Override
    public void setSurface(Surface surface) {
        mSurface = surface;
        if (mPlayer != null) {
            if (surface != null && !surface.isValid()) {
                mSurface = null;
            }
            mPlayer.setVideoSurface(surface);
        }
    }

    @Override
    public void setMediaPlayerListener(MediaPlayerListener playerListener) {
        mPlayerListener = playerListener;
    }

    // -------------------------------------------------------- @ Player.EventListener

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // 重新播放状态顺序为：STATE_IDLE -》STATE_BUFFERING -》STATE_READY
        // 缓冲时顺序为：STATE_BUFFERING -》STATE_READY

        // LogUtil.v("_onPlayerStateChanged(), mIsBuffering: " + mIsBuffering + ", mIsPreparing: " + mIsPreparing);
        // LogUtil.v("_onPlayerStateChanged(), playWhenReady: " + playWhenReady + ", playbackState: " + playbackState);

        if (mLastPlayWhenReady == playWhenReady && mLastPlaybackState == playbackState) {
            return;
        }

        if (mIsBuffering) {
            if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
                notifyOnInfo(SuperConstants.MEDIA_INFO_BUFFERING_END, mPlayer.getBufferedPercentage());
                mIsBuffering = false;
            }
        }

        if (mIsPreparing) {
            if (playbackState == Player.STATE_READY) {
                notifyOnPrepared();
                notifyOnInfo(SuperConstants.MEDIA_INFO_VIDEO_RENDER_START, 0);
                mIsPreparing = false;
            }
        }

        switch (playbackState) {
            case Player.STATE_IDLE:
                break;
            case Player.STATE_BUFFERING:
                notifyOnInfo(SuperConstants.MEDIA_INFO_BUFFERING_START, mPlayer.getBufferedPercentage());
                mIsBuffering = true;
                break;
            case Player.STATE_READY:
                break;
            case Player.STATE_ENDED:
                notifyOnCompletion();
                break;
            default:
                break;
        }

        mLastPlaybackState = playbackState;
        mLastPlayWhenReady = playWhenReady;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        notifyOnError();
    }

    // -------------------------------------------------------- @ VideoListener

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        mVideoWidth = width;
        mVideoHeight = height;
        notifyOnVideoSizeChanged(width, height);
        if (unappliedRotationDegrees > 0) {
            notifyOnInfo(SuperConstants.MEDIA_INFO_VIDEO_ROTATION_CHANGED, unappliedRotationDegrees);
        }
    }

    // -------------------------------------------------------- @ private

    private MediaSource getMediaSource(String mediaUrl) {
        return new ProgressiveMediaSource.Factory(mDataSourceFactory).createMediaSource(Uri.parse(mediaUrl));
    }

    private void notifyOnInfo(int what, int extra) {
        if (mPlayerListener != null) {
            mPlayerListener.onInfo(what, extra);
        }
    }

    private void notifyOnPrepared() {
        if (mPlayerListener != null) {
            mPlayerListener.onPrepared();
        }
    }

    private void notifyOnCompletion() {
        if (mPlayerListener != null) {
            mPlayerListener.onCompletion();
        }
    }

    private void notifyOnError() {
        if (mPlayerListener != null) {
            mPlayerListener.onError();
        }
    }

    private void notifyOnVideoSizeChanged(int width, int height) {
        if (mPlayerListener != null) {
            mPlayerListener.onVideoSizeChanged(width, height);
        }
    }

}
