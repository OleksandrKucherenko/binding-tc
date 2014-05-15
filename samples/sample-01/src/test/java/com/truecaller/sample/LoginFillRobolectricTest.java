package com.truecaller.sample;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

/** First robolectric test. */
@RunWith(RobolectricTestRunner.class)
public class LoginFillRobolectricTest {

    @Test
    public void test_00_CreateActivity() {
        final Activity activity = Robolectric.buildActivity(MainActivity.class).get();

        assertTrue(null != activity);
    }
}
