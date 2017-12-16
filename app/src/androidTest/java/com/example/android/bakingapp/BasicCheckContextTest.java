package com.example.android.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;


/**
 * Created by ErwinF on 12/4/2017.
 */

@RunWith(AndroidJUnit4.class)
public class BasicCheckContextTest {

    @Rule
    public ActivityTestRule<MainActivity> mIntentsTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {

        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.bakingapp", appContext.getPackageName());
    }

    @Test
    public void checkIfRandomThingsDoStuff() {

        onData(anything()).inAdapterView(withId(R.id.card_grid_view)).atPosition(1).check(matches(withText(JsonInfoUtils.RECIPE_NAMES[1])));

        onData(anything()).inAdapterView(withId(R.id.card_grid_view)).atPosition(1).perform(click());

        onView(withId(R.id.voila_text)).check(matches(withText(R.string.voila)));

    }

}
