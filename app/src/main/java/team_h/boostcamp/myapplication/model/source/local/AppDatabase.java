package team_h.boostcamp.myapplication.model.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Memory;
import team_h.boostcamp.myapplication.model.Recommendation;

@Database(version = 1, entities = {Diary.class, Memory.class, Recommendation.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "appDatabase.db";
    private static volatile AppDatabase INSTANCE;

    private AppDatabase() {}

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            AppDatabase.class,
                            DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AppDao diaryDao();
}
