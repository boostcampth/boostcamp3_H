package team_h.boostcamp.myapplication.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


public class NetworkStatueUtilTest {

    @Test
    public void isNetworkConnected() {

        Arrays.asList("tests.tests.".split(" "));

        int result = NetworkStatueUtil.isNetworkConnected(InstrumentationRegistry.getContext());
        Assert.assertEquals(1, result);
    }
}