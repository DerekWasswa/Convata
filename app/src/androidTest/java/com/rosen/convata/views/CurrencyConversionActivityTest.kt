package com.rosen.convata.views

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rosen.convata.R
import com.rosen.convata.utils.EspressoIdlingResource
import com.rosen.convata.utils.TestActions
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class CurrencyConversionActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CurrencyConversionActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun tests_initial_conversion_status() {
        onView(withId(R.id.amount)).check(matches(withHint("Amount")))
        onView(withId(R.id.loader)).check(matches(withText(R.string.conversions_unavailable)))
    }

    @Test
    fun tests_amount_accepts_whole_numbers() {
        onView(withId(R.id.amount)).perform(click())
        onView(withId(R.id.amount)).perform(typeText("100"))
        onView(withId(R.id.amount)).check(matches(withText("100")))
    }

    @Test
    fun tests_amount_accepts_decimal_numbers() {
        onView(withId(R.id.amount)).perform(click())
        onView(withId(R.id.amount)).perform(typeText("100.5"))
        onView(withId(R.id.amount)).check(matches(withText("100.5")))
    }

    @Test
    fun tests_amount_rejects_unsupported_characters() {
        onView(withId(R.id.amount)).perform(click())
        onView(withId(R.id.amount)).perform(typeText("abc"))
        onView(withId(R.id.amount)).check(matches(withText("")))
    }

    @Test
    fun tests_base_currency_selection() {
        onView(withId(R.id.baseCurrencyLabel)).perform(click())
        onView(withText("USD")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.baseCurrencyLabel)).check(matches(withText("USD")))
    }

    @Test
    fun tests_base_currency_search() {
        onView(withId(R.id.baseCurrencyLabel)).perform(click())
        onView(withId(R.id.baseCurrencyLabel)).perform(typeTextIntoFocusedView("AF"), TestActions.dropAutoCompletes())
        onView(withText("AFN")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.baseCurrencyLabel)).check(matches(withText("AFN")))
    }

    @Test
    fun tests_base_currency_conversions() {
        // enter amount
        onView(withId(R.id.amount)).perform(click())
        onView(withId(R.id.amount)).perform(typeText("100"))

        // select base currency
        onView(withId(R.id.baseCurrencyLabel)).perform(click())
        onView(withText("USD")).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.loader)).check(matches(not(isDisplayed())))
        onView(withId(R.id.currencyList)).check(matches(isDisplayed()))
    }

    @Test
    fun tests_base_currency_conversion_displayed() {
        // enter amount
        onView(withId(R.id.amount)).perform(click())
        onView(withId(R.id.amount)).perform(typeText("100"))

        // select base currency
        onView(withId(R.id.baseCurrencyLabel)).perform(click())
        onView(withText("USD")).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.currencyList)).check(matches(TestActions.atPosition(1, isDisplayed())))
    }

    @Test
    fun tests_base_currency_conversion_click() {
        // enter amount
        onView(withId(R.id.amount)).perform(click())
        onView(withId(R.id.amount)).perform(typeText("100"))

        // select base currency
        onView(withId(R.id.baseCurrencyLabel)).perform(click())
        onView(withText("USD")).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.currencyList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, TestActions.clickViewGroupItemViaId(R.id.currencyCard)))
    }

}