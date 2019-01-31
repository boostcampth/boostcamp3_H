package team_h.boostcamp.myapplication.model.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import team_h.boostcamp.myapplication.model.Diary;
import team_h.boostcamp.myapplication.model.Recommendation;

@Database(entities = {Diary.class, Recommendation.class, /*Memory.class*/}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
}
