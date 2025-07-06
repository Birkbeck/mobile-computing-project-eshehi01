package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for MainActivity.
 * Verifies that the activity launches correctly
 * and the app title string resource is loaded as expected.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun mainActivity_startsCorrectly() {
        // Launch MainActivity in a test scenario
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Verify the app's title string is correct
        scenario.onActivity { activity ->
            val appName = activity.getString(R.string.app_title)
            assertEquals("Culinary Companion", appName)
        }

        // Close the scenario to clean up resources
        scenario.close()
    }
}