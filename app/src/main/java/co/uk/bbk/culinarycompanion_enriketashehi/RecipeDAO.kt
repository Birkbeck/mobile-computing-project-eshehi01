package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getRecipeById(id: Int): LiveData<Recipe?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT COUNT(*) FROM recipe")
    suspend fun countRecipes(): Int

    @Query("SELECT * FROM recipe WHERE id = :id")
    suspend fun getRecipeByIdSync(id: Int): Recipe?
}
