package teamh.boostcamp.myapplication.view.setting;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.SharedPreferenceManager;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.remote.apis.deepaffects.DeepAffectApiClient;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;
import teamh.boostcamp.myapplication.data.repository.DiaryRepositoryImpl;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;

public class InitializationWorker extends Worker {
    public InitializationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        RecallRepository recallRepository = RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getApplicationContext()));
        DiaryRepository diaryRepository = DiaryRepositoryImpl.getInstance(AppDatabase.getInstance(getApplicationContext()).diaryDao(),
                DeepAffectApiClient.getInstance());
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(
                diaryRepository.loadAll()
                        .map(diary -> {
                            File file = new File(diary.getRecordFilePath());
                            file.delete();

                            return diary;
                        })
                        .flatMapCompletable(diary -> diaryRepository.deleteDiary(diary.getId()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );

        compositeDisposable.add(
                recallRepository
                        .deleteAll()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(getApplicationContext(), R.string.setting_initialization_success, Toast.LENGTH_LONG).show();
                        })
        );

        SharedPreferenceManager.getInstance(getApplicationContext()).removeLastDiarySaveTime();

        return Result.success();
    }
}
