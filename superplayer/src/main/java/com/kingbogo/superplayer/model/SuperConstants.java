package com.kingbogo.superplayer.model;

/**
 * <p>
 * 常量类
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/8
 */
public final class SuperConstants {

    // ------------------------------------------------------- @ rend mode

    /** 渲染模式：自适应 */
    public static final int REND_MODE_DEFAULT = 0;
    /** 渲染模式：16:9 */
    public static final int REND_MODE_16_9 = 1;
    /** 渲染模式：4:3 */
    public static final int REND_MODE_4_3 = 2;
    /** 渲染模式：平铺 */
    public static final int REND_MODE_FIT_XY = 3;
    /** 渲染模式：原始大小 */
    public static final int REND_MODE_ORIGINAL = 4;
    /** 渲染模式：剧中+裁剪 */
    public static final int REND_MODE_CENTER_CROP = 5;

    // ------------------------------------------------------- @ PlayerMode

    public static final int PLAYER_MODE_NORMAL = 1;
    public static final int PLAYER_MODE_FULLSCREEN = 2;

    // ------------------------------------------------------- @ MediaInfo

    /** MediaInfo：缓冲开始 */
    public static final int MEDIA_INFO_BUFFERING_START = 1;
    /** MediaInfo：缓冲结束 */
    public static final int MEDIA_INFO_BUFFERING_END = 2;
    /** MediaInfo：视频旋转信息 */
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 3;
    /** MediaInfo：渲染开始 */
    public static final int MEDIA_INFO_VIDEO_RENDER_START = 4;

    // ------------------------------------------------------- @ MediaQueueMode

    /** 队列模式：列表循环 */
    public static final int MEDIA_QUEUE_MODE_LIST_LOOP = 0;
    /** 队列模式：列表一次 */
    public static final int MEDIA_QUEUE_MODE_LIST_SINGLE = 1;
    /** 队列模式：单曲循环 */
    public static final int MEDIA_QUEUE_MODE_SINGLE_LOOP = 2;
    /** 队列模式：单曲模式 */
    public static final int MEDIA_QUEUE_MODE_SINGLE_ONCE = 3;

}
