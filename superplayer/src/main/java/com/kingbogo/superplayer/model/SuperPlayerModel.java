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

    /** title */
    public String title;

    /** 占位图 【视频专用】 */
    @DrawableRes
    public int holderImageResId;

    /** 是否循环播放 */
    public boolean isLoop = false;

    /** 是否静音播放 */
    public boolean isMute = false;

    /** 播放完成后：是否隐藏  【视频专用】 */
    public boolean isGoneAfterComplete = false;

    /** 锁屏是否 暂停 */
    public boolean isPauseAfterLockScreen = true;

    /** 开始播放的位置.单位：ms */
    public long startPlayPositionMs = 0;

    /** 默认播放填充模式 （默认播放模式为: 自适应模式 ）  【视频专用】 */
    public int renderMode = SuperConstants.REND_MODE_DEFAULT;

    /** 播放器模式：正常模式  【视频专用】 */
    public int playerMode = SuperConstants.PLAYER_MODE_NORMAL;

    /** 是否需要进度回调 【视频专用】 */
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

    /**
     * [构造]
     *
     * @param url   播放地址
     * @param tag   TAG
     * @param title 标题
     */
    public SuperPlayerModel(String url, String tag, String title) {
        this.url = url;
        this.tag = tag;
        this.title = title;
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
