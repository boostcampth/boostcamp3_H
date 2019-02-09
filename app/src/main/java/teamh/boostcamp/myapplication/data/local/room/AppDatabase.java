package teamh.boostcamp.myapplication.data.local.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import teamh.boostcamp.myapplication.data.local.room.dao.DiaryDao;
import teamh.boostcamp.myapplication.data.local.room.entity.DiaryEntity;

@Database(entities = {DiaryEntity.class}, version = 1, exportSchema = false)
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
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // 임시 데이터 추가 구현
                                }
                            }).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DiaryDao diaryDao();
}
