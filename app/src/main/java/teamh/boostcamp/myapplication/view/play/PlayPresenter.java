package teamh.boostcamp.myapplication.view.play;

class PlayPresenter {
    private PlayView view;
    private RecordPlayer recordPlayer;

    PlayPresenter(RecordPlayer recordPlayer, PlayView view) {
        this.recordPlayer = recordPlayer;
        this.view = view;
    }

    void playRecalls() {
        if(recordPlayer.getListSize()==0){
            view.showListSizeError();
            return;
        }
        if (!recordPlayer.isPlaying()) {
            recordPlayer.play();
            view.showPlayingState(recordPlayer.isPlaying());
        } else {
            stopPlaying();
        }
    }

    void stopPlaying() {
        if (recordPlayer.isPlaying()) {
            recordPlayer.stopList();
            view.showPlayingState(recordPlayer.isPlaying());
        }
    }

    void onViewDestroyed() {
        recordPlayer.releasePlayer();
        view = null;
    }
}
