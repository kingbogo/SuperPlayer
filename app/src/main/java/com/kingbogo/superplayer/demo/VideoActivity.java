package com.kingbogo.superplayer.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kingbogo.superplayer.demo.util.Constants;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.SuperLogUtil;
import com.kingbogo.superplayer.view.SuperPlayerView;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener, SuperPlayerListener {

    private static final String TAG = "VideoActivity";

    private SuperPlayerView superPlayerView;

    private Fragment1 fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
    }

    private void initView() {
        superPlayerView = findViewById(R.id.video_super_player_view);

        findViewById(R.id.video_play_1_btn).setOnClickListener(this);
        findViewById(R.id.video_play_2_btn).setOnClickListener(this);
        findViewById(R.id.video_play_3_btn).setOnClickListener(this);
        findViewById(R.id.video_play_loop_btn).setOnClickListener(this);
        findViewById(R.id.video_play_local_btn).setOnClickListener(this);
        findViewById(R.id.video_play_15_btn).setOnClickListener(this);
        findViewById(R.id.video_pause_btn).setOnClickListener(this);
        findViewById(R.id.video_resume_btn).setOnClickListener(this);
        findViewById(R.id.video_stop_btn).setOnClickListener(this);
        findViewById(R.id.video_stop_clear_btn).setOnClickListener(this);
        findViewById(R.id.video_replay_btn).setOnClickListener(this);
        findViewById(R.id.video_replay_seek_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mute_yes_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mute_no_btn).setOnClickListener(this);
        findViewById(R.id.video_play_duration_btn).setOnClickListener(this);

        findViewById(R.id.video_play_mode_default_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mode_16_9_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mode_4_3_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mode_fit_xy_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mode_original_btn).setOnClickListener(this);
        findViewById(R.id.video_play_mode_center_crop_btn).setOnClickListener(this);

        findViewById(R.id.video_play_is_playing_btn).setOnClickListener(this);
        findViewById(R.id.video_play_fragment_btn).setOnClickListener(this);

        fragment1 = Fragment1.newInstance("Fragment中播放");
        getSupportFragmentManager().beginTransaction().add(R.id.video_container, fragment1).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        superPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        superPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (superPlayerView != null) {
            superPlayerView.release();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.video_play_1_btn:
                superPlayerView.playWithUrl(Constants.VIDEO_URL_1);
                break;

            case R.id.video_play_2_btn:
                SuperPlayerModel playerModel2 = new SuperPlayerModel(Constants.VIDEO_URL_2);
                playerModel2.tag = "Test2";
                playerModel2.isNeedProgressCallback = true;
                playerModel2.setPlayerListener(this);
                playerModel2.startPlayPositionMs = 500 * 1000;
                superPlayerView.playWithModel(playerModel2);
                break;

            case R.id.video_play_3_btn:
                SuperPlayerModel playerModel3 = new SuperPlayerModel(Constants.VIDEO_URL_3);
                playerModel3.tag = "Test3";
                playerModel3.startPlayPositionMs = 20 * 1000;
                playerModel3.isPauseAfterLockScreen = false;
                playerModel3.isGoneAfterComplete = true;
                playerModel3.setPlayerListener(this);
                superPlayerView.playWithModel(playerModel3);
                break;

            case R.id.video_play_loop_btn:
                superPlayerView.setLoop(true);
                break;

            case R.id.video_play_local_btn:
                // 请自行处理"动态权限"
//                String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/bh_1_1.mp4";

//                String url = getExternalCacheDir().getAbsolutePath() + "/bh_1_1.mp4";

                String url = "/data/data/com.kingbogo.superplayer.demo/cache/bh_1_1.mp4";

                SuperLogUtil.d("url ===> " + url);

                SuperPlayerModel model = new SuperPlayerModel(url);
                model.isLoop = true;
                superPlayerView.playWithModel(model);
                break;

            case R.id.video_play_15_btn:
                superPlayerView.seek(15);
                break;

            case R.id.video_pause_btn:
                superPlayerView.pause();
                int audioSessionId = superPlayerView.getAudioSessionId();
                SuperLogUtil.d("audioSessionId => " + audioSessionId);
                break;

            case R.id.video_resume_btn:
                superPlayerView.resume();
                break;

            case R.id.video_stop_btn:
                superPlayerView.stopPlay(false);
                break;

            case R.id.video_stop_clear_btn:
                superPlayerView.stopPlay(true);
                break;

            case R.id.video_replay_btn:
                superPlayerView.replay();
                break;

            case R.id.video_replay_seek_btn:
                superPlayerView.replay4Seek();
                break;

            case R.id.video_play_mute_yes_btn:
                superPlayerView.setMute(true);
                break;

            case R.id.video_play_mute_no_btn:
                superPlayerView.setMute(false);
                break;

            case R.id.video_play_duration_btn:
                long currentDuration = superPlayerView.getCurrentDuration();
                long totalDuration = superPlayerView.getTotalDuration();
                toast(currentDuration + "/" + totalDuration);
                SuperLogUtil.i("currentDuration: " + currentDuration + ", totalDuration: " + totalDuration);
                break;

            case R.id.video_play_mode_default_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_DEFAULT);
                break;

            case R.id.video_play_mode_16_9_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_16_9);
                break;

            case R.id.video_play_mode_4_3_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_4_3);
                break;

            case R.id.video_play_mode_fit_xy_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_FIT_XY);
                break;

            case R.id.video_play_mode_original_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_ORIGINAL);
                break;

            case R.id.video_play_mode_center_crop_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_CENTER_CROP);
                break;

            case R.id.video_play_is_playing_btn:
                boolean isPlaying = superPlayerView.isPlaying();
                String logInfo = "isPlaying: " + isPlaying;
                SuperLogUtil.i(logInfo);
                toast(logInfo);
                break;

            case R.id.video_play_fragment_btn:
                fragment1.play();
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
