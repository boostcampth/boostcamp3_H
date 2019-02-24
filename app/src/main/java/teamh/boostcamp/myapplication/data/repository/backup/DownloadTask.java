package teamh.boostcamp.myapplication.data.repository.backup;

import android.content.Context;
import android.util.Log;

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
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.model.Event;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.utils.EventBus;

public class DownloadTask extends Worker {

    @NonNull
    private DiaryRepository diaryRepository;
    @NonNull
    private BackUpRepository backUpRepository;
    @NonNull
    private Result result;

    public DownloadTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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

        Disposable disposable = Single.zip(backUpRepository.loadAllDiaryList(), diaryRepository.loadAllDiaryEntityList(),
                (remoteEntityList, localEntityList) -> {
                    final int size = localEntityList.size();
                    for (int i = 0; i < size; ++i) {
                        remoteEntityList.remove(localEntityList.get(i));
                    }
                    return remoteEntityList;
                }).flatMapObservable(Observable::fromIterable)
                .flatMap(backUpRepository::downloadSingleRecordFile)
                .flatMapCompletable(diaryRepository::insertDiary)
                .doOnComplete(() -> EventBus.sendEvent(Event.DOWNLOAD_COMPLETE))
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
