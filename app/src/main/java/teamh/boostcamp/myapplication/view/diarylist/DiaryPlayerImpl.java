package teamh.boostcamp.myapplication.view.diarylist;

import android.media.MediaPlayer;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DiaryPlayerImpl implements DiaryPlayer {

    @Nullable
    private MediaPlayer mediaPlayer;
    @Nullable
    private MediaPlayer.OnCompletionListener completionListener;
    private boolean isPlaying = false;

    DiaryPlayerImpl() {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void play(@NonNull String filePath) {
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

    @Override
    public void setCompletionListener(@NonNull MediaPlayer.OnCompletionListener completionListener) {
        this.completionListener = completionListener;
        mediaPlayer.setOnCompletionListener(completionListener);
    }

    @Override
    public void stop() {
        if(isPlaying) {
            mediaPlayer.stop();
            isPlaying = false;
        }
    }

    @Override
    public void releasePlayer() {
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
