package team_h.boostcamp.myapplication.view.play;

import java.util.List;

import team_h.boostcamp.myapplication.model.Diary;

public interface RecordPlayer {

    void setList(List<Diary> list);

    void playList();

    void stopList();

    boolean isPlaying();
}
