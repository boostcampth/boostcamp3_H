package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.CountedTag;

public interface CountedTagRepository {

    @NonNull
    Single<List<CountedTag>> loadCountedTagList();
}
