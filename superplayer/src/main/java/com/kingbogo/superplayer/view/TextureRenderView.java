package com.kingbogo.superplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.kingbogo.superplayer.common.IMediaPlayer;
import com.kingbogo.superplayer.common.IRenderView;
import com.kingbogo.superplayer.util.LogUtil;
import com.kingbogo.superplayer.util.MeasureHelper;

/**
 * <p>
 * TextureRenderView
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/9
 */
@SuppressLint("ViewConstructor")
class TextureRenderView extends TextureView implements IRenderView, TextureView.SurfaceTextureListener {

    private SurfaceTexture mSurfaceTexture;
    private MeasureHelper mMeasureHelper;
    private IMediaPlayer mMediaPlayer;
    private Surface mSurface;

    public TextureRenderView(Context context, IMediaPlayer mediaPlayer) {
        super(context);
        mMediaPlayer = mediaPlayer;
        init();
    }

    private void init() {
        mMeasureHelper = new MeasureHelper();
        setSurfaceTextureListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measuredSize = mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }

    // --------------------------------------------------------- @ IRenderView

    @Override
    public View getRendView() {
        return this;
    }

    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    @Override
    public void setRenderMode(int renderMode) {
        mMeasureHelper.setRendMode(renderMode);
        requestLayout();
    }

    @Override
    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public Bitmap screenshot() {
        return getBitmap();
    }

    @Override
    public void release() {
        if (mSurface != null) {
            mSurface.release();
        }

        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
    }

    // --------------------------------------------------------- @ TextureView.SurfaceTextureListener

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        LogUtil.d("_onSurfaceTextureAvailable(), mSurfaceTexture: " + mSurfaceTexture + ", width: " + width + ", height: " + height);
        if (mSurfaceTexture != null) {
            setSurfaceTexture(mSurfaceTexture);
        } else {
            mSurfaceTexture = surfaceTexture;
            mSurface = new Surface(surfaceTexture);
            mMediaPlayer.setSurface(mSurface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtil.d("_onSurfaceTextureSizeChanged(), surface: " + surface + ", width: " + width + ", height: " + height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtil.d("_onSurfaceTextureDestroyed, surface: " + surface);
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

}
