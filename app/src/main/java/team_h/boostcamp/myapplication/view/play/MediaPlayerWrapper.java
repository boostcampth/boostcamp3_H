package team_h.boostcamp.myapplication.view.play;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import team_h.boostcamp.myapplication.model.Diary;

public class MediaPlayerWrapper {
    private static final String TAG = "MediaPlayerWrapper";
    private MediaPlayer mediaPlayer;
    private List<Diary> playList;
    private boolean playing = false;
    private int count = 0;

    public MediaPlayerWrapper() {
        if (mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
        }
    }

    void setPlayList(List<Diary> playList) {
        this.playList = playList;
    }

    void playList() {

        if (!playing) {
            try {
                Log.d(TAG, "playList: ~~~~~" + playList.get(0).getRecordFilePath());
                Log.d(TAG, "playList: ~~~~~" + Environment.getExternalStorageDirectory()+"/Download/first.mp3");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/Download/first.mp3");
                mediaPlayer.prepare();
            } catch (IOException e) {
                Log.d(TAG, "playList: IOException" + e.getStackTrace());
            }

            mediaPlayer.setOnCompletionListener(mp -> {
                if (playList.size() - 1 > count) {
                    count++;

                    try {
                        mediaPlayer.reset();
                        Log.d(TAG, "playList: " + count + " " + playList.get(count).getRecordFilePath());
                        mediaPlayer.setDataSource(playList.get(count).getRecordFilePath());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        Log.d(TAG, "playList: IOException" + e.getStackTrace());
                    }

                    mediaPlayer.start();
                } else {

                    stopList();
                }
            });

            mediaPlayer.start();
        } else {
            stopList();
        }

    }

    void stopList() {

        if(playing){
            count = 0;
            mediaPlayer.stop();
            playing = false;
        }
    }

    boolean isPlaying(){
        return playing;
    }


}
