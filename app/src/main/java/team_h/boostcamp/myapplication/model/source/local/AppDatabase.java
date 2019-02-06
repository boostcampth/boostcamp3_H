package team_h.boostcamp.myapplication.model.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.Recommendation;

@Database(entities = {Diary.class, Recommendation.class, Memory.class}, version = 3, exportSchema = false)
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
