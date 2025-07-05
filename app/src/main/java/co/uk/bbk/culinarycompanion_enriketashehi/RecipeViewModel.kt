package co.uk.bbk.culinarycompanion_enriketashehi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel for Recipe data, connects UI with data layer
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository

    val allRecipes: LiveData<List<Recipe>>

    init {
        val dao = RecipeDatabase
            .getDatabase(application)
            .recipeDao()
        repository = RecipeRepository(dao)

        // Initialize LiveData for all recipes
        allRecipes = repository.getAllRecipes()
    }

    // Insert new recipe
    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }

    // Get recipe by ID
    fun getRecipeById(id: Int): LiveData<Recipe?> {
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