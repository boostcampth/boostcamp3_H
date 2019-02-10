package teamh.boostcamp.myapplication.view.graph;

import androidx.annotation.NonNull;
import teamh.boostcamp.myapplication.data.repository.StatisticsRepository;

public class StatisticsPresenter {

    @NonNull
    private StatisticsPresenter statisticsPresenter;

    @NonNull
    private StatisticsRepository statisticsRepository;

    @NonNull
    private StatisticsView statisticsView;

    // 생성자를 통한 주입.
    public StatisticsPresenter(@NonNull StatisticsRepository statisticsRepository,
                               @NonNull StatisticsView statisticsView){
        this.statisticsRepository = statisticsRepository;
        this.statisticsView = statisticsView;
    }

    void loadStatisticsData(){
        /**
         * Repository에게 데이터 요청해서 받아오는 로직.
         * 그리고 view에 갱신
         * */
    }
}
