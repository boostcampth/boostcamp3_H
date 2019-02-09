package teamh.boostcamp.myapplication.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

class HashTagParser {

    /**
     * hashtag string format "#hashtag_#hashtag2"
     */
    @NonNull
    static List<String> StringToList(@NonNull String hashTagString){
        return new ArrayList(Arrays.asList(hashTagString.split(" ")));
    }
}
