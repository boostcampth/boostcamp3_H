package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.local.room.converter.DateTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.EmotionTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.StringListTypeConverter;

@Database(entities = {DiaryEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class, EmotionTypeConverter.class, StringListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    @NonNull
    private static final String DB_NAME = "applicationDB.db";

    @NonNull
    private static volatile AppDatabase INSTANCE;

    public AppDatabase() {
    }

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Completable.fromAction(() -> INSTANCE.diaryDao().
                                            insertDiaryList(DiaryEntity.generateSampleDiaryData()))
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(() -> {
                                               Log.d("Test", "데이터 저장 완료");
                                            }, throwable -> {
                                                Log.d("Test", "데이터 저장 실패");
                                            });
                                }
                            }).build();
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    public abstract DiaryDao diaryDao();
}
