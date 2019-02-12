package teamh.boostcamp.myapplication.view.recall;

import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;

public class RecallPresenter {

    @NonNull
    private RecallView recallView;
    @NonNull
    private RecallRepository recallRepository;
    @NonNull
    private CompositeDisposable compositeDisposable;

    RecallPresenter(@NonNull RecallView recallView, @NonNull RecallRepository recallRepository) {
        this.recallView = recallView;
        this.recallRepository = recallRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    void loadRecallList() {
        compositeDisposable.add(
                recallRepository
                        .loadRecallList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recallList -> {
                            recallView.addRecallList(recallList);
                        })
        );
    }

    void generateRecall(){
        compositeDisposable.add(
                recallRepository.insertRecall(new RecallEntity(
                        0,
                        new Date(),
                        Emotion.fromValue(generateRandomNumber(5))))
                .subscribe(this::loadRecallList)
        );
    }

    private int generateRandomNumber(int limit){
        return (int) (Math.random() * limit) ;
    }
}