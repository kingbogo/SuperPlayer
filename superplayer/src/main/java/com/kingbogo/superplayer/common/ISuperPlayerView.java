package com.kingbogo.superplayer.common;

import com.kingbogo.superplayer.model.SuperPlayerState;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/30
 */
public interface ISuperPlayerView {

    /**
     * 状态变化
     *
     * @param playerState 播放器状态
     */
    void onPlayerStateChanged(SuperPlayerState playerState);

    /**
     * @return 渲染View
     */
    IRenderView getRenderView();

}
