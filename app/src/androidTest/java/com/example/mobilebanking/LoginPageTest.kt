package com.example.mobilebanking


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginPageTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginPage::class.java)

    @Test
    fun loginPageTest() {
        val textInputEditText = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.editText_loginUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("admin"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withText("admin"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.editText_loginUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(pressImeActionButton())

        val textInputEditText3 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.editText_loginPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("qwerty"), closeSoftKeyboard())

        val textInputEditText4 = onView(
            allOf(
                withText("qwerty"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.editText_loginPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(pressImeActionButton())

        val checkableImageButton = onView(
            allOf(
                withId(R.id.text_input_end_icon), withContentDescription("Show password"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        checkableImageButton.perform(click())

        val editText = onView(
            allOf(
                withText("admin"),
                withParent(withParent(withId(R.id.editText_loginUsername))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("admin")))

        val editText2 = onView(
            allOf(
                withText("qwerty"),
                withParent(withParent(withId(R.id.editText_loginPassword))),
                isDisplayed()
            )
        )
        editText2.check(matches(withText("qwerty")))

        val editText3 = onView(
            allOf(
                withText("qwerty"),
                withParent(withParent(withId(R.id.editText_loginPassword))),
                isDisplayed()
            )
        )
        editText3.check(matches(withText("qwerty")))

        val materialButton = onView(
            allOf(
                withId(R.id.button_loginLogin), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
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
