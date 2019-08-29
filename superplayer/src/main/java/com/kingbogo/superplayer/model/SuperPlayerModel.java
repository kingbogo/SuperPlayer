package com.kingbogo.superplayer.model;

import android.support.annotation.DrawableRes;

import com.kingbogo.superplayer.listener.SuperPlayerListener;

import java.lang.ref.WeakReference;

/**
 * <p>
 * 播放对象
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/12
 */
public class SuperPlayerModel {

    /** URL */
    public String url;

    /** 标识 */
    public String tag;

    /** id */
    public int id;

    /** 占位图 */
    @DrawableRes
    public int holderImageResId;

    /** 是否循环播放 */
    public boolean isLoop = false;

    /** 是否静音播放 */
    public boolean isMute = false;

    /** 播放完成后：是否隐藏 */
    public boolean isGoneAfterComplete = false;

    /** 锁屏是否 暂停 */
    public boolean isPauseAfterLockScreen = true;

    /** 开始播放的位置.单位：ms */
    public long startPlayPositionMs = 0;

    /**
     * 默认播放填充模式 （默认播放模式为: 自适应模式 ） <br/>
     */
    public int renderMode = SuperConstants.REND_MODE_DEFAULT;

    /**
     * 是否需要进度回调
     */
    public boolean isNeedProgressCallback = false;

    /** 播放事件 */
    private WeakReference<SuperPlayerListener> playerListener;

    /**
     * [构造]
     */
    public SuperPlayerModel() {
    }

    /**
     * [构造]
     *
     * @param url 播放地址
     */
    public SuperPlayerModel(String url) {
        this.url = url;
    }

    public SuperPlayerListener getPlayerListener() {
        if (playerListener != null) {
            return playerListener.get();
        } else {
            return null;
        }
    }

    public void setPlayerListener(SuperPlayerListener listener) {
        playerListener = new WeakReference<>(listener);
    }

    @Override
    public String toString() {
        return "SuperPlayerModel{" +
                "url='" + url + '\'' +
                ", tag='" + tag + '\'' +
                ", id=" + id +
                ", renderMode=" + renderMode +
                '}';
    }
}
