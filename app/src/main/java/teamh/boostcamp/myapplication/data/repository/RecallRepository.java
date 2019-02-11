package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.Recall;

public interface RecallRepository {

    @NonNull
    Single<List<Recall>> loadRecallList();

    @NonNull
    void insertRecall();

}