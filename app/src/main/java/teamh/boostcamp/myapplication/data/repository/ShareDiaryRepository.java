package teamh.boostcamp.myapplication.data.repository;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.ShareDiary;

public interface ShareDiaryRepository {

    @NonNull
    Single<ShareDiary> loadShareDiary(String id);

}
