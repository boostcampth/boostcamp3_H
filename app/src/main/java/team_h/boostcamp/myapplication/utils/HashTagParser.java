package team_h.boostcamp.myapplication.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashTagParser {

    /**
     * hashtag string format "#hashtag_#hashtag2"
     */

    public static List<String> StringToList(String hashTagString){
        return new ArrayList(Arrays.asList(hashTagString.split(" ")));
    }
}
