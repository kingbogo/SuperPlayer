package com.kingbogo.superplayer.common;

import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.CheckUtil;
import com.kingbogo.superplayer.util.SuperLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 播放队列
 * </p>
 *
 * @author Kingbo
 * @date 2020-03-16
 */
public class MediaQueue implements IMediaQueue {
    
    private static final String TAG = "MediaQueue";
    
    private static final int RE_PLAY_MAX_COUNT = 3;
    
    private static final int INDEX_RESET = -1;
    
    private List<SuperPlayerModel> mQueue;
    
    private MediaQueueListener mListener;
    
    private int mCurrentIndex = INDEX_RESET;
    
    private int mLoopMode = SuperConstants.MEDIA_QUEUE_MODE_LIST_ONCE;
    
    /** 播放出错时：重试的次数 */
    private int mTrayRePlayCount = 0;
    
    @Override
    public void init() {
        mQueue = new ArrayList<>();
    }
    
    @Override
    public void destroy() {
        if (mQueue != null) {
            mQueue.clear();
            mQueue = null;
        }
        mListener = null;
    }
    
    @Override
    public void update(List<SuperPlayerModel> data) {
        if (mQueue != null) {
            mQueue.clear();
        } else {
            mQueue = new ArrayList<>();
        }
        mQueue.addAll(data);
        mCurrentIndex = INDEX_RESET;
    }
    
    @Override
    public boolean isEmpty() {
        return !CheckUtil.isEmpty(mQueue);
    }
    
    @Override
    public int size() {
        if (mQueue != null) {
            return mQueue.size();
        } else {
            return 0;
        }
    }
    
    @Override
    public void clear() {
        if (mQueue != null) {
            mQueue.clear();
        }
        mCurrentIndex = INDEX_RESET;
    }
    
    @Override
    public List<SuperPlayerModel> getAllData() {
        return mQueue;
    }
    
    @Override
    public SuperPlayerModel remove(int index) {
        if (mQueue != null) {
            return mQueue.remove(index);
        } else {
            return null;
        }
    }
    
    @Override
    public SuperPlayerModel getMediaModelByIndex(int index) {
        if (mQueue != null && index >= 0) {
            return mQueue.get(index);
        } else {
            return null;
        }
    }
    
    @Override
    public SuperPlayerModel getMediaModelById(int mediaId) {
        if (mQueue != null) {
            for (SuperPlayerModel model : mQueue) {
                if (model.id == mediaId) {
                    return model;
                }
            }
        }
        return null;
    }
    
    @Override
    public SuperPlayerModel getMediaModelByTag(String mediaTag) {
        if (!CheckUtil.isEmpty(mediaTag)) {
            for (SuperPlayerModel model : mQueue) {
                if (mediaTag.equals(model.tag)) {
                    return model;
                }
            }
        }
        return null;
    }
    
    @Override
    public int getCurrentIndex() {
        return mCurrentIndex;
    }
    
    @Override
    public boolean skipToIndex(int index) {
        SuperPlayerModel playerModel = getMediaModelByIndex(index);
        if (playerModel != null) {
            mCurrentIndex = index;
            notifyQueueCurrentIndexUpdate(index, playerModel);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean skipToPrevious() {
        if (mCurrentIndex > 0) {
            int previousIndex = mCurrentIndex - 1;
            return skipToIndex(previousIndex);
        }
        return false;
    }
    
    @Override
    public boolean skipToNext() {
        int size = size();
        int nextIndex = mCurrentIndex + 1;
        if ((size > 0) && (nextIndex < size)) {
            return skipToIndex(nextIndex);
        }
        return false;
    }
    
    @Override
    public void setQueueLoopMode(@MediaQueueLoopMode int mediaQueueMode) {
        this.mLoopMode = mediaQueueMode;
    }
    
    @Override
    public int getQueueLoopMode() {
        return mLoopMode;
    }
    
    @Override
    public void setListener(MediaQueueListener listener) {
        mListener = listener;
    }
    
    @Override
    public void onPlayerStateChanged(SuperPlayerState playerState) {
        if (playerState == SuperPlayerState.COMPLETED) {
            // 【已完成】
            if (mLoopMode == SuperConstants.MEDIA_QUEUE_MODE_LIST_LOOP) {
                // 列表循环
                if (mCurrentIndex == (size() - 1)) {
                    // 最后1个：从0开始
                    skipToIndex(0);
                } else {
                    skipToNext();
                }
            } else if (mLoopMode == SuperConstants.MEDIA_QUEUE_MODE_LIST_ONCE) {
                // 列表一次
                skipToNext();
            } else if (mLoopMode == SuperConstants.MEDIA_QUEUE_MODE_SINGLE_LOOP) {
                // 单曲循环：继续播放当前
                skipToIndex(mCurrentIndex);
            }
        } else if (playerState == SuperPlayerState.ERROR) {
            // 【播放错误】：重试3次
            SuperLogUtil.d(TAG, "_onPlayerStateChanged(), 播放出错，重试，mTrayRePlayCount: " + mTrayRePlayCount);
            if (mTrayRePlayCount < RE_PLAY_MAX_COUNT) {
                mTrayRePlayCount++;
                skipToIndex(mCurrentIndex);
            }
        } else if (playerState == SuperPlayerState.PLAYING) {
            // 【播放中】：重试清0
            mTrayRePlayCount = 0;
        }
    }
    
    // --------------------------------------------------------------------- @ private
    
    private void notifyQueueCurrentIndexUpdate(int index, SuperPlayerModel playerModel) {
        if (mListener != null) {
            mListener.onQueueCurrentIndexUpdate(index, playerModel);
        }
    }
    
}
