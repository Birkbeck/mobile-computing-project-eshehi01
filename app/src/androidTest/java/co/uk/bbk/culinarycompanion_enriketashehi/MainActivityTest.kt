package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun mainActivity_startsCorrectly() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { activity ->
            val appName = activity.getString(R.string.app_title)
            assertEquals("Culinary Companion", appName)
        }

        scenario.close()
    }
}