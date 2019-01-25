package team_h.boostcamp.myapplication.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HashTagPaser {

    /**
     * hashtag string format "#hashtag_#hashtag2"
     */

    public static List<String> StringToList(String hashTagString){
        List<String> hashTags = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(hashTagString);

        while(stringTokenizer.hasMoreTokens()){
            hashTags.add(stringTokenizer.nextToken());
        }

        return hashTags;
    }
}
