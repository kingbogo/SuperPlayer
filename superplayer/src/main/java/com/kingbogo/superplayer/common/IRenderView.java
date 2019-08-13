package com.kingbogo.superplayer.common;

import android.graphics.Bitmap;
import android.view.View;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/9
 */
public interface IRenderView {

    /**
     * @return 获取RenderView
     */
    View getRendView();

    /**
     * 设置视频宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置渲染模式
     *
     * @param renderMode 模式
     */
    void setRenderMode(int renderMode);

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度值
     */
    void setVideoRotation(int degree);

    /**
     * 截图
     */
    Bitmap screenshot();

    /**
     * 释放资源
     */
    void release();

}
