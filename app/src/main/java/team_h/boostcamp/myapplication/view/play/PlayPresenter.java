package team_h.boostcamp.myapplication.view.play;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;

public class PlayPresenter implements PlayContractor.Presenter {
    private PlayContractor.View view;
    private AppDatabase appDatabase;
    private RecordPlayer recordPlayer;
    private CompositeDisposable compositeDisposable;

    PlayPresenter(AppDatabase appDatabase, RecordPlayerImpl recordPlayer, PlayContractor.View view) {
        this.appDatabase = appDatabase;
        this.recordPlayer = recordPlayer;
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {
        recordPlayer.stopList();
        recordPlayer = null;
        appDatabase = null;
    }

    @Override
    public void loadData(int MemoryId) {
        compositeDisposable.add(
                appDatabase.appDao().loadSelectedDiayLista(MemoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                            view.setDiaryList(list);
                            recordPlayer.setList(list);
                        })
        );
    }

    @Override
    public void playDiaryList() {
        if (!recordPlayer.isPlaying()) {
            recordPlayer.playList();
            view.makeToast("일기를 재생합니다.");
        } else {
            stopDiaryList();
        }
    }

    @Override
    public void stopDiaryList() {
        if(recordPlayer.isPlaying()){
            recordPlayer.stopList();
            view.makeToast("재생을 정지합니다.");
        }
    }

}
