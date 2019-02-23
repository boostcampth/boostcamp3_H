package teamh.boostcamp.myapplication.data.repository.backup;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.utils.EventBus;

public class UploadTask extends Worker {

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
        result = Result.success();
    }

    @NonNull
    @Override
    public Result doWork() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Disposable disposable = backUpRepository.loadAllDiaryId()
                .flatMapMaybe(diaryRepository::loadNotBackupDiaryList)
                .flatMapObservable(Observable::fromIterable)
                .flatMap(backUpRepository::uploadSingleRecordFile)
                .flatMapCompletable(backUpRepository::insertDiary)
                .doOnComplete(() -> EventBus.sendEvent(Event.BACK_UP_COMPLETE))
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    result = Result.failure();
                }).subscribe(countDownLatch::countDown,
                        throwable -> {
                            throwable.printStackTrace();
                            countDownLatch.countDown();
                        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        return result;
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }
}
