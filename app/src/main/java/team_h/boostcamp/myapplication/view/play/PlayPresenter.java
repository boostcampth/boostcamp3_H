package team_h.boostcamp.myapplication.view.play;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.R;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.Recommendation;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.utils.ResourceSendUtil;
import team_h.boostcamp.myapplication.view.adapter.AdapterContract;
import team_h.boostcamp.myapplication.view.memories.MemoriesCardAdapter;
import team_h.boostcamp.myapplication.view.memories.MemoriesContractor;

public class PlayPresenter implements PlayContractor.Presenter {
    private static final String TAG = "PlayPresenter";
    private PlayContractor.View view;
    private AppDatabase appDatabase;
    private PlayDiaryAdapter playDiaryAdapter;
    private MediaPlayerWrapper mediaPlayerWrapper;
    private CompositeDisposable compositeDisposable;

    PlayPresenter(PlayContractor.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {

    }

    public void setAppDatabase(AppDatabase database) {
        appDatabase = database;
    }

    public void setPlayDiaryAdapter(PlayDiaryAdapter playDiaryAdapter) {
        this.playDiaryAdapter = playDiaryAdapter;
    }

    public void setMediaPlayer(MediaPlayerWrapper mediaPlayerWrapper) {
        this.mediaPlayerWrapper = mediaPlayerWrapper;
    }

    public void loadData(int MemoryId){
        appDatabase.appDao().loadSelectedDiayLista(MemoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    playDiaryAdapter.addItems(list);
                    mediaPlayerWrapper.setPlayList(list);
                    Log.d(TAG, "loadData: " + list.size());
                });
    }

    public void onPlayDiaryList(){
        if(!mediaPlayerWrapper.isPlaying()){
            mediaPlayerWrapper.playList();
        }else{
            mediaPlayerWrapper.stopList();
        }
    }

}
