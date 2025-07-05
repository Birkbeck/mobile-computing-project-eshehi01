package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.lifecycle.LiveData

class RecipeRepository(private val recipeDao: RecipeDao) {

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipeById(id: Int): LiveData<Recipe?> {
        return recipeDao.getRecipeById(id)
    }

    suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }


}