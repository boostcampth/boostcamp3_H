package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
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
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DiaryDao diaryDao();

    public abstract RecallDao recallDao();
}
