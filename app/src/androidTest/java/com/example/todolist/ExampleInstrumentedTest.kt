package com.example.todolist

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var instrumentationContext: Context
    lateinit var appContext: Context
    lateinit var testDB : FragmentDBHelper
    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        /** Modifies the actual database due to using appContext,
         *  but I couldn't get it to work the the test/instrumentationContext --
         *  it doesn't have write permissions to create the file where the new database
         *  would be stored. **/
        appContext.deleteDatabase(FragmentDBHelper.DATABASE_NAME)
        testDB = FragmentDBHelper(appContext, null)
        var str1 = testDB.loadAndParseItemsOnInit()
        assert(str1 == "not placeholder text")
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.todolist", appContext.packageName)
    }

    @Test
    fun fragmentDBLoadsOnInit() {
        var str1 = testDB.loadAndParseItemsOnInit()
        assert(str1 == "not placeholder text")
    }

    @Test
    fun fragmentDBOnlyOneValue() {
        testDB.addItem("Hello World")
        assert(testDB.getSingleItemText() == "Hello World")
        testDB.addItem("Goodbye World")
        assert(testDB.getSingleItemText() == "Goodbye World")
        testDB.updateItem(1, "New World")
        assert(testDB.getSingleItemText() == "New World")

    }
}