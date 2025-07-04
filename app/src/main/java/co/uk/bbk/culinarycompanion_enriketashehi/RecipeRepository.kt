package co.uk.bbk.culinarycompanion_enriketashehi

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun getRecipeById(id: Int): Recipe? {
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

    suspend fun getAllRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes()
    }
}