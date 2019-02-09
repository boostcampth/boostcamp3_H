package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import teamh.boostcamp.myapplication.data.model.Diary;
import teamh.boostcamp.myapplication.data.model.Memory;
import teamh.boostcamp.myapplication.data.model.Recommendation;
import teamh.boostcamp.myapplication.data.local.room.dao.AppDao;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;

@Database(entities = {Diary.class, Recommendation.class, Memory.class}, version = 4, exportSchema = false)
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
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AppDao appDao();
    public abstract DiaryDao diaryDao();
}
