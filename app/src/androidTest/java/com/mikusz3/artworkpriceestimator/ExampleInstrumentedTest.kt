package com.mikusz3.artworkpriceestimator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mikusz3.artworkpriceestimator", appContext.packageName)
    }

    @Test
    fun estimatorScreen_showsCoreInputs() {
        composeRule.onNodeWithText("Artwork Price Estimator").assertIsDisplayed()
        composeRule.onNodeWithText("OpenAI API Key").assertIsDisplayed()
        composeRule.onNodeWithText("Upload Artwork").assertIsDisplayed()
        composeRule.onNodeWithText("Analyze and Estimate Price").assertIsDisplayed()
    }
}