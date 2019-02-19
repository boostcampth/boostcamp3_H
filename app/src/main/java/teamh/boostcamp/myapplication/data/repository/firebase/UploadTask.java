package teamh.boostcamp.myapplication.data.repository.firebase;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.utils.EventBus;

public class UploadTask extends Worker {

    public UploadTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        final Result[] result = new Result[]{Result.success()};
        CountDownLatch countDownLatch = new CountDownLatch(1);

        DiaryRepository diaryRepository = DiaryRepositoryImpl.getInstance(
                AppDatabase.getInstance(getApplicationContext()).diaryDao(),
                null);

        FirebaseRepository firebaseRepository = FirebaseRepositoryImpl.getInstance();

        Disposable disposable = firebaseRepository.loadAllDiaryId()
                .flatMapMaybe(diaryRepository::loadNotBackupDiaryList)
                .flatMapSingle(firebaseRepository::uploadRecordFile)
                .flatMapCompletable(firebaseRepository::insertDiaries)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            countDownLatch.countDown();
                            EventBus.sendEvent(Event.BACK_UP_COMPLETE);
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            result[0] = Result.failure();
                            countDownLatch.countDown();
                        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable.dispose();

        return result[0];
    }
}
