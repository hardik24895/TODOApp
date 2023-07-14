package com.example.hardiknoteapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class AppUITest(){

    @get : Rule
    var mActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isViewIsDisplay() {
        onView(withId(R.id.animAddNote)).check(matches(isDisplayed()))
    }

    @Test
    fun A_addNewNote(){
        onView(withId(R.id.animAddNote)).perform(click())
        onView(withId(R.id.edtTitle)).perform(ViewActions.typeText("Test"))
        onView(withId(R.id.edtDesc)).perform(ViewActions.typeText("Test Desc"), closeSoftKeyboard())
        onView(withId(R.id.imgSave)).perform(click())
        onView(withId(R.id.animAddNote)).check(matches(isDisplayed()))
    }


    @Test
    fun B_updateNote(){
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))
        onView(withId(R.id.edtTitle)).perform(ViewActions.typeText("Test Update"))
        onView(withId(R.id.edtDesc)).perform(ViewActions.typeText("Test Desc update"), closeSoftKeyboard())
        onView(withId(R.id.imgSave)).perform(click())
        onView(withId(R.id.animAddNote)).check(matches(isDisplayed()))
    }

    @Test
    fun C_deleteNote(){
        onView(withId(R.id.rv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))
        onView(withId(R.id.imgDelete)).perform(click())
        val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val button: UiObject = uiDevice.findObject(UiSelector().text("Yes"))
        if (button.exists() && button.isEnabled) {
            button.click()
        }
        onView(withId(R.id.animAddNote)).check(matches(isDisplayed()))
    }

}