package com.johnson.healthmonitor.media;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.johnson.healthmonitor.MainActivity;

import java.io.IOException;

public class MediaManager {
    public static  MediaPlayer player;
    static {
        player =  new MediaPlayer();
        mAudioManager = (AudioManager) MainActivity.getActivity().getSystemService(MainActivity.AUDIO_SERVICE);
        assetManager = MainActivity.getActivity().getAssets();
    }

    private static AudioManager mAudioManager;
    private static AssetManager assetManager;

    public static void playEffert(String fileName){
        try {
            if (player!=null)
                player.release();
            player =  new MediaPlayer();
            int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
            int maxVolume = mAudioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小

            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(fileDescriptor.getFileDescriptor());
//            player.setLooping(true);
            player.prepare();
            player.start();
//            player.isPlaying()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPlaying(){
        if(player!=null)
            return player.isPlaying();
        return false;
    }

    public static void stop(){
        if (player!=null) {
            player.release();
            player = null;
        }
    }
}
