package com.kingbogo.superplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingbogo.superplayer.R;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/31
 */
public class SuperIconView extends FrameLayout {

    private ImageView mIconIv;

    public SuperIconView(@NonNull Context context) {
        this(context, null);
    }

    public SuperIconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperIconView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = View.inflate(getContext(), R.layout.view_super_icon, this);
        mIconIv = rootView.findViewById(R.id.super_icon_iv);
    }

    public void setIcon(@DrawableRes int iconResId, OnClickListener clickListener) {
        setVisibility(VISIBLE);
        mIconIv.setImageResource(iconResId);
        if (clickListener != null) {
            mIconIv.setOnClickListener(clickListener);
        }
    }

}
