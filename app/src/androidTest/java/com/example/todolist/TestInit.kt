package com.example.todolist


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Test made with expresso. Attempts to clear the actual database before each test.
 * Couldn't find a way to get it to run on its own test database.
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestInit {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testInit() {
        val textView = onView(
            allOf(
                withId(R.id.firstFragment), withText("Fragment 1 : not placeholder text"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Fragment 1 : not placeholder text")))

        val appCompatEditText = onView(
            allOf(
                withId(R.id.EditTextTODOTitle),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("abc"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.ButtonAddTODO), withText("Add TODO"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val viewGroup = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.RecyclerViewTODOItems),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.TextViewTODOTitle), withText("abc"),
                withParent(withParent(withId(R.id.RecyclerViewTODOItems))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("abc")))

        val checkBox = onView(
            allOf(
                withId(R.id.CheckBoxDone),
                withParent(withParent(withId(R.id.RecyclerViewTODOItems))),
                isDisplayed()
            )
        )
        checkBox.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
