package com.kingbogo.superplayer.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kingbogo.superplayer.demo.util.Constants;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.player.SuperAudioPlayer;
import com.kingbogo.superplayer.util.SuperLogUtil;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener, SuperPlayerListener {

    private static final String TAG = "AudioActivity";

    private SuperAudioPlayer superAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        initView();

        superAudioPlayer = new SuperAudioPlayer(this);
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
                toast(currentDuration + "/" + totalDuration);
                SuperLogUtil.i("currentDuration: " + currentDuration + ", totalDuration: " + totalDuration);
                break;

            case R.id.audio_play_is_playing_btn:
                boolean isPlaying = superAudioPlayer.isPlaying();
                String logInfo = "isPlaying: " + isPlaying;
                SuperLogUtil.i(logInfo);
                toast(logInfo);
                break;

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

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
