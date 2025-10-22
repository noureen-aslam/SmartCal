package com.example.smartcal.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.assertion.ViewAssertions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.smartcal.R;

@RunWith(AndroidJUnit4.class)
public class AddFoodFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addFoodAndSeeUpdatedList() {
        Espresso.onView(ViewMatchers.withId(R.id.fabAdd)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.etFoodName)).perform(ViewActions.typeText("Apple"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.etAmount)).perform(ViewActions.typeText("150"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.btnSave)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerFoods))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
