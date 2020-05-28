package com.kingbogo.superplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.kingbogo.superplayer.R;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/9/2
 */
public class SuperGestureView extends LinearLayout {

    private ImageView mIconIv;
    private TextView mPercentTv;
    private ProgressBar mPercentPb;

    public SuperGestureView(Context context) {
        this(context, null);
    }

    public SuperGestureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperGestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_super_gesture, this);
        mIconIv = rootView.findViewById(R.id.super_gesture_icon_iv);
        mPercentTv = rootView.findViewById(R.id.super_gesture_percent_tv);
        mPercentPb = rootView.findViewById(R.id.super_gesture_percent_pb);
    }

    public void setIcon(@DrawableRes int icon) {
        mIconIv.setImageResource(icon);
    }

    public void setTextView(String text) {
        mPercentTv.setText(text);
    }

    public void setProPercent(int percent) {
        mPercentPb.setProgress(percent);
    }

    public void setProVisibility(int visibility) {
        mPercentPb.setVisibility(visibility);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE) {
            AlphaAnimation outAnim = new AlphaAnimation(1.0f, 0.0f);
            outAnim.setDuration(350);
            startAnimation(outAnim);
        }
    }
}
