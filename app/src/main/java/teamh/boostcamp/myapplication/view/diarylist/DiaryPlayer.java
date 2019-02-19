package teamh.boostcamp.myapplication.view.diarylist;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;

public interface DiaryPlayer {

    void play(@NonNull final String filePath);

    void setCompletionListener(@NonNull MediaPlayer.OnCompletionListener completionListener);

    void stop();

    void releasePlayer();
}
