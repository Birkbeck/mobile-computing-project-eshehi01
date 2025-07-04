package co.uk.bbk.culinarycompanion_enriketashehi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel for Recipe data, connects UI with data layer
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository

    init {
        val dao = RecipeDatabase
            .getDatabase(application)
            .recipeDao()
        repository = RecipeRepository(dao)
    }

    // Insert new recipe asynchronously
    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }

    // Get all recipes - suspending function for use with coroutines
    suspend fun getAllRecipes(): List<Recipe> {
        return repository.getAllRecipes()
    }

    // Get recipe by ID - suspending function
    suspend fun getRecipeById(id: Int): Recipe? {
        return repository.getRecipeById(id)
    }

    // Update recipe asynchronously
    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateRecipe(recipe)
        }
    }

    // Delete recipe asynchronously
    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }
}