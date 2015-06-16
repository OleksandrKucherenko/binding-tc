package com.artfulbits.sample.espresso;

import android.test.ActivityInstrumentationTestCase2;

import com.artfulbits.sample.MainActivity;
import com.artfulbits.sample.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/** Default Android tests. */
public class LoginFillEspressoTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public LoginFillEspressoTest() {
        super("com.artfulbits.sample", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        getActivity();
    }

    public void test_00_CreateActivity() {
        onView(withId(R.id.tv_login))
                .check(matches(withText(R.string.labelLogin)));

        onView(withId(R.id.et_login))
                .perform(typeText("developer"));
    }
}
