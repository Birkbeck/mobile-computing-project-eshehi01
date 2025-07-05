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

@RunWith(AndroidJUnit4::class)
class MainActivityAndDatabaseTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var database: RecipeDatabase
    private lateinit var dao: RecipeDao

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            RecipeDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.recipeDao()
    }

    @After
    fun tearDown() {
        scenario.close()
        database.close()
    }

    @Test
    fun insertRecipe_and_ReadRecipe_returnsExpectedResult() = runBlocking {
        val recipe = Recipe(
            id = 1,
            title = "MADB Test Recipe",
            preview = "MADB Test Preview",
            ingredients = "Ingredient One, Ingredient Two",
            instructions = "Some instructions.",
            category = "Desserts"
        )

        // Insert the recipe
        dao.insertRecipe(recipe)

        // Retrieve recipe synchronously
        val loaded = dao.getRecipeByIdSync(recipe.id)

        assertNotNull(loaded)
        assertEquals("MADB Test Recipe", loaded?.title)
        assertEquals("Desserts", loaded?.category)
    }
}