package teamh.boostcamp.myapplication.view.setting;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import androidx.annotation.NonNull;
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
                    .flatMap(diaryRepository::loadNotBackupDiaryList)
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
            settingView.showBackUpFailMsg();
        }
    }

    void downloadAllBackupFilesFromFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            compositeDisposable.add(diaryRepository.deleteAllDiaries()
                    .andThen(firebaseRepository.loadAllDiaryList())
                    .flatMapSingle(firebaseRepository::downloadRecordFile)
                    .flatMapCompletable(diaryEntities ->
                            diaryRepository.insertDiary(diaryEntities.toArray(new DiaryEntity[diaryEntities.size()])))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        settingView.dismissDialog();
                        settingView.showLoadSuccessMsg();
                    }, throwable -> {
                        settingView.dismissDialog();
                        settingView.showLoadFailMsg();
                    }));
        }
    }

}
