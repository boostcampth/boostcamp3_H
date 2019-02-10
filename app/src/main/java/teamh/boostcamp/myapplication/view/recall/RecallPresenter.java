package teamh.boostcamp.myapplication.view.recall;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;

public class RecallPresenter {

    @NonNull
    private RecallView recallView;
    @NonNull
    private RecallRepository recallRepository;

    public RecallPresenter(@NonNull RecallView recallView, @NonNull RecallRepository recallRepository) {
        this.recallView = recallView;
        this.recallRepository = recallRepository;
    }

    public void loadRecallList() {
        recallRepository
                .loadRecallList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recallList -> {
                    recallView.addRecallList(recallList);
                });
    }


}
