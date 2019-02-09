package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import io.reactivex.schedulers.Schedulers;
import teamh.boostcamp.myapplication.data.local.room.dao.AppDao;
import teamh.boostcamp.myapplication.data.local.room.dao.LegacyDiaryDao;
import teamh.boostcamp.myapplication.data.model.LegacyDiary;
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.data.model.Recommendation;

@Database(entities = {LegacyDiary.class, Recommendation.class, Memory.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "appDB.db";
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // 생성 callback
                                    getInstance(context).diaryDao().insertDiary(LegacyDiary.generateSampleDiaryData())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(
                                                    () -> Log.d("Test", "임시 파일 생성"),
                                                    throwable -> Log.d("Test", "")
                                            );
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AppDao appDao();

    public abstract LegacyDiaryDao diaryDao();
}
