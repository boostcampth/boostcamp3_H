package teamh.boostcamp.myapplication.view;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.android.schedulers.AndroidSchedulers;
import teamh.boostcamp.myapplication.data.local.room.AppDatabase;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;
import teamh.boostcamp.myapplication.data.repository.RecallRepositoryImpl;

public class DataInsertWorker extends Worker {
    public DataInsertWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        RecallRepository recallRepository = RecallRepositoryImpl.getInstance(AppDatabase.getInstance(getApplicationContext()));
        recallRepository.insertRecall(new RecallEntity(
                0,
                new Date(),
                Emotion.fromValue(generateRandomNumber(5))))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        return Result.success();
    }

    private int generateRandomNumber(int limit) {
        return (int) (Math.random() * limit);
    }
}
