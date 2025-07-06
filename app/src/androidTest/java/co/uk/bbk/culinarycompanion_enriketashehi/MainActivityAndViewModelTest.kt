package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Unit test for testing MainActivity together with the ViewModel layer
class MainActivityAndViewModelTestTest {

    // Rule that makes LiveData execute immediately and synchronously for unit testing
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // ViewModel under test
    private lateinit var viewModel: RecipeViewModel

    // Scenario to manage MainActivity lifecycle during testing
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        // Launch MainActivity before each test
        scenario = ActivityScenario.launch(MainActivity::class.java)

        // Create the RecipeViewModel with the application context
        val appContext = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.app.Application>()
        viewModel = RecipeViewModel(appContext)
    }

    @After
    fun tearDown() {
        // Close the activity scenario after each test to release resources
        scenario.close()
    }

    @Test
    fun test_MainActivityLaunches_and_ViewModelEmitsRecipes() {
        // Variable to hold the emitted LiveData value
        var emittedData: List<Recipe>? = null

        // Create an observer that captures the emitted recipe list
        val observer = Observer<List<Recipe>> {
            emittedData = it
        }

        // Observe the LiveData from the ViewModel
        viewModel.allRecipes.observeForever(observer)

        // Check that the LiveData emits at least a non-null value
        assertNotNull(emittedData)

        // Clean up the observer after the test
        viewModel.allRecipes.removeObserver(observer)
    }
}