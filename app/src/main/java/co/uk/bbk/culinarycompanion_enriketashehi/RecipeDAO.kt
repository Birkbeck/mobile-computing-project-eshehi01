package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object (DAO) for performing database operations on the Recipe table.
 */
@Dao
interface RecipeDao {

    /**
     * Returns a LiveData list of all recipes in the database.
     */
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>

    /**
     * Returns a LiveData object containing a single recipe by its ID,
     * or null if no recipe is found.
     */
    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getRecipeById(id: Int): LiveData<Recipe?>

    /**
     * Inserts a recipe into the database.
     * If a recipe with the same ID exists, it is replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    /**
     * Updates an existing recipe in the database.
     */
    @Update
    suspend fun updateRecipe(recipe: Recipe)

    /**
     * Deletes a recipe from the database.
     */
    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    /**
     * Returns the total number of recipes in the database.
     */
    @Query("SELECT COUNT(*) FROM recipe")
    suspend fun countRecipes(): Int

    /**
     * Retrieves a single recipe by its ID synchronously (not LiveData).
     * Useful for cases where you want the result immediately in a coroutine.
     */
    @Query("SELECT * FROM recipe WHERE id = :id")
    suspend fun getRecipeByIdSync(id: Int): Recipe?
}
