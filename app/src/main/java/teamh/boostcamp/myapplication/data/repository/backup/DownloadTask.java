package teamh.boostcamp.myapplication.data.repository.backup;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;
import io.reactivex.Observable;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;

public class DownloadTask extends RxWorker {

    @NonNull
    private DiaryRepository diaryRepository;
    @NonNull
    private BackUpRepository backUpRepository;

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
    }

    @Override
    public Single<Result> createWork() {

        result = Result.success();

        return Single.zip(backUpRepository.loadAllDiaryList(), diaryRepository.loadAllDiaryEntityList(),
                (remoteEntityList, localEntityList) -> {
                    final int size = localEntityList.size();
                    for (int i = 0; i < size; ++i) {
                        remoteEntityList.remove(localEntityList.get(i));
                    }
                    return remoteEntityList;
                }).flatMapObservable(Observable::fromIterable)
                .toList()
                .flatMap(backUpRepository::downloadRecordFile)
                .flatMapCompletable(diaryEntityList ->
                        diaryRepository.insertDiary(diaryEntityList.toArray(new DiaryEntity[diaryEntityList.size()])))
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                    result = Result.failure();
                }).toSingle(() -> result);
    }

}
