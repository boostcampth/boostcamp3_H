package team_h.boostcamp.myapplication.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class HashTagPaser {

    /**
     * hashtag string format "#hashtag_#hashtag2"
     */

    public static List<String> StringToList(String hashTagString){
        return new ArrayList(Arrays.asList(hashTagString.split(" ")));
    }
}
