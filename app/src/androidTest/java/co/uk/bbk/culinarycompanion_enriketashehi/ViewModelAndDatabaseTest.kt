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

class ViewModelAndDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RecipeDatabase
    private lateinit var appContext: Application
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext<Application>()

        // Create in-memory Room database
        database = Room.inMemoryDatabaseBuilder(
            appContext,
            RecipeDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        // Override singleton INSTANCE so ViewModel uses the in-memory database
        RecipeDatabase.setTestInstance(database)

        // Create the ViewModel
        viewModel = RecipeViewModel(appContext)
    }

    @After
    fun tearDown() {
        database.close()
        RecipeDatabase.clearTestInstance()
    }

    @Test
    fun insertRecipe_and_ViewModelEmitsRecipeList() = runBlocking {
        val recipe = Recipe(
            id = 1,
            title = "VMDB Test Recipe",
            preview = "VMDB Test Preview",
            ingredients = "Ingredient One, Ingredient Two",
            instructions = "Some instructions",
            category = "Dinner"
        )

        // Insert directly into DB through DAO
        database.recipeDao().insertRecipe(recipe)

        var recipesFromVM: List<Recipe>? = null

        withContext(Dispatchers.Main) {
            val observer = androidx.lifecycle.Observer<List<Recipe>> {
                recipesFromVM = it
            }

            viewModel.allRecipes.observeForever(observer)

            assertNotNull(recipesFromVM)
            assertEquals(1, recipesFromVM?.size)
            assertEquals("VMDB Test Recipe", recipesFromVM?.first()?.title)

            viewModel.allRecipes.removeObserver(observer)
        }
    }
}