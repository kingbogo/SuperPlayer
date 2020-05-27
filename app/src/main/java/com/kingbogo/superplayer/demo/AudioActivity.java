package com.kingbogo.superplayer.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kingbogo.superplayer.demo.util.Constants;
import com.kingbogo.superplayer.demo.util.ToastUtil;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.listener.SuperPlayerQueueListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.player.SuperAudioPlayer;
import com.kingbogo.superplayer.util.SuperLogUtil;

import java.util.ArrayList;
import java.util.List;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener, SuperPlayerListener, SuperPlayerQueueListener {
    
    private static final String TAG = "AudioActivity";
    
    private SuperAudioPlayer superAudioPlayer;

    private AudioFragment audioFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        initView();
        
        superAudioPlayer = new SuperAudioPlayer(this);

        audioFragment = AudioFragment.newInstance();
    }
    
    private void initView() {
        findViewById(R.id.audio_play_1_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_2_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_3_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_loop_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_15_btn).setOnClickListener(this);
        findViewById(R.id.audio_pause_btn).setOnClickListener(this);
        findViewById(R.id.audio_resume_btn).setOnClickListener(this);
        findViewById(R.id.audio_stop_btn).setOnClickListener(this);
        findViewById(R.id.audio_replay_btn).setOnClickListener(this);
        findViewById(R.id.audio_replay_seek_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_mute_yes_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_mute_no_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_duration_btn).setOnClickListener(this);
        findViewById(R.id.audio_play_is_playing_btn).setOnClickListener(this);
        
        findViewById(R.id.audio_play_list_btn).setOnClickListener(this);

        findViewById(R.id.audio_play_fragment_btn).setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        superAudioPlayer.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        superAudioPlayer.onPause();
    }
    
    @Override
    protected void onDestroy() {
        if (superAudioPlayer != null) {
            superAudioPlayer.release();
        }
        super.onDestroy();
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.audio_play_1_btn:
                superAudioPlayer.playWithUrl(Constants.VIDEO_URL_1);
                break;
            
            case R.id.audio_play_2_btn:
                SuperPlayerModel playerModel2 = new SuperPlayerModel(Constants.VIDEO_URL_2);
                playerModel2.tag = "Test2";
                playerModel2.isNeedProgressCallback = true;
                playerModel2.setPlayerListener(this);
                superAudioPlayer.playWithModel(playerModel2);
                break;
            
            case R.id.audio_play_3_btn:
                SuperPlayerModel playerModel3 = new SuperPlayerModel(Constants.VIDEO_URL_3);
                playerModel3.tag = "Test3";
                playerModel3.startPlayPositionMs = 20 * 1000;
                playerModel3.isPauseAfterLockScreen = false;
                playerModel3.isGoneAfterComplete = true;
                playerModel3.setPlayerListener(this);
                superAudioPlayer.playWithModel(playerModel3);
                break;
            
            case R.id.audio_play_loop_btn:
                superAudioPlayer.setLoop(true);
                break;
            
            case R.id.audio_play_15_btn:
                superAudioPlayer.seek(15);
                break;
            
            case R.id.audio_pause_btn:
                superAudioPlayer.pause();
                int audioSessionId = superAudioPlayer.getAudioSessionId();
                SuperLogUtil.d("audioSessionId => " + audioSessionId);
                break;
            
            case R.id.audio_resume_btn:
                superAudioPlayer.resume();
                break;
            
            case R.id.audio_stop_btn:
                superAudioPlayer.stop(false);
                break;
            
            case R.id.audio_replay_btn:
                superAudioPlayer.replay();
                break;
            
            case R.id.audio_replay_seek_btn:
                superAudioPlayer.replay4Seek();
                break;
            
            case R.id.audio_play_mute_yes_btn:
                superAudioPlayer.setMute(true);
                break;
            
            case R.id.audio_play_mute_no_btn:
                superAudioPlayer.setMute(false);
                break;
            
            case R.id.audio_play_duration_btn:
                long currentDuration = superAudioPlayer.getCurrentDuration();
                long totalDuration = superAudioPlayer.getTotalDuration();
                ToastUtil.toast(this,currentDuration + "/" + totalDuration);
                SuperLogUtil.i("currentDuration: " + currentDuration + ", totalDuration: " + totalDuration);
                break;
            
            case R.id.audio_play_is_playing_btn:
                boolean isPlaying = superAudioPlayer.isPlaying();
                String logInfo = "isPlaying: " + isPlaying;
                SuperLogUtil.i(logInfo);
                ToastUtil.toast(this, logInfo);
                break;
            
            case R.id.audio_play_list_btn:
                List<SuperPlayerModel> modelList = new ArrayList<>();
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_1, TAG, "列表1"));
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_2, TAG, "列表2"));
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_3, TAG, "列表3"));
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_4, TAG, "列表4"));
                superAudioPlayer.playWithModelList(modelList, 0, SuperConstants.MEDIA_QUEUE_MODE_LIST_LOOP, this);
                break;

            case R.id.audio_play_fragment_btn:{
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!audioFragment.isAdded()) {
                    transaction.add(R.id.audio_fl, audioFragment, audioFragment.getClass().getName());
                }
                transaction.commitAllowingStateLoss();

                break;
            }
            
            default:
                break;
        }
    }
    
    @Override
    public void onSuperPlayerStateChanged(SuperPlayerModel playerModel, SuperPlayerState playerState) {
        SuperLogUtil.i("_onSuperPlayerStateChanged(), tag: " + playerModel.tag + ", playerState: " + playerState);
    }
    
    @Override
    public void onSuperPlayerProgress(SuperPlayerModel playerModel, long currentDuration, long totalDuration) {
        SuperLogUtil.v("_onSuperPlayerProgress(), tag: " + playerModel.tag + ", currentDuration: " + currentDuration + ", totalDuration: " + totalDuration);
    }
    
    @Override
    public void onSuperPlayerQueueIndexUpdate(int index, SuperPlayerModel playerModel) {
        SuperLogUtil.i(TAG, "==> onSuperPlayerQueueIndexUpdate(), index: " + index + ", playerModel: " + playerModel);
        ToastUtil.toast(this, "当前播放 index: " + index);
    }
    
}
