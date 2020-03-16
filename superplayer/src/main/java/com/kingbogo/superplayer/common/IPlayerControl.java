package com.kingbogo.superplayer.common;

/**
 * <p>
 * 控制器接口
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/12
 */
public interface IPlayerControl {

    void onCtrlResume();

    void onCtrlPause();

    void onCtrlReplay();

    long onCtrlGetCurrentDuration();

    long onCtrlGetTotalDuration();

    int onCtrlGetBufferedPercentage();

    void onCtrlSeekTo(long timeMs);

    boolean onCtrlIsPlaying();

    void onCtrlSetMute(boolean isMute);

    boolean onCtrlIsMute();

    void onCtrlSetRenderMode(int renderMode);

    void onCtrlSetSpeed(float speed);

    void onCtrlSetLock(boolean isLock);

    void onCtrlFullScreen(boolean isFull);

    boolean onCtrlIsFullScreen();

}
