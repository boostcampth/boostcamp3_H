package teamh.boostcamp.myapplication.data.local.room.typeConverter;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class DateTypeConverter {
    
    @TypeConverter
    @NonNull
    public static Long dateToLong(@NonNull Date date) {
        return date.getTime();
    }

    @TypeConverter
    @NonNull
    public static Date longToDate(@NonNull Long time) {
        return new Date(time);
    }
}
