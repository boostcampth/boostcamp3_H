package teamh.boostcamp.myapplication.view.setting;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;
import teamh.boostcamp.myapplication.data.repository.firebase.FirebaseRepository;

class SettingPresenter {

    private static final String TAG = "SettingPresenter";

    private SettingView view;
    private SettingView settingView;
    private RecallRepository recallRepository;
    private DiaryRepository diaryRepository;
    private CompositeDisposable compositeDisposable;
    private SharedPreferenceManager sharedPreferenceManager;

    private FirebaseRepository firebaseRepository;

    SettingPresenter(@NonNull SettingView view,
                     @NonNull RecallRepository recallRepository,
                     @NonNull DiaryRepository diaryRepository,
                     @NonNull FirebaseRepository firebaseRepository) {
        this.settingView = view;
        this.recallRepository = recallRepository;
        this.diaryRepository = diaryRepository;
        this.firebaseRepository = firebaseRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    void deleteDiary() {
        compositeDisposable.add(
                diaryRepository.loadAll()
                        .map(diary -> {
                            Log.d(TAG, "deleteDiary: " + diary.getRecordFilePath());
                            File file = new File(diary.getRecordFilePath());
                            file.delete();

                            return diary;
                        })
                        .flatMapCompletable(diary -> diaryRepository.deleteDiary(diary.getId()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
        sharedPreferenceManager.removeLastDiarySaveTime();
    }
    void deleteRecall() {
        compositeDisposable.add(
                recallRepository
                        .deleteAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> view.showInitializationMessage(), throwable -> Log.d(TAG, "deleteRecall: " + throwable.getStackTrace()))
        );
    }

    void onDestroy() {
        compositeDisposable.clear();
        diaryRepository = null;
        recallRepository = null;
    }


    void backupLocalDataToFirebaseRepository() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            settingView.showDialog();

            compositeDisposable.add(firebaseRepository.loadAllDiaryId()
                    .flatMapMaybe(diaryRepository::loadNotBackupDiaryList)
                    .flatMapSingle(firebaseRepository::uploadRecordFile)
                    .flatMapCompletable(firebaseRepository::insertDiaries)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                settingView.dismissDialog();
                                settingView.showBackUpSuccessMsg();
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                settingView.dismissDialog();
                                settingView.showBackUpFailMsg();
                            }));
        } else {
            settingView.showNotLoginMsg();
        }
    }

    void downloadAllBackupFilesFromFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


            settingView.showDialog();

            compositeDisposable.add(Single.zip(firebaseRepository.loadAllDiaryList(), diaryRepository.loadAllDiaryEntityList(),
                    (remoteEntityList, localEntityList) -> {
                        final int size = localEntityList.size();
                        for (int i = 0; i < size; ++i) {
                            remoteEntityList.remove(localEntityList.get(i));
                        }
                        return remoteEntityList;
                    }).flatMapObservable(Observable::fromIterable)
                    .toList()
                    .flatMap(firebaseRepository::downloadRecordFile)
                    .flatMapCompletable(diaryEntityList ->
                            diaryRepository.insertDiary(diaryEntityList.toArray(new DiaryEntity[diaryEntityList.size()])))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        settingView.dismissDialog();
                        settingView.showLoadSuccessMsg();
                    }, throwable -> {
                        settingView.dismissDialog();
                        settingView.showLoadFailMsg();
                    }));
        } else {
            settingView.showNotLoginMsg();
        }
    }

}
