package teamh.boostcamp.myapplication.view.setting;

import android.util.Log;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;

public class SettingPresenter {

    private static final String TAG = "SettingPresenter";

    private RecallRepository recallRepository;
    private DiaryRepository diaryRepository;
    private CompositeDisposable compositeDisposable;

    public SettingPresenter(RecallRepository recallRepository, DiaryRepository diaryRepository) {
        this.recallRepository = recallRepository;
        this.diaryRepository = diaryRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    void deleteDiary(){
        compositeDisposable.add(
                diaryRepository.loadAll()
                        .map(diary -> {
                                    File file = new File(diary.getRecordFilePath());
                                    file.delete();

                          return diary;
                        })
                        .flatMapCompletable(diary -> diaryRepository.deleteDiary(diary.getId()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    void deleteRecall(){

        compositeDisposable.add(
                recallRepository
                        .deleteAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {},throwable -> Log.d(TAG, "deleteRecall: " + throwable.getStackTrace()))
        );

    }

    void onDestroy(){
        compositeDisposable.clear();
        diaryRepository = null;
        recallRepository = null;
    }

}
