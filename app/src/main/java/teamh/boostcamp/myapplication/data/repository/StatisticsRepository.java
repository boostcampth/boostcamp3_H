package teamh.boostcamp.myapplication.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import teamh.boostcamp.myapplication.data.model.CountedTag;

/**
 * @author 이승우
 * @version 1.0.1
 * 최신 2주 간의 감정 정보를 불러온다.
 * 최신 2주 간의 해시태그 정보를 불러온다.
 */
public interface StatisticsRepository {

    @NonNull
    Single<List<CountedTag>> loadRecentCountedTagList();

}
