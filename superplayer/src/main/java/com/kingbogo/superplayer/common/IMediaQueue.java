package com.kingbogo.superplayer.common;

import android.support.annotation.IntDef;

import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.player.MediaPlayer;

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

    void init(MediaPlayer mediaPlayer);

    void destory();

    void update(List<SuperPlayerModel> data);

    boolean isEmpty();

    int size();

    void clear();

    List<SuperPlayerModel> getAllData();

    SuperPlayerModel remove(int index);

    SuperPlayerModel getMediaItem(int index);

    SuperPlayerModel getMediaItem(String mediaId);

    int getCurrentIndex();

    /** 跳转到index */
    boolean skipToIndex(int index);

    /** 跳转到上一个 */
    boolean skipToPrevious();

    /** 跳转到下一个 */
    boolean skipToNext();

    void setQueueMode(@MediaQueueMode int mediaQueueMode);

    /**
     * 添加对媒体播放队列的状态监听者
     * @param listener
     */
    void addListener(MediaQueueListener listener);

    void removeListener(MediaQueueListener listener);

    // ------------------------------------------------------------------ @ queue listener

    interface MediaQueueListener {

    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SuperConstants.MEDIA_QUEUE_MODE_LIST_LOOP, SuperConstants.MEDIA_QUEUE_MODE_LIST_SINGLE,
            SuperConstants.MEDIA_QUEUE_MODE_SINGLE_LOOP, SuperConstants.MEDIA_QUEUE_MODE_SINGLE_ONCE})
    @interface MediaQueueMode {
    }

}
