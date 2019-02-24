package teamh.boostcamp.myapplication.view.setting;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;
import teamh.boostcamp.myapplication.data.repository.backup.DownloadTask;
import teamh.boostcamp.myapplication.data.repository.backup.UploadTask;

class SettingPresenter {

    private static final String TAG = "SettingPresenter";
    private static final String DOWNLOAD_TAG = "DOWNLOAD";
    private static final String UPLOAD_TAG = "UPLOAD";

    private SettingView settingView;
    private DiaryRepository diaryRepository;
    private CompositeDisposable compositeDisposable;
    private RecallRepository recallRepository;

    SettingPresenter(@NonNull SettingView view,
                     @NonNull DiaryRepository diaryRepository,
                     @NonNull RecallRepository recallRepository) {
        this.settingView = view;
        this.diaryRepository = diaryRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.recallRepository = recallRepository;
    }

    void onDestroy() {
        compositeDisposable.clear();
        diaryRepository = null;
    }


    void backupLocalDataToFirebaseRepository() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            final OneTimeWorkRequest uploadTask = new OneTimeWorkRequest.Builder(UploadTask.class)
                    .addTag(UPLOAD_TAG)
                    .build();

            settingView.showBackUpStartMsg();
            settingView.startWorker(uploadTask, UPLOAD_TAG);

        } else {
            settingView.showNotLoginMsg();
        }
    }

    void downloadAllBackupFilesFromFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            final OneTimeWorkRequest downloadTask = new OneTimeWorkRequest.Builder(DownloadTask.class)
                    .addTag(DOWNLOAD_TAG)
                    .build();

            settingView.showLoadStartMsg();
            settingView.startWorker(downloadTask, DOWNLOAD_TAG);

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

    void deleteAllRecall() {
        compositeDisposable.add(recallRepository
                .deleteAll().observeOn(AndroidSchedulers.mainThread()).subscribe()
        );
    }
}
