package com.androidwebviewdemo;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        MediaController mediaController=new MediaController(this);
        VideoView videoView= (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("http://i0.hqyxjy.com/files/1.%E6%95%99%E5%B8%88%E5%A6%82%E4%BD%95%E4%B8%8B%E8%BD%BD%E3%80%81%E6%B3%A8%E5%86%8C%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%B9%B6%E5%AE%8C%E6%88%90%E5%BC%80%E8%AF%BE%E8%AE%BE%E7%BD%AE.mp4"));
//        videoView.start();
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.start();
    }
}
