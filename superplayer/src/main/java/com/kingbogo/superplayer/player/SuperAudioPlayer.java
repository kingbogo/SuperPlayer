package com.kingbogo.superplayer.player;

import android.content.Context;

import com.kingbogo.superplayer.common.IMediaQueue;
import com.kingbogo.superplayer.common.MediaQueue;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.listener.SuperPlayerQueueListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.util.SuperLogUtil;

import java.util.List;

/**
 * <p>
 * 超级播放器（音频）
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/29
 */
public class SuperAudioPlayer extends MediaPlayer implements IMediaQueue.MediaQueueListener {
    
    private static final String TAG = "SuperAudioPlayer";
    
    private Context mContext;
    
    private IMediaQueue mMediaQueue;
    private SuperPlayerQueueListener mMediaQueueListener;
    
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
    
    /**
     * 播放
     *
     * @param playerModel 对象
     *
     * @return 成功/失败
     */
    @Override
    public boolean playWithModel(SuperPlayerModel playerModel) {
        stop(false);
        initPlayer(mContext);
        return super.playWithModel(playerModel);
    }
    
    /**
     * 播放一个列表
     *
     * @param modelList 列表
     */
    public void playWithModelList(List<SuperPlayerModel> modelList) {
        playWithModelList(modelList, null);
    }
    
    /**
     * 播放一个列表
     *
     * @param modelList          列表
     * @param mediaQueueListener 对列事件
     */
    public void playWithModelList(List<SuperPlayerModel> modelList,
                                  SuperPlayerQueueListener mediaQueueListener) {
        playWithModelList(modelList, 0, SuperConstants.MEDIA_QUEUE_MODE_LIST_ONCE, mediaQueueListener);
    }
    
    /**
     * 播放一个列表
     *
     * @param modelList          列表
     * @param playIndex          播放序列：默认传0:从第1个开始播放
     * @param queueLoopMode      播放的循环模式 @
     * @param mediaQueueListener 对列事件
     */
    public void playWithModelList(List<SuperPlayerModel> modelList,
                                  int playIndex,
                                  @IMediaQueue.MediaQueueLoopMode int queueLoopMode,
                                  SuperPlayerQueueListener mediaQueueListener) {
        SuperLogUtil.d(TAG, "_playWithModelList()......");
        if (!CheckUtil.isEmpty(modelList)) {
            mMediaQueueListener = mediaQueueListener;
            if (mMediaQueue == null) {
                mMediaQueue = new MediaQueue();
            }
            mMediaQueue.setListener(this);
            mMediaQueue.update(modelList);
            mMediaQueue.setQueueLoopMode(queueLoopMode);
            if (playIndex < 0) {
                playIndex = 0;
            }
            int maxIndex = modelList.size() - 1;
            if (playIndex > maxIndex) {
                playIndex = maxIndex;
            }
            setMediaQueue(mMediaQueue);
            mMediaQueue.skipToIndex(playIndex);
        }
    }
    
    /**
     * 重播
     */
    public void replay() {
        if (mCurrentPlayerModel != null) {
            playWithModel(mCurrentPlayerModel);
        }
    }
    
    @Override
    public void release() {
        super.release();
        if (mMediaQueue != null) {
            mMediaQueue.destroy();
        }
    }
    
    //------------------------------------------------ @ IMediaQueue.MediaQueueListener
    
    @Override
    public void onQueueCurrentIndexUpdate(int index, SuperPlayerModel playerModel) {
        SuperLogUtil.i(TAG, "_onQueueCurrentIndexUpdate(), index: " + index + ", playerModel: " + playerModel);
        if (playerModel != null) {
            playWithModel(playerModel);
        }
        if (mMediaQueueListener != null) {
            mMediaQueueListener.onSuperPlayerQueueIndexUpdate(index, playerModel);
        }
    }
    
}
