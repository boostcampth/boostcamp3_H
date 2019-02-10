package teamh.boostcamp.myapplication.view.diarylist;

import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;

public class DiaryListPresenter {
    @NonNull
    private DiaryRepository diaryRepository;
    @NonNull
    private CompositeDisposable compositeDisposable;
    @NonNull
    private DiaryListView diaryListView;


    public DiaryListPresenter(@NonNull DiaryListView diaryListView,
                              @NonNull DiaryRepository diaryRepository) {
        this.diaryListView = diaryListView;
        this.diaryRepository = diaryRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    void loadDiaryList(@NonNull final Date recordDate,
                       final int pageSize) {

        compositeDisposable.add(diaryRepository.loadDiaryList(recordDate, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diaries -> {
                    // TODO 받아온 데이터 넘겨주기
                    diaryListView.addDiaryList(diaries);
                    diaryListView.showLoadDiaryListSuccessMsg();
                }, throwable -> {
                    // TODO 에러 처리
                    diaryListView.showLoadDiaryListFailMsg();
                }));
    }

    void viewOnDetach() {
        compositeDisposable.clear();
    }
}
