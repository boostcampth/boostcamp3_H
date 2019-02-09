package teamh.boostcamp.myapplication.data.local.room.converter;

import java.util.Arrays;
import java.util.List;

import androidx.room.TypeConverter;

public class TagListTypeConverter {

    @TypeConverter
    public static List<String> stringToTagList(String tags) {
        List<String> tagList = Arrays.asList(tags.split("#"));
        return tagList;
    }

    @TypeConverter
    public static String tagListToString(List<String> tagList) {
        String tags = "";
        for(String item : tagList) {
            tags += " " + item;
        }
        return tags;
    }
}
