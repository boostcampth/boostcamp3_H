package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import teamh.boostcamp.myapplication.data.local.room.converter.DateTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.EmotionTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.StringListTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.dao.RecallDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.local.room.entity.RecallEntity;

@Database(entities = {DiaryEntity.class, RecallEntity.class}, version = 9, exportSchema = false)
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
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    // FIXME 더미 데이터 추가
/*                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                                    final String filePath = File.separator + "storage" + File.separator
                                    + "emulated" + File.separator + "0" + File.separator + "diary" + File.separator +
                                            simpleDateFormat.format(new Date()) + ".aac";

                                    final File file = new File(filePath);

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

                                    for (int i = 2; i <= 10; ++i) {
                                        samples.add(new DiaryEntity(
                                                simpleDateFormat.format(TODAY - DAY * (i + 2)),
                                                new Date(TODAY - DAY * (i+2)),
                                                filePath,
                                                Arrays.asList(String.format("#%2d번", i)),
                                                Emotion.fromValue(Math.abs(random.nextInt() % 5)),
                                                Emotion.fromValue(Math.abs(random.nextInt() % 5))
                                        ));
                                    }

                                    DiaryEntity[] temp = new DiaryEntity[samples.size()];
                                    Completable.fromAction(() -> INSTANCE.diaryDao().insert(samples.toArray(temp)))
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(() -> {
                                                Log.d("test", "Test");
                                            }, throwable -> {
                                                throwable.printStackTrace();
                                            });*/
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
