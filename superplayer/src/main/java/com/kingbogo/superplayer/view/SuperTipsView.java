package com.kingbogo.superplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class SuperTipsView extends FrameLayout {

    private LinearLayout mTipsLl;
    private TextView mTipsTv;
    private TextView mTipsButtonTv;


    public SuperTipsView(@NonNull Context context) {
        this(context, null);
    }

    public SuperTipsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTipsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = View.inflate(getContext(), R.layout.view_super_tips, this);
        mTipsLl = rootView.findViewById(R.id.super_tips_ll);
        mTipsTv = rootView.findViewById(R.id.super_tips_tv);
        mTipsButtonTv = rootView.findViewById(R.id.super_tips_button_tv);
    }

    public void setTips(String tips) {
        if (tips != null) {
            mTipsTv.setText(tips);
        } else {
            mTipsTv.setVisibility(GONE);
        }
    }

    public void setTipsAndHideButton(String tips) {
        setTips(tips);
        mTipsButtonTv.setVisibility(GONE);
    }

    public void setButtonTextAndListener(String buttonText, View.OnClickListener clickListener) {
        mTipsButtonTv.setVisibility(VISIBLE);
        if (buttonText != null) {
            mTipsButtonTv.setText(buttonText);
        }
        if (clickListener != null) {
            mTipsButtonTv.setOnClickListener(clickListener);
        }
    }

}
