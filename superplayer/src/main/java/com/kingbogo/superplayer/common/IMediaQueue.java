package com.kingbogo.superplayer.common;

import android.support.annotation.IntDef;

import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * <p>
 * 播放队列
 * </p>
 *
 * @author Kingbo
 * @date 2019/9/12
 */
public interface IMediaQueue {
    
    void destroy();
    
    void update(List<SuperPlayerModel> data);
    
    boolean isEmpty();
    
    int size();
    
    void clear();
    
    List<SuperPlayerModel> getAllData();
    
    SuperPlayerModel remove(int index);
    
    SuperPlayerModel getMediaModelByIndex(int index);
    
    SuperPlayerModel getMediaModelById(int mediaId);
    
    SuperPlayerModel getMediaModelByTag(String mediaTag);
    
    int getCurrentIndex();
    
    /** 跳转到index */
    boolean skipToIndex(int index);
    
    /** 跳转到上一个 */
    boolean skipToPrevious();
    
    /** 跳转到下一个 */
    boolean skipToNext();
    
    void setQueueLoopMode(@MediaQueueLoopMode int mediaQueueLoopMode);
    
    @MediaQueueLoopMode
    int getQueueLoopMode();
    
    /**
     * 添加对媒体播放队列的状态监听者
     */
    void setListener(MediaQueueListener listener);
    
    /**
     * 状态变化
     *
     * @param playerState 播放器状态
     */
    void onPlayerStateChanged(SuperPlayerState playerState);
    
    // ------------------------------------------------------------------ @ MediaQueueListener
    
    /**
     * 播放对列事件
     */
    interface MediaQueueListener {
        
        /**
         * 队列中当前播放的序号更新
         *
         * @param index         当前播放的序列
         * @param isInteriorTry 是否是内部重试
         * @param playerModel   播放对象
         */
        void onQueueCurrentIndexUpdate(int index, boolean isInteriorTry, SuperPlayerModel playerModel);
        
    }
    
    // ------------------------------------------------------------------ @ queue listener
    
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SuperConstants.MEDIA_QUEUE_MODE_LIST_LOOP, SuperConstants.MEDIA_QUEUE_MODE_LIST_ONCE,
            SuperConstants.MEDIA_QUEUE_MODE_SINGLE_LOOP, SuperConstants.MEDIA_QUEUE_MODE_SINGLE_ONCE})
    @interface MediaQueueLoopMode {
    }
    
}
