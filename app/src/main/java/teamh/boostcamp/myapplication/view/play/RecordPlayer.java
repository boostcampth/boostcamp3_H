package teamh.boostcamp.myapplication.view.play;

import android.media.MediaPlayer;

import java.util.List;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.model.Diary;

public interface RecordPlayer {

    void setList(List<Diary> list);

    void playList();

    void stopList();

    boolean isPlaying();

    void releasePlayer();

    void setOnCompletionListener(@NonNull MediaPlayer.OnCompletionListener onCompletionListener);
}
