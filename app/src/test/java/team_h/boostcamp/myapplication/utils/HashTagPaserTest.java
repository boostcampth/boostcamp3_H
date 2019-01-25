package team_h.boostcamp.myapplication.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HashTagPaserTest {

    @Test
    public void stringToList() {
        List<String> tags = HashTagPaser.StringToList("#hello #hi #안녕");

        for (String s: tags) {
            System.out.println(s);
        }
    }
}