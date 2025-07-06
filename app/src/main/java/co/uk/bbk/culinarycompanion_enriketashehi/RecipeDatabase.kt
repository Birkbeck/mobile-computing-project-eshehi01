package co.uk.bbk.culinarycompanion_enriketashehi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Room database class for the Recipe entity.
 * Provides a singleton instance of the database and prepopulates it on first run.
 */
@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    // Abstract function to get access to the DAO
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * If it doesn't exist yet, builds it using Room.
         */
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                    .addCallback(roomCallback) // Add callback to prepopulate the DB
                    .fallbackToDestructiveMigration(false) // Avoid wiping data on migration
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback triggered when the database is opened.
         * Used here to check if prepopulation is needed.
         */
        private val roomCallback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        prepopulateDatabase(database.recipeDao())
                    }
                }
            }
        }

        /**
         * Inserts a predefined list of recipes into the database if it's currently empty.
         */
        private suspend fun prepopulateDatabase(recipeDao: RecipeDao) {
            val count = recipeDao.countRecipes()
            if (count == 0) {
                val recipes = listOf(
                    Recipe(
                        title = "Avocado Toast",
                        preview = "Toasted bread topped with smashed avocado...",
                        ingredients = "1 ripe avocado\n2 slices sourdough bread\nSalt\nPepper\nLemon juice",
                        instructions = "1. Toast the bread.\n2. Smash the avocado with salt, pepper, and lemon juice.\n3. Spread on toast.",
                        category = "Breakfast"
                    ),
                    Recipe(
                        title = "Falafel Wrap",
                        preview = "Crispy falafel wrapped with salad and tahini...",
                        ingredients = "4 falafel balls\n1 flatbread\nLettuce\nTomato\nTahini sauce",
                        instructions = "1. Warm the flatbread.\n2. Add lettuce and sliced tomato.\n3. Add falafel and drizzle with tahini.\n4. Roll and serve.",
                        category = "Lunch"
                    ),
                    Recipe(
                        title = "Baingan Bharta",
                        preview = "Smoky mashed aubergine cooked with spices...",
                        ingredients = "1 aubergine\n1 onion\n1 tomato\nGarlic\nSpices",
                        instructions = "1. Roast aubergine and mash.\n2. Fry onions and garlic.\n3. Add tomatoes and spices.\n4. Mix in mashed aubergine.",
                        category = "Dinner"
                    ),
                    Recipe(
                        title = "Greek Salad",
                        preview = "Fresh tomatoes, cucumber, olives, and tofu feta...",
                        ingredients = "Tomatoes\nCucumber\nRed onion\nOlives\nTofu feta\nOlive oil\nOregano",
                        instructions = "1. Chop vegetables.\n2. Mix with olives and tofu feta.\n3. Drizzle with olive oil and sprinkle oregano.",
                        category = "Brunch"
                    ),
                    Recipe(
                        title = "Chocolate Lava Cake",
                        preview = "Rich vegan chocolate cake with molten centre...",
                        ingredients = "Dark chocolate\nFlour\nCoconut oil\nSugar\nPlant milk\nBaking powder",
                        instructions = "1. Prepare batter.\n2. Pour into ramekins.\n3. Bake until edges are set.\n4. Serve warm.",
                        category = "Desserts"
                    ),
                    Recipe(
                        title = "Lentil Soup",
                        preview = "Hearty soup with lentils, carrots, and spices...",
                        ingredients = "1 cup lentils\n2 carrots\n1 onion\nGarlic\nCumin\nVegetable broth",
                        instructions = "1. Sauté onion and garlic.\n2. Add chopped carrots and spices.\n3. Add lentils and broth.\n4. Simmer until soft.",
                        category = "Other"
                    )
                )
                recipes.forEach { recipeDao.insertRecipe(it) }
            }
        }

        /**
         * Used in unit tests to replace the singleton instance with a test database.
         */
        fun setTestInstance(testDb: RecipeDatabase) {
            INSTANCE = testDb
        }

        /**
         * Clears the singleton instance, useful for resetting between tests.
         */
        fun clearTestInstance() {
            INSTANCE = null
        }

    }
}