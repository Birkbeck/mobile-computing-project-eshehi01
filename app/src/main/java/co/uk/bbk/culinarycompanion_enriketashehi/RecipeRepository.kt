package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.lifecycle.LiveData

/**
 * Repository layer for Recipe data.
 *
 * Abstracts access to the Room DAO so the rest of the app
 * doesn’t interact directly with database queries.
 *
 * Provides methods to read or modify recipes, and exposes
 * LiveData so UI can observe changes automatically.
 */
class RecipeRepository(private val recipeDao: RecipeDao) {

    /**
     * Returns a LiveData list of all recipes in the database.
     */
    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    /**
     * Returns a single recipe as LiveData by ID.
     *
     * @param id Recipe ID
     * @return LiveData object containing the recipe (or null if not found)
     */
    fun getRecipeById(id: Int): LiveData<Recipe?> {
        return recipeDao.getRecipeById(id)
    }

    /**
     * Inserts a new recipe into the database.
     * Suspends until operation completes.
     */
    suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    /**
     * Updates an existing recipe in the database.
     * Suspends until operation completes.
     */
    suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe)
    }

    /**
     * Deletes a recipe from the database.
     * Suspends until operation completes.
     */
    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }
}