package com.makitryuk.testingapp.Models;

import android.content.Context;
import android.media.MediaPlayer;

import com.makitryuk.testingapp.R;

import java.io.IOException;

public class Music extends Thread {

    MediaPlayer mPlayer;

    public Music() {
        
    }

    @Override
    public void run() {
        super.run();
        try {
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Music(Context context) {
        mPlayer= MediaPlayer.create(context, R.raw.music);
    }
}
