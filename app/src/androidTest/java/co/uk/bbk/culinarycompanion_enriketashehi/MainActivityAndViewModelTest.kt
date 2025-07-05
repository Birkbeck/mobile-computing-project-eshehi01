package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityAndViewModelTestTest {

    // Allows LiveData to execute synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RecipeViewModel
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        // Launch the Activity
        scenario = ActivityScenario.launch(MainActivity::class.java)

        // Create the ViewModel
        val appContext = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.app.Application>()
        viewModel = RecipeViewModel(appContext)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun test_MainActivityLaunches_and_ViewModelEmitsRecipes() {
        // Observe LiveData once and verify data is emitted
        var emittedData: List<Recipe>? = null

        val observer = Observer<List<Recipe>> {
            emittedData = it
        }

        viewModel.allRecipes.observeForever(observer)

        // Check that the emitted data is not null (even if empty)
        assertNotNull(emittedData)

        viewModel.allRecipes.removeObserver(observer)
    }
}