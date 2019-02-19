package teamh.boostcamp.myapplication.data.repository.firebase;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.utils.EventBus;

public class DownloadTask extends Worker {

    private DiaryRepository diaryRepository;
    private FirebaseRepository firebaseRepository;

    public DownloadTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        diaryRepository = DiaryRepositoryImpl.getInstance(
                AppDatabase.getInstance(getApplicationContext()).diaryDao(),
                DeepAffectApiClient.getInstance());
        firebaseRepository = FirebaseRepositoryImpl.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {

        final Result[] result = new Result[]{Result.success()};
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Disposable disposable = Single.zip(firebaseRepository.loadAllDiaryList(), diaryRepository.loadAllDiaryEntityList(),
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
                .subscribe(() -> {
                    countDownLatch.countDown();
                    EventBus.sendEvent(Event.DOWNLOAD_COMPLETE);
                }, throwable -> {
                    throwable.printStackTrace();
                    result[0] = Result.failure();
                    countDownLatch.countDown();
                });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        diaryRepository = null;
        firebaseRepository = null;
        disposable.dispose();

        return result[0];
    }
}
