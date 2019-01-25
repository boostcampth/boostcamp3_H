package team_h.boostcamp.myapplication.utils;

import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;


public class NetworkStatueUtilTest {

    @Test
    public void isNetworkConnected() {
        int result = NetworkStatueUtil.isNetworkConnected(InstrumentationRegistry.getContext());
        Assert.assertEquals(1, result);
    }
}