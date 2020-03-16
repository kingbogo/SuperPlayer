package com.kingbogo.superplayer.demo;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingbogo.superplayer.demo.util.Constants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.view.SuperPlayerView;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/22
 */
public class Fragment1 extends Fragment {

    private static final String EXTRA_DATA = "extraData";

    private String mParam;
    private Activity mActivity;
    private SuperPlayerView mSuperPlayer;

    public static Fragment1 newInstance(String param) {
        Fragment1 fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATA, param);
        fragment1.setArguments(bundle);
        return fragment1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mParam = getArguments().getString(EXTRA_DATA);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSuperPlayer != null) {
            mSuperPlayer.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSuperPlayer != null) {
            mSuperPlayer.onResume();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        TextView fragment1Tv = rootView.findViewById(R.id.fragment1_tv);
        mSuperPlayer = rootView.findViewById(R.id.fragment1_super_player_view);
        fragment1Tv.setText(mParam);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // play();
    }

    public void play() {
//        String url = "/data/data/com.kingbogo.superplayer.demo/cache/bh_1_1.mp4";
        SuperPlayerModel model = new SuperPlayerModel(Constants.VIDEO_URL_4);
        model.isLoop = true;
        mSuperPlayer.playWithModel(model);
    }

}
