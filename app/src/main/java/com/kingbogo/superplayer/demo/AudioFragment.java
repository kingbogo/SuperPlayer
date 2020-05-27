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

import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.player.SuperAudioPlayer;
import com.kingbogo.superplayer.util.SuperLogUtil;
import com.kingbogo.superplayer.view.SuperPlayerView;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2020/3/30
 */
public class AudioFragment extends Fragment {

    private static final String TAG = "AudioFragment";


    private SuperAudioPlayer mSuperAudionPlayer;

    public static AudioFragment newInstance() {
        AudioFragment fragment1 = new AudioFragment();
        Bundle bundle = new Bundle();
        fragment1.setArguments(bundle);
        return fragment1;
    }


    @Override
    public void onDestroy() {
        SuperLogUtil.d(TAG, "_onDestroy()...");
        super.onDestroy();
        if (mSuperAudionPlayer != null) {
            mSuperAudionPlayer.release();
            mSuperAudionPlayer = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SuperLogUtil.d(TAG, "_onDestroy()...");
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        mSuperAudionPlayer = new SuperAudioPlayer(getContext());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        play();
    }

    public void play() {
        ////        String url = "/data/data/com.kingbogo.superplayer.demo/cache/bh_1_1.mp4";
        //        SuperPlayerModel model = new SuperPlayerModel(Constants.VIDEO_URL_4);
        //        model.isLoop = true;
        //        mSuperPlayer.playWithModel(model);

        mSuperAudionPlayer.playWithUrl("https://resource.ddkt365.com/ddktRes/voice/point/word2.mp3",
                new SuperPlayerListener() {
                    @Override
                    public void onSuperPlayerStateChanged(SuperPlayerModel playerModel, SuperPlayerState playerState) {
                        SuperLogUtil.i(TAG, "onSuperPlayerStateChanged -> current state: " + playerState);
                    }

                    @Override
                    public void onSuperPlayerProgress(SuperPlayerModel playerModel, long currentDuration, long totalDuration) {
                    }
                });
    }


}
