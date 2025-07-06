package co.uk.bbk.culinarycompanion_enriketashehi

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit test that verifies integration between:
 * - Room database
 * - ViewModel layer
 *
 * Ensures the ViewModel correctly reads data from the database.
 */
class ViewModelAndDatabaseTest {

    // Rule to force LiveData to execute synchronously during tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RecipeDatabase
    private lateinit var appContext: Application
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setUp() {
        // Get the application context used for initializing the database
        appContext = ApplicationProvider.getApplicationContext<Application>()

        // Create an in-memory Room database for testing
        database = Room.inMemoryDatabaseBuilder(
            appContext,
            RecipeDatabase::class.java
        )
            .allowMainThreadQueries() // Safe here because this is a test environment
            .build()

        // Set the singleton instance of RecipeDatabase to this in-memory test instance
        RecipeDatabase.setTestInstance(database)

        // Create the ViewModel, which will now use the test database
        viewModel = RecipeViewModel(appContext)
    }

    @After
    fun tearDown() {
        // Clean up resources after each test
        database.close()
        RecipeDatabase.clearTestInstance()
    }

    @Test
    fun insertRecipe_and_ViewModelEmitsRecipeList() = runBlocking {
        // Create a test recipe to insert
        val recipe = Recipe(
            id = 1,
            title = "VMDB Test Recipe",
            preview = "VMDB Test Preview",
            ingredients = "Ingredient One, Ingredient Two",
            instructions = "Some instructions",
            category = "Dinner"
        )

        // Insert the recipe directly into the database using the DAO
        database.recipeDao().insertRecipe(recipe)

        // Variable to hold the list of recipes observed from the ViewModel
        var recipesFromVM: List<Recipe>? = null

        // Switch to the main dispatcher to observe LiveData
        withContext(Dispatchers.Main) {
            val observer = androidx.lifecycle.Observer<List<Recipe>> {
                recipesFromVM = it
            }

            // Observe the LiveData exposed by the ViewModel
            viewModel.allRecipes.observeForever(observer)

            // Assert that LiveData emitted a non-null list
            assertNotNull(recipesFromVM)
            assertEquals(1, recipesFromVM?.size)
            assertEquals("VMDB Test Recipe", recipesFromVM?.first()?.title)

            // Remove observer to avoid leaks
            viewModel.allRecipes.removeObserver(observer)
        }
    }
}