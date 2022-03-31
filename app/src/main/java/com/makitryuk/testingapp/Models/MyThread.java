package com.makitryuk.testingapp.Models;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.makitryuk.testingapp.R;

public class MyThread extends Thread {
    private static final String TAG = "shaman";
    MediaPlayer mPlayer;
    Context context;
    public MyThread(Context ct){
        this.context = ct;
    }

    public void run() {
        Log.d(TAG, "Thread is running...");
        mPlayer= MediaPlayer.create(context, R.raw.music);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }
        });
        mPlayer.start();
    }
    private void stopPlay(){
        mPlayer.stop();
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
        }
        catch (Throwable t) {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
