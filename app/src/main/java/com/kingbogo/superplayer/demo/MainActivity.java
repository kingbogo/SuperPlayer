package com.kingbogo.superplayer.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.LogUtil;
import com.kingbogo.superplayer.view.SuperPlayerView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SuperPlayerListener {

    public static final String URL_1 = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
    public static final String URL_2 = "http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4";
    public static final String URL_3 = "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4";
    public static final String URL_4 = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";
    public static final String URL_5 = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4";

    private SuperPlayerView superPlayerView;

//    private Fragment1 fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        superPlayerView = findViewById(R.id.main_super_player_view);

        findViewById(R.id.main_play_1_btn).setOnClickListener(this);
        findViewById(R.id.main_play_2_btn).setOnClickListener(this);
        findViewById(R.id.main_play_3_btn).setOnClickListener(this);
        findViewById(R.id.main_play_loop_btn).setOnClickListener(this);
        findViewById(R.id.main_play_local_btn).setOnClickListener(this);
        findViewById(R.id.main_play_15_btn).setOnClickListener(this);
        findViewById(R.id.main_pause_btn).setOnClickListener(this);
        findViewById(R.id.main_resume_btn).setOnClickListener(this);
        findViewById(R.id.main_stop_btn).setOnClickListener(this);
        findViewById(R.id.main_stop_clear_btn).setOnClickListener(this);
        findViewById(R.id.main_replay_btn).setOnClickListener(this);
        findViewById(R.id.main_replay_seek_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mute_yes_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mute_no_btn).setOnClickListener(this);
        findViewById(R.id.main_play_total_duration_btn).setOnClickListener(this);
        findViewById(R.id.main_play_current_duration_btn).setOnClickListener(this);

        findViewById(R.id.main_play_mode_default_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mode_16_9_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mode_4_3_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mode_fit_xy_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mode_original_btn).setOnClickListener(this);
        findViewById(R.id.main_play_mode_center_crop_btn).setOnClickListener(this);

        findViewById(R.id.main_play_is_playing_btn).setOnClickListener(this);
        findViewById(R.id.main_play_fragment_btn).setOnClickListener(this);

//        fragment1 = Fragment1.newInstance("Fragment中播放");
//        getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragment1).commit();

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
            case R.id.main_play_1_btn:
                superPlayerView.playWithUrl(URL_1);
                break;

            case R.id.main_play_2_btn:
                SuperPlayerModel playerModel2 = new SuperPlayerModel(URL_2);
                playerModel2.tag = "Test2";
                playerModel2.isNeedProgressCallback = true;
                playerModel2.setPlayerListener(this);
                superPlayerView.playWithModel(playerModel2);
                break;

            case R.id.main_play_3_btn:
                SuperPlayerModel playerModel3 = new SuperPlayerModel(URL_3);
                playerModel3.tag = "Test3";
                playerModel3.startPlayPositionMs = 20 * 1000;
                playerModel3.isPauseAfterLockScreen = false;
                playerModel3.isGoneAfterComplete = true;
                playerModel3.setPlayerListener(this);
                superPlayerView.playWithModel(playerModel3);
                break;

            case R.id.main_play_loop_btn:
                superPlayerView.setLoop(true);
                break;

            case R.id.main_play_local_btn:
                // 请自行处理"动态权限"
//                String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/bh_1_1.mp4";

//                String url = getExternalCacheDir().getAbsolutePath() + "/bh_1_1.mp4";

                String url = "/data/data/com.kingbogo.superplayer.demo/cache/bh_1_1.mp4";

                LogUtil.d("url ===> " + url);

                SuperPlayerModel model = new SuperPlayerModel(url);
                model.isLoop = true;
                superPlayerView.playWithModel(model);
                break;

            case R.id.main_play_15_btn:
                superPlayerView.seek(15);
                break;

            case R.id.main_pause_btn:
                superPlayerView.pause();
                int audionSessonId = superPlayerView.getAudioSessionId();
                LogUtil.d("audionSessonId => " + audionSessonId);
                break;

            case R.id.main_resume_btn:
                superPlayerView.resume();
                break;

            case R.id.main_stop_btn:
                superPlayerView.stopPlay(false);
                break;

            case R.id.main_stop_clear_btn:
                superPlayerView.stopPlay(true);
                break;

            case R.id.main_replay_btn:
                superPlayerView.replay();
                break;

            case R.id.main_replay_seek_btn:
                superPlayerView.replay4Seek();
                break;

            case R.id.main_play_mute_yes_btn:
                superPlayerView.setMute(true);
                break;

            case R.id.main_play_mute_no_btn:
                superPlayerView.setMute(false);
                break;

            case R.id.main_play_total_duration_btn:
                long totalDuration = superPlayerView.getTotalDuration();
                LogUtil.i("totalDuration: " + totalDuration);
                break;

            case R.id.main_play_current_duration_btn:
                long currentDuration = superPlayerView.getCurrentDuration();
                LogUtil.i("currentDuration: " + currentDuration);
                break;

            case R.id.main_play_mode_default_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_DEFAULT);
                break;

            case R.id.main_play_mode_16_9_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_16_9);
                break;

            case R.id.main_play_mode_4_3_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_4_3);
                break;

            case R.id.main_play_mode_fit_xy_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_FIT_XY);
                break;

            case R.id.main_play_mode_original_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_ORIGINAL);
                break;

            case R.id.main_play_mode_center_crop_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_CENTER_CROP);
                break;

            case R.id.main_play_is_playing_btn:
                boolean isPlaying = superPlayerView.isPlaying();
                String logInfo = "isPlaying: " + isPlaying;
                LogUtil.i(logInfo);
                toast(logInfo);
                break;

//            case R.id.main_play_fragment_btn:
//                fragment1.play();
//                break;

            default:
                break;
        }
    }

    @Override
    public void onSuperPlayerStateChanged(SuperPlayerModel playerModel, SuperPlayerState playerState) {
        LogUtil.i("_onSuperPlayerStateChanged(), tag: " + playerModel.tag + ", playerState: " + playerState);
        if (playerState == SuperPlayerState.PLAYING) {
            LogUtil.d("开始播放。。。");
        }
    }

    @Override
    public void onSuperPlayerProgress(SuperPlayerModel playerModel, long currentDuration, long totalDuration) {
        LogUtil.v("_onSuperPlayerProgress(), tag: " + playerModel.tag + ", currentDuration: " + currentDuration + ", totalDuration: " + totalDuration);
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
