package teamh.boostcamp.myapplication.view.graph;

import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import teamh.boostcamp.myapplication.data.repository.StatisticsRepository;

public class StatisticsPresenter {

    @NonNull
    private StatisticsRepository statisticsRepository;

    @NonNull
    private StatisticsView statisticsView;

    // 생성자를 통한 주입.
    public StatisticsPresenter(@NonNull StatisticsRepository statisticsRepository,
                               @NonNull StatisticsView statisticsView) {
        this.statisticsRepository = statisticsRepository;
        this.statisticsView = statisticsView;
    }

    void loadStatisticsData() {
        /**
         * Repository에게 데이터 요청해서 받아오는 로직.
         * 그리고 view에 갱신
         * */

        statisticsRepository.loadRecentEmotionHistoryList(new Date())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(emotionHistoryList -> {
                    // TODO 부에 처리
                }, throwable -> {
                    // TODO 에러 처리
                });
    }

    void loadTagList() {
        statisticsRepository.loadRecentCountedTagList(new Date())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countedTags -> {
                    // TODO : 뷰에 처리
                }, throwable -> {
                    // TODO : 에러 처리
                });
    }
}
