package teamh.boostcamp.myapplication.view.play;

class PlayPresenter {
    private PlayView view;
    private RecordPlayer recordPlayer;

    PlayPresenter(RecordPlayer recordPlayer, PlayView view) {
        this.recordPlayer = recordPlayer;
        recordPlayer.setOnChangeStateListener(new RecordPlayerImpl.OnStateChangeListener() {
            @Override
            public void onStop() {
                view.setButtonText(false);
                view.showPlayingState(false);
            }

            @Override
            public void onPlay() {
                view.setButtonText(true);
                view.showPlayingState(true);
            }
        });
        this.view = view;
    }

    void playRecalls() {
        if(recordPlayer.getListSize()==0){
            view.showListSizeError();
            return;
        }
        if (!recordPlayer.isPlaying()) {
            recordPlayer.play();
        } else {
            stopPlaying();
        }
    }

    void stopPlaying() {
        if (recordPlayer.isPlaying()) {
            recordPlayer.stopList();
        }
    }

    void onViewDestroyed() {
        recordPlayer.releasePlayer();
        view = null;
    }
}
