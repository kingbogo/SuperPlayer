package com.kingbogo.superplayer.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kingbogo.superplayer.controller.SuperPlayerController;
import com.kingbogo.superplayer.demo.util.Constants;
import com.kingbogo.superplayer.demo.util.ToastUtil;
import com.kingbogo.superplayer.listener.SuperPlayerListener;
import com.kingbogo.superplayer.listener.SuperPlayerQueueListener;
import com.kingbogo.superplayer.model.SuperConstants;
import com.kingbogo.superplayer.model.SuperPlayerModel;
import com.kingbogo.superplayer.model.SuperPlayerState;
import com.kingbogo.superplayer.util.SuperLogUtil;
import com.kingbogo.superplayer.view.SuperPlayerView;

import java.util.ArrayList;
import java.util.List;


public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SuperPlayerListener, SuperPlayerQueueListener {
    
    private static final String TAG = "PlayerActivity";
    
    private SuperPlayerView superPlayerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
    }
    
    private void initView() {
        superPlayerView = findViewById(R.id.player_super_player_view);
        superPlayerView.setSuperPlayerModel(new SuperPlayerModel(Constants.VIDEO_URL_3, TAG, "我是标题3"));
        superPlayerView.setPlayerController(new SuperPlayerController(this));
        
        findViewById(R.id.player_play_1_btn).setOnClickListener(this);
        findViewById(R.id.player_play_2_btn).setOnClickListener(this);
        findViewById(R.id.player_play_3_btn).setOnClickListener(this);
        
        findViewById(R.id.player_play_mode_default_btn).setOnClickListener(this);
        findViewById(R.id.player_play_mode_16_9_btn).setOnClickListener(this);
        findViewById(R.id.player_play_mode_4_3_btn).setOnClickListener(this);
        findViewById(R.id.player_play_mode_fit_xy_btn).setOnClickListener(this);
        findViewById(R.id.player_play_mode_original_btn).setOnClickListener(this);
        findViewById(R.id.player_play_mode_center_crop_btn).setOnClickListener(this);
        
        findViewById(R.id.player_play_list_btn).setOnClickListener(this);
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
            case R.id.player_play_1_btn:
                superPlayerView.playWithUrl(Constants.VIDEO_URL_2);
                break;
            
            case R.id.player_play_2_btn:
                SuperPlayerModel playerModel2 = new SuperPlayerModel(Constants.VIDEO_URL_2, TAG, "我是标题2");
                playerModel2.tag = "Test2";
                playerModel2.isNeedProgressCallback = true;
                playerModel2.setPlayerListener(this);
                superPlayerView.playWithModel(playerModel2);
                break;
            
            case R.id.player_play_3_btn:
                SuperPlayerModel playerModel3 = new SuperPlayerModel(Constants.VIDEO_URL_3, TAG, "我是标题3");
                playerModel3.tag = "Test3";
                playerModel3.startPlayPositionMs = 20 * 1000;
                playerModel3.isPauseAfterLockScreen = false;
                playerModel3.isGoneAfterComplete = true;
                playerModel3.setPlayerListener(this);
                superPlayerView.playWithModel(playerModel3);
                break;
            
            case R.id.player_play_mode_default_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_DEFAULT);
                break;
            
            case R.id.player_play_mode_16_9_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_16_9);
                break;
            
            case R.id.player_play_mode_4_3_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_4_3);
                break;
            
            case R.id.player_play_mode_fit_xy_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_FIT_XY);
                break;
            
            case R.id.player_play_mode_original_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_ORIGINAL);
                break;
            
            case R.id.player_play_mode_center_crop_btn:
                superPlayerView.setRenderMode(SuperConstants.REND_MODE_CENTER_CROP);
                break;
            
            case R.id.player_play_list_btn:
                List<SuperPlayerModel> modelList = new ArrayList<>();
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_1, TAG, "列表1"));
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_2, TAG, "列表2"));
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_3, TAG, "列表3"));
                modelList.add(new SuperPlayerModel(Constants.VIDEO_URL_4, TAG, "列表4"));
                superPlayerView.playWithModelList(modelList, 0, SuperConstants.MEDIA_QUEUE_MODE_SINGLE_LOOP, this);
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
    
    // ------------------------------------------------------ @ SuperPlayerQueueListener
    
    @Override
    public void onSuperPlayerQueueIndexUpdate(int index, SuperPlayerModel playerModel) {
        SuperLogUtil.i(TAG, "==> onSuperPlayerQueueIndexUpdate(), index: " + index + ", playerModel: " + playerModel);
        ToastUtil.toast(this, "当前播放 index: " + index);
    }
    
}
