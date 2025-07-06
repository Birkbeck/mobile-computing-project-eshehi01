package co.uk.bbk.culinarycompanion_enriketashehi

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// Instrumented test running on Android device or emulator
@RunWith(AndroidJUnit4::class)
class MainActivityAndDatabaseTest {

    // Scenario to launch and control MainActivity lifecycle during tests
    private lateinit var scenario: ActivityScenario<MainActivity>

    // In-memory test database instance, does not persist data to disk
    private lateinit var database: RecipeDatabase

    // DAO to interact with the database during tests
    private lateinit var dao: RecipeDao

    @Before
    fun setUp() {
        // Launch MainActivity before each test
        scenario = ActivityScenario.launch(MainActivity::class.java)

        // Obtain application context for building the database
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Create an in-memory Room database for testing (cleared after tests)
        database = Room.inMemoryDatabaseBuilder(
            context,
            RecipeDatabase::class.java
        )
            // Allow queries on the main thread for simplicity in testing
            .allowMainThreadQueries()
            .build()

        // Get the DAO to perform database operations
        dao = database.recipeDao()
    }

    @After
    fun tearDown() {
        // Close the activity and database after each test to release resources
        scenario.close()
        database.close()
    }

    @Test
    fun insertRecipe_and_ReadRecipe_returnsExpectedResult() = runBlocking {
        // Create a sample recipe to insert into the database
        val recipe = Recipe(
            id = 1,
            title = "MADB Test Recipe",
            preview = "MADB Test Preview",
            ingredients = "Ingredient One, Ingredient Two",
            instructions = "Some instructions.",
            category = "Desserts"
        )

        // Insert the sample recipe into the database
        dao.insertRecipe(recipe)

        // Retrieve the inserted recipe synchronously by ID
        val loaded = dao.getRecipeByIdSync(recipe.id)

        // Assert that the retrieved recipe is not null
        assertNotNull(loaded)

        // Assert that the recipe title matches the inserted value
        assertEquals("MADB Test Recipe", loaded?.title)

        // Assert that the recipe category matches the inserted value
        assertEquals("Desserts", loaded?.category)
    }
}