package teamh.boostcamp.myapplication.view.play;

import java.util.List;

import teamh.boostcamp.myapplication.data.model.Diary;

public interface RecordPlayer {

    void setList(List<Diary> list);

    void playList();

    void stopList();

    boolean isPlaying();
}
