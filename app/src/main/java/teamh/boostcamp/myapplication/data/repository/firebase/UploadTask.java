package teamh.boostcamp.myapplication.data.repository.firebase;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.utils.EventBus;

public class UploadTask extends RxWorker {

    @NonNull
    private DiaryRepository diaryRepository;
    @NonNull
    private BackUpRepository backUpRepository;
    @NonNull
    private Result result;

    public UploadTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        diaryRepository = DiaryRepositoryImpl.getInstance(
                AppDatabase.getInstance(getApplicationContext()).diaryDao(),
                DeepAffectApiClient.getInstance());
        backUpRepository = BackUpRepositoryImpl.getInstance(FirebaseDatabase.getInstance(),
                FirebaseStorage.getInstance(),
                FirebaseAuth.getInstance(),
                AppDatabase.getInstance(context).diaryDao());
    }

    @Override
    public Single<Result> createWork() {
        result = Result.success();

        return backUpRepository.loadAllDiaryId()
                .flatMapMaybe(diaryRepository::loadNotBackupDiaryList)
                .flatMapSingle(backUpRepository::uploadRecordFile)
                .flatMapCompletable(backUpRepository::insertDiaries)
                .doOnComplete(() -> EventBus.sendEvent(Event.BACK_UP_COMPLETE))
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    result = Result.failure();
                }).toSingle(() -> result);
    }
}
