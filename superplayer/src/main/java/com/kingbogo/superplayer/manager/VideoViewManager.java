package com.kingbogo.superplayer.manager;

import com.kingbogo.superplayer.view.SuperPlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/12
 */
public class VideoViewManager {

    private List<SuperPlayerView> mPlayerViews;

    private static volatile VideoViewManager mInstance;

    private VideoViewManager() {
        mPlayerViews = new ArrayList<>();
    }

    public static VideoViewManager getInstance() {
        if (mInstance == null) {
            synchronized (VideoViewManager.class) {
                if (mInstance == null) {
                    mInstance = new VideoViewManager();
                }
            }
        }
        return mInstance;
    }


}
