package team_h.boostcamp.myapplication.view.memories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.Recommendation;
import team_h.boostcamp.myapplication.model.source.local.AppDatabase;
import team_h.boostcamp.myapplication.view.adapter.AdapterContract;

public class MemoriesPresenter implements MemoriesContractor.Presenter {

    private MemoriesContractor.View view;
    private AdapterContract.Model<Memory> memoriesCardAdapterModel;
    private AdapterContract.View memoriesCardAdapterView;
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    MemoriesPresenter(MemoriesContractor.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setMemoriesCardAdapterView(MemoriesCardAdapter adapter) {
        this.memoriesCardAdapterView = adapter;
    }

    @Override
    public void setMemoriesCardAdapterModel(MemoriesCardAdapter adapter) {
        this.memoriesCardAdapterModel = adapter;
    }

    @Override
    public void onViewAttached() {

    }

    @Override
    public void onViewDetached() {
        view = null;
        compositeDisposable.clear();
    }

    @Override
    public void onRecommendMemory() {
        if (!checkCurrentMemory()) {
            generateMemory();
        } else {
            view.makeToast("아직 추천일이 아닙니다.");
        }

    }

    private void generateMemory() {
        int selectedMemory = generateRandomNumber();
        String generateDate = DateToString(new Date());
        Memory memory = new Memory(0, "Happy of January", generateDate, selectedMemory);

        view.makeToast("선택된 감정" + generateRandomNumber());

        // 새로운 메모리 추가하기
        compositeDisposable.add(Observable.just(memory)
                .subscribeOn(Schedulers.io())
                .subscribe(m -> {
                            appDatabase.appDao().insertMemory(m);
                            memoriesCardAdapterModel.addItem(m);
                        }
                ));

        // 새로운 메모리 다이어리 리스트 저장하기
        compositeDisposable.add(appDatabase.appDao().loadRecentMemory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        memories1 -> {
                            int id = memories1.getId();
                            String endDate = memories1.getDate();
                            String startDate = generateStartDate(endDate);

                            appDatabase.appDao().getSelectedRecord(startDate, endDate, selectedMemory)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(diaries -> {
                                        for (int i = 0; i < diaries.size(); i++) {
                                            Diary currentDiary = diaries.get(i);
                                            appDatabase.appDao().insertRecommendation(new Recommendation(0, id, currentDiary.getId()));
                                        }
                                    });
                        }
                ));
    }

    private boolean checkCurrentMemory() {
        //Todo 1주일에 한번 추천하기 기능
        return false;
    }

    @Override
    public void onDeleteButtonClicked(int position) {
        view.makeToast("삭제버튼이눌렸습니다.");
        Memory memory = memoriesCardAdapterModel.getItem(position);
        compositeDisposable.add(Completable.fromAction(() -> appDatabase.appDao().deleteMemory(memory))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> memoriesCardAdapterModel.removeItem(position)));
    }

    @Override
    public void onPlayButtonClicked(int position) {
        view.makeToast("플레이하겠습니다.");
        view.navigateToPlayActivity(memoriesCardAdapterModel.getItem(position));
    }

    @Override
    public void setDatabase(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public void loadData() {
        compositeDisposable.add(appDatabase.appDao().loadMemories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(memories1 -> memoriesCardAdapterModel.updateItems(memories1)
                ));
    }

    private int generateRandomNumber() {
        return (int) (Math.random() * 5);
    }

    private String generateStartDate(String endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyMMdd", Locale.KOREA);
        Date currentDate = null;
        try {
            currentDate = simpleDateFormat.parse(endDate);
        } catch (ParseException e) {
            e.getStackTrace();
        }
        Date startDate = new Date(currentDate.getTime() - (14 * 24 * 60 * 60 * 1000));

        return simpleDateFormat.format(startDate);
    }

    private String DateToString(Date today) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return simpleDateFormat.format(today);
    }

}
