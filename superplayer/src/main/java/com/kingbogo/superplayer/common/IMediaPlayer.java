package com.kingbogo.superplayer.common;

import android.view.Surface;

import com.kingbogo.superplayer.listener.MediaPlayerListener;

/**
 * <p>
 * 播放器接口
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/9
 */
public interface IMediaPlayer {

    void initPlayer();

    void startPlay(String url);

    void replay();

    void seekTo(long position);

    void resume();

    void pause();

    void stop(boolean isClearFrame);

    void setLoop(boolean isLoop);

    void setSpeed(float speed);

    void setVolume(float audioVolume);

    float getVolume();

    boolean isPlaying();

    boolean isLoop();

    long getTotalDuration();

    long getCurrentDuration();

    long getBufferedDuration();

    void reset();

    void release();

    int getWidth();

    int getHeight();

    void setSurface(Surface surface);

    void setMediaPlayerListener(MediaPlayerListener playerListener);

    int getAudioSessionId();

}
