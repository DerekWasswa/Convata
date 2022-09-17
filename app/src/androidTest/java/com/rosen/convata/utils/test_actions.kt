package com.rosen.convata.utils

import android.view.View
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object TestActions {

    fun dropAutoCompletes(): ViewAction =
        object : ViewAction {
            override fun getDescription(): String = "Shows the dropdown menu of an AutoCompleteTextView"

            override fun getConstraints(): Matcher<View> = allOf(
                isEnabled(), isAssignableFrom(AutoCompleteTextView::class.java)
            )

            override fun perform(uiController: UiController, view: View) {
                val autoCompleteTextView = view as AutoCompleteTextView
                autoCompleteTextView.showDropDown()
                uiController.loopMainThreadUntilIdle()
            }
        }

    fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun clickViewGroupItemViaId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "touch click event on a list view group via an id"
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }

}