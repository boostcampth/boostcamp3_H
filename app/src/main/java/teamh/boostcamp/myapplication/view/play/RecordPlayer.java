package teamh.boostcamp.myapplication.view.play;

import java.util.List;

import teamh.boostcamp.myapplication.data.model.LegacyDiary;

public interface RecordPlayer {

    void setList(List<LegacyDiary> list);

    void playList();

    void stopList();

    boolean isPlaying();
}
