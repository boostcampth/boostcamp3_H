package teamh.boostcamp.myapplication.utils;

import org.junit.Test;

import java.util.List;

public class HashTagPaserTest {

    @Test
    public void stringToList() {
        List<String> tags = HashTagParser.StringToList("#hello #hi #안녕");

        for (String s: tags) {
            System.out.println(s);
        }
    }
}