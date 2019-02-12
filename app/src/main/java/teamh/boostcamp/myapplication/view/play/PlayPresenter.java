package teamh.boostcamp.myapplication.view.play;

import io.reactivex.disposables.CompositeDisposable;

public class PlayPresenter{
    private PlayView view;
    private RecordPlayer recordPlayer;

    PlayPresenter(RecordPlayer recordPlayer, PlayView view) {
        this.recordPlayer = recordPlayer;
        this.view = view;
    }

    public void loadData(int MemoryId) {
    }

    public void playMemory() {
        if (!recordPlayer.isPlaying()) {
            recordPlayer.playList();
            view.makeToast("일기를 재생합니다.");
        } else {
            stopMemory();
        }
    }

    public void stopMemory() {
        if(recordPlayer.isPlaying()){
            recordPlayer.stopList();
            view.makeToast("재생을 정지합니다.");
        }
    }

}
