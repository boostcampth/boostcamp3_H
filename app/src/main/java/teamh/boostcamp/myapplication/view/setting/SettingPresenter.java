package teamh.boostcamp.myapplication.view.setting;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;
import teamh.boostcamp.myapplication.data.repository.firebase.FirebaseRepository;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.utils.EventBus;

class SettingPresenter {

    private static final String TAG = "SettingPresenter";

    private SettingView settingView;
    private DiaryRepository diaryRepository;
    private CompositeDisposable compositeDisposable;
    private RecallRepository recallRepository;
    private FirebaseRepository firebaseRepository;

    SettingPresenter(@NonNull SettingView view,
                     @NonNull DiaryRepository diaryRepository,
                     @NonNull FirebaseRepository firebaseRepository,
                     @NonNull RecallRepository recallRepository) {
        this.settingView = view;
        this.diaryRepository = diaryRepository;
        this.firebaseRepository = firebaseRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.recallRepository = recallRepository;
    }

    void onDestroy() {
        compositeDisposable.clear();
        diaryRepository = null;
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
                        EventBus.sendEvent(Event.BACK_UP_COMPLETE);
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

    /*
    void deleteAllDiary(){
        compositeDisposable.add(diaryRepository.deleteAllDiaries().observeOn(AndroidSchedulers.mainThread()).subscribe());
    }
    */

    Completable initialize() {
        return recallRepository.deleteAll()
                .andThen(diaryRepository.deleteAllDiaries())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
