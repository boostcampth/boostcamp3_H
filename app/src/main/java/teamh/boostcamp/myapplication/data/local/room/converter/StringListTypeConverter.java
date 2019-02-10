package teamh.boostcamp.myapplication.data.local.room.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.room.TypeConverter;

public class StringListTypeConverter {

    @TypeConverter
    public static List<String> fromString(String string) {
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public static String fromStringList(List<String> stringList) {
        return new Gson().toJson(stringList);
    }
}
