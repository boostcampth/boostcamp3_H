package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.converter.DateTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.EmotionTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.StringListTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.dao.RecallDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;
import teamh.boostcamp.myapplication.data.model.Emotion;

@Database(entities = {DiaryEntity.class, RecallEntity.class}, version = 8, exportSchema = false)
@TypeConverters({DateTypeConverter.class, EmotionTypeConverter.class, StringListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "appDB.db";
    private static volatile AppDatabase INSTANCE;

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    // FIXME 더미 데이터 추가
                                    final String filePath = "/storage/emulated/0/2019-02-08.acc";
                                    final File file = new File("/storage/emulated/0/2019-02-08.acc");

                                    if (!file.exists()) {
                                        try {
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Random random = new Random();

                                    List<DiaryEntity> samples = new ArrayList<>();

                                    final long TODAY = new Date().getTime();
                                    final long DAY = 86400000L;

                                    for (int i = 1; i <= 20; ++i) {
                                        samples.add(new DiaryEntity(
                                                i,
                                                new Date(TODAY - DAY * i),
                                                filePath,
                                                Arrays.asList(String.format("#%2d번", i)),
                                                Emotion.fromValue(Math.abs(random.nextInt() % 5)),
                                                Emotion.fromValue(Math.abs(random.nextInt() % 5))
                                        ));
                                    }

                                    DiaryEntity[] temp = new DiaryEntity[samples.size()];



                                    RecallEntity[] recallList = {new RecallEntity(0, new Date(), Emotion.fromValue(0))
                                            , new RecallEntity(0, new Date(), Emotion.fromValue(1))
                                    , new RecallEntity(0, new Date(), Emotion.fromValue(2))
                                    , new RecallEntity(0, new Date(), Emotion.fromValue(3))};

                                    Completable.fromAction(() -> INSTANCE.recallDao().insertRecall(recallList))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DiaryDao diaryDao();

    public abstract RecallDao recallDao();
}
