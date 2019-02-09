package teamh.boostcamp.myapplication.utils;

import androidx.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;


public class NetworkStateUtilTest {

    @Test
    public void isNetworkConnected() {
        int result = NetworkStateUtil.isNetworkConnected(InstrumentationRegistry.getContext());
        Assert.assertEquals(1, result);
    }
}