package com.kingbogo.superplayer.listener;

import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.model.SuperPlayerModel;

/**
 * <p>
 * SuperPlayerListener：基本事件
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/10
 */
public interface SuperPlayerListener {

    /**
     * 状态变化
     *
     * @param playerModel 播放对象
     * @param playerState 播放器状态
     */
    void onSuperPlayerStateChanged(SuperPlayerModel playerModel, SuperPlayerState playerState);

    /**
     * 进度回调
     *
     * @param playerModel     播放对象
     * @param currentDuration 当前进度，单位：ms
     * @param totalDuration   总时长，单位：ms
     */
    void onSuperPlayerProgress(SuperPlayerModel playerModel, long currentDuration, long totalDuration);

}
