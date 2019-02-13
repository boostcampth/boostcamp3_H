package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;
import teamh.boostcamp.myapplication.data.model.Recall;

public interface RecallRepository {

    @NonNull
    Single<List<Recall>> loadRecallList();

    @NonNull
    Maybe<Recall> insertRecall(RecallEntity recallEntity);

    @NonNull
    Completable deleteRecall(int index);

}