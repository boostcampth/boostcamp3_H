package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;
import teamh.boostcamp.myapplication.data.local.room.converter.DateTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.EmotionTypeConverter;
import teamh.boostcamp.myapplication.data.local.room.converter.TagListTypeConverter;

@Database(entities = {DiaryEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class, EmotionTypeConverter.class, TagListTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    @NonNull
    private static final String DB_NAME = "applicationDB.db";

    @NonNull
    private static AppDatabase INSTANCE;

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
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.e("Test", "확인");
                                }
                            }).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DiaryDao diaryDao();
}
