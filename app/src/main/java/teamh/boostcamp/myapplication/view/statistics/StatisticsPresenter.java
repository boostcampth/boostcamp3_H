package teamh.boostcamp.myapplication.view.statistics;

import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.repository.StatisticsRepository;

public class StatisticsPresenter {

    @NonNull
    private StatisticsRepository statisticsRepository;

    @NonNull
    private StatisticsView StatisticsView;

    @NonNull
    private CompositeDisposable compositeDisposable;

    // 생성자를 통한 주입.
    StatisticsPresenter(@NonNull StatisticsView StatisticsView,
                        @NonNull StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
        this.StatisticsView = StatisticsView;
        this.compositeDisposable = new CompositeDisposable();
    }

    void loadStatisticsData() {
        /* FIXME
         * Repository에게 데이터 요청해서 받아오는 로직.
         * 그리고 view에 갱신
         * 화면이 꺼지는 상황을 대비하기 위해서 CompositeDisposable이 필요하다.
         * Detach, Destroy 상황에서 구독을 해제해줘야 한다.
         * 비동기 처리를 통해 받아온 데이터를 어떤 스레드에서 처리할지?! - 메인 스레드
         * */
        compositeDisposable.add(statisticsRepository.loadRecentEmotionHistoryList(new Date())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(emotionHistoryList -> {
                    StatisticsView.updateStatisticsData(emotionHistoryList);
                    StatisticsView.checkLoadStatisticsDataSuccessMessage();
                }, throwable -> {
                    // TODO 에러 처리
                    StatisticsView.checkLoadStatisticsDataFailMessage();
                }));
    }

    void loadTagList() {
        compositeDisposable.add(statisticsRepository.loadRecentCountedTagList(new Date())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countedTags -> {
                    // TODO : 뷰에 전달
                    StatisticsView.updateTagListData(countedTags);
                    StatisticsView.checkLoadTagListSuccessMessage();
                }, throwable -> {
                    // TODO : 에러 처리
                    StatisticsView.checkLoadTagListFailMessage();
                }));
    }

    void viewDestroyed() {
        compositeDisposable.clear(); // 구독 해제
    }
}
