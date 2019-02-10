package teamh.boostcamp.myapplication.view.diarylist;

import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.repository.DiaryRepository;

public class DiaryListPresenter {
    @NonNull
    final private DiaryRepository diaryRepository;
    @NonNull
    final private CompositeDisposable compositeDisposable;
    @NonNull
    final private DiaryListView diaryListView;


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
                .subscribe(diaryListView::addDiaryList
                        , throwable -> diaryListView.showLoadDiaryListFailMsg()
                ));
    }

    void onViewDestroyed() {
        compositeDisposable.clear();
    }
}
