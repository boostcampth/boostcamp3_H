package teamh.boostcamp.myapplication.view.recall;

import android.util.Log;

import java.util.Date;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;
import teamh.boostcamp.myapplication.data.model.Emotion;
import teamh.boostcamp.myapplication.data.repository.RecallRepository;

public class RecallPresenter {

    @NonNull
    private RecallView view;
    @NonNull
    private RecallRepository recallRepository;
    @NonNull
    private CompositeDisposable compositeDisposable;

    RecallPresenter(@NonNull RecallView view, @NonNull RecallRepository recallRepository) {
        this.view = view;
        this.recallRepository = recallRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    void loadRecallList() {
        compositeDisposable.add(
                recallRepository
                        .loadRecallList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recallList -> {
                            view.addRecallList(recallList);
                        })
        );
    }

    void generateRecall() {
        compositeDisposable.add(
                recallRepository.insertRecall(new RecallEntity(
                        0,
                        new Date(),
                        Emotion.fromValue(generateRandomNumber(5))))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recall -> view.addRecall(recall))
        );
    }

    void deleteRecall(int position, int id) {
        compositeDisposable.add(
                recallRepository.deleteRecall(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            view.showDeleteSuccessResult();
                            view.deleteRecall(position);
                        })
        );
    }

    private int generateRandomNumber(int limit) {
        return (int) (Math.random() * limit);
    }
}