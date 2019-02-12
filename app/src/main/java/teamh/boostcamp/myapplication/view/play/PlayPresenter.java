package teamh.boostcamp.myapplication.view.play;

class PlayPresenter {
    private PlayView view;
    private RecordPlayer recordPlayer;

    PlayPresenter(RecordPlayer recordPlayer, PlayView view) {
        this.recordPlayer = recordPlayer;
        this.view = view;
    }

    void playMemory() {
        if (!recordPlayer.isPlaying()) {
            recordPlayer.playList();
            view.showPlayingState(recordPlayer.isPlaying());
        } else {
            stopMemory();
        }
    }

    void stopMemory() {
        if (recordPlayer.isPlaying()) {
            recordPlayer.stopList();
            view.showPlayingState(recordPlayer.isPlaying());
        }
    }
}
