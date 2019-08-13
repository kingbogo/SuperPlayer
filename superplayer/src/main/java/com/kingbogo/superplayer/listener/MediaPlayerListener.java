package com.kingbogo.superplayer.listener;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/10
 */
public interface MediaPlayerListener {

    void onInfo(int what, int extra);

    void onPrepared();

    void onCompletion();

    void onError();

    void onVideoSizeChanged(int width, int height);

}
