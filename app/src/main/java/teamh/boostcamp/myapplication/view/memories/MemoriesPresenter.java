package teamh.boostcamp.myapplication.view.memories;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.R;
import teamh.boostcamp.myapplication.data.local.room.LegacyAppDatabase;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.data.model.Recommendation;
import teamh.boostcamp.myapplication.utils.ResourceSendUtil;
import teamh.boostcamp.myapplication.view.adapter.AdapterContract;

public class MemoriesPresenter implements MemoriesContractor.Presenter {
    private static final String TAG = "MemoriesPresenter";
    private MemoriesContractor.View view;
    private AdapterContract.Model<Memory> memoriesCardAdapterModel;
    private AdapterContract.View memoriesCardAdapterView;
    private LegacyAppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;
    private ResourceSendUtil resourceSendUtil;

    MemoriesPresenter(MemoriesContractor.View view, ResourceSendUtil resourceSendUtil) {
        this.view = view;
        this.resourceSendUtil = resourceSendUtil;
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
        Date generatedDate = new Date();
        String generateDate = DateToString(new Date());
        String title = generateTitle(selectedMemory, generatedDate);
        Memory memory = new Memory(0, title, generateDate, selectedMemory);

        compositeDisposable.add(appDatabase.appDao().insertMemory(memory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            appDatabase.appDao().loadRecentMemory()
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
                                                                Log.d(TAG, "generateMemory: insertRecommendation" + diaries.get(i).getId());
                                                                LegacyDiary currentDiary = diaries.get(i);
                                                                appDatabase.appDao().insertRecommendation(new Recommendation(0, id, currentDiary.getId()))
                                                                        .subscribeOn(Schedulers.io())
                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                        .subscribe(() -> {
                                                                        });
                                                            }
                                                        });
                                            }
                                    );
                        }
                        , throwable -> {
                        })
        );
    }

    private boolean checkCurrentMemory() {
        //Todo 1주일에 한번 추천하기 기능
        return false;
    }

    @Override
    public void onDeleteButtonClicked(int position) {
        view.makeToast("삭제버튼이눌렸습니다.");
        Memory memory = memoriesCardAdapterModel.getItem(position);
        compositeDisposable.add(
                appDatabase.appDao().deleteMemory(memory)
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
    public void setDatabase(LegacyAppDatabase appDatabase) {
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date currentDate = null;
        try {
            currentDate = simpleDateFormat.parse(endDate);
        } catch (ParseException e) {
            e.getStackTrace();
        }
        Date startDate = new Date(currentDate.getTime() - (14 * 24 * 60 * 60 * 1000));

        return simpleDateFormat.format(startDate);
    }

    private String generateTitle(int selectedMemory, Date generatedDate) {
        StringBuilder newTitle = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM", Locale.KOREA);
        newTitle.append(simpleDateFormat.format(generatedDate)).append("의 ");

        switch (selectedMemory) {
            case 0:
                newTitle.append(resourceSendUtil.getString(R.string.title_emition0));
                break;
            case 1:
                newTitle.append(resourceSendUtil.getString(R.string.title_emition1));
                break;
            case 2:
                newTitle.append(resourceSendUtil.getString(R.string.title_emition2));
                break;
            case 3:
                newTitle.append(resourceSendUtil.getString(R.string.title_emition3));
                break;
            case 4:
                newTitle.append(resourceSendUtil.getString(R.string.title_emition4));
                break;
        }

        return newTitle.toString();
    }

    private String DateToString(Date today) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return simpleDateFormat.format(today);
    }

}
