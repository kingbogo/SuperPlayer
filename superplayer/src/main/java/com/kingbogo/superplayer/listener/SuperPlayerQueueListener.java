package com.kingbogo.superplayer.listener;

import com.kingbogo.superplayer.model.SuperPlayerModel;

/**
 * <p>
 * 带播放对列的 listener
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/10
 */
public interface SuperPlayerQueueListener {
    
    /**
     * 播放对列中队列当前播放的序号更新
     *
     * @param index       当前播放的序列
     * @param playerModel 播放对象
     */
    void onSuperPlayerQueueIndexUpdate(int index, SuperPlayerModel playerModel);
    
}
