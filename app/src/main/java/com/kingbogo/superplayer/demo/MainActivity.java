package com.kingbogo.superplayer.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.main_audio_btn).setOnClickListener(this);
        findViewById(R.id.main_video_btn).setOnClickListener(this);
        findViewById(R.id.main_player_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_audio_btn:
                startActivity(new Intent(this, AudioActivity.class));
                break;

            case R.id.main_video_btn:
                startActivity(new Intent(this, VideoActivity.class));
                break;

            case R.id.main_player_btn:
                startActivity(new Intent(this, PlayerActivity.class));
                break;

            default:
                break;
        }
    }

}
