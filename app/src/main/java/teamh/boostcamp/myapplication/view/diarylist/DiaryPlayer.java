package teamh.boostcamp.myapplication.view.diarylist;

import android.media.MediaPlayer;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DiaryPlayer {

    @Nullable
    private MediaPlayer mediaPlayer;
    @Nullable
    private MediaPlayer.OnCompletionListener completionListener;
    private boolean isPlaying = false;

    DiaryPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void play(@NonNull final String filePath) {
        if(!isPlaying) {
            try {
                isPlaying = true;
                mediaPlayer.reset();
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        } else {
            stop();
        }
    }

    void setCompletionListener(@NonNull MediaPlayer.OnCompletionListener completionListener) {
        this.completionListener = completionListener;
        mediaPlayer.setOnCompletionListener(completionListener);
    }

    void stop() {
        if(isPlaying) {
            mediaPlayer.stop();
            isPlaying = false;
        }
    }

    void releasePlayer() {
        if(isPlaying) {
            stop();
        }
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        isPlaying = false;
        mediaPlayer = null;
    }
}
