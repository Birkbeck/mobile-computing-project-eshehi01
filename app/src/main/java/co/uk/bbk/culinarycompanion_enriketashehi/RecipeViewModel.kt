package co.uk.bbk.culinarycompanion_enriketashehi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for Recipe data.
 *
 * Acts as the middle layer between the UI and the data layer (Repository/Room).
 * Exposes LiveData so UI can observe recipe data.
 * Uses viewModelScope for launching coroutines to perform DB operations off the main thread.
 */
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository

    // LiveData list of all recipes, automatically updates UI on changes
    val allRecipes: LiveData<List<Recipe>>

    init {
        // Obtain DAO from the singleton database instance
        val dao = RecipeDatabase
            .getDatabase(application)
            .recipeDao()

        // Create repository to abstract data operations
        repository = RecipeRepository(dao)

        // Initialize LiveData with all recipes
        allRecipes = repository.getAllRecipes()
    }

    /**
     * Inserts a new recipe into the database.
     * Runs on background thread via coroutine.
     */
    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }

    /**
     * Returns a single recipe as LiveData based on its ID.
     * Used for detail screens or editing existing recipes.
     */
    fun getRecipeById(id: Int): LiveData<Recipe?> {
        return repository.getRecipeById(id)
    }

    /**
     * Updates an existing recipe in the database.
     * Runs on background thread via coroutine.
     */
    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateRecipe(recipe)
        }
    }

    /**
     * Deletes a recipe from the database.
     * Runs on background thread via coroutine.
     */
    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }
}