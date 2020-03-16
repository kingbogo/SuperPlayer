package com.kingbogo.superplayer.player;

import android.content.Context;

import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.SuperPlayerModel;

/**
 * <p>
 * 超级播放器（音频）
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/29
 */
public class SuperAudioPlayer extends MediaPlayer {

    private static final String TAG = "SuperAudioPlayer";

    private Context mContext;

    /**
     * [构造]
     */
    public SuperAudioPlayer(Context context) {
        mContext = context;
    }

    /**
     * 播放
     *
     * @param url URL
     */
    public void playWithUrl(String url) {
        SuperPlayerModel playerModel = new SuperPlayerModel(url);
        // 默认锁屏不暂停
        playerModel.isPauseAfterLockScreen = false;
        playWithModel(playerModel);
    }

    /**
     * 播放
     *
     * @param url            URL
     * @param playerListener 监听者
     */
    public void playWithUrl(String url, SuperPlayerListener playerListener) {
        SuperPlayerModel playerModel = new SuperPlayerModel(url);
        // 默认锁屏不暂停
        playerModel.isPauseAfterLockScreen = false;
        playerModel.setPlayerListener(playerListener);
        playWithModel(playerModel);
    }

    @Override
    public boolean playWithModel(SuperPlayerModel playerModel) {
        stop(false);
        initPlayer(mContext);
        return super.playWithModel(playerModel);
    }

    /**
     * 重播
     */
    public void replay() {
        if (mCurrentPlayerModel != null) {
            playWithModel(mCurrentPlayerModel);
        }
    }
}
