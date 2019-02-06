package team_h.boostcamp.myapplication.view.play;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;

public class PlayPresenter implements PlayContractor.Presenter {
    private static final String TAG = "PlayPresenter";
    private PlayContractor.View view;
    private AppDatabase appDatabase;
    private PlayDiaryAdapter playDiaryAdapter;
    private MediaPlayerWrapper mediaPlayerWrapper;
    private CompositeDisposable compositeDisposable;

    PlayPresenter(AppDatabase appDatabase, MediaPlayerWrapper mediaPlayerWrapper, PlayContractor.View view) {
        this.appDatabase = appDatabase;
        this.mediaPlayerWrapper = mediaPlayerWrapper;
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {
        mediaPlayerWrapper.stopList();
        mediaPlayerWrapper = null;
        appDatabase = null;
    }

    public void setPlayDiaryAdapter(PlayDiaryAdapter playDiaryAdapter) {
        this.playDiaryAdapter = playDiaryAdapter;
    }

    public void loadData(int MemoryId) {
        compositeDisposable.add(
                appDatabase.appDao().loadSelectedDiayLista(MemoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                            playDiaryAdapter.addItems(list);
                            mediaPlayerWrapper.setPlayList(list);
                        })
        );
    }

    public void onPlayDiaryList() {
        if (!mediaPlayerWrapper.isPlaying()) {
            mediaPlayerWrapper.playList();
        } else {
            mediaPlayerWrapper.stopList();
        }
    }

    void stopPlay() {
        mediaPlayerWrapper.stopList();
    }

}
