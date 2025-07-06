package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentMainBinding

/**
 * MainFragment displays the home screen of the app.
 * - Shows a list of recipes.
 * - Allows filtering by category via a Spinner.
 * - Navigates to the Create/Edit screen to add a new recipe.
 * - Navigates to Recipe Detail when a recipe is clicked.
 */
class MainFragment : Fragment() {

    // View binding object for this fragment's layout
    private lateinit var binding: FragmentMainBinding

    // Adapter for the RecyclerView displaying recipes
    private lateinit var adapter: RecipeAdapter

    // ViewModel providing the recipe data (using LiveData)
    private val viewModel: RecipeViewModel by viewModels()

    // Currently selected category in the spinner ("All" by default)
    private var currentCategory: String = "All"

    /**
     * Inflates the layout using ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the view is created.
     * - Sets up UI components.
     * - Observes LiveData from ViewModel to show recipes.
     * - Handles navigation to add a new recipe.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the title for the ActionBar
        (activity as? MainActivity)?.setTitle(R.string.home_screen_title)

        // Set up the category filter spinner
        setupCategorySpinner()

        // Observe the list of all recipes from the ViewModel
        viewModel.allRecipes.observe(viewLifecycleOwner) { allRecipes ->
            val filteredRecipes = filterRecipes(allRecipes, currentCategory)
            showRecipes(filteredRecipes)
        }

        // Navigate to Create/Edit screen when user taps Add button
        binding.addRecipeButton.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToCreateEditRecipeFragment(recipeId = -1)
            findNavController().navigate(action)
        }
    }

    /**
     * Sets up the spinner dropdown for selecting recipe categories.
     * When a new category is selected:
     * - Updates currentCategory.
     * - Filters and displays recipes in that category.
     */
    private fun setupCategorySpinner() {
        val categories = resources.getStringArray(R.array.recipe_categories)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.categoryFilterSpinner.adapter = spinnerAdapter

        binding.categoryFilterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentCategory = categories[position]
                    // Whenever spinner changes, re-filter the latest recipes
                    viewModel.allRecipes.value?.let { allRecipes ->
                        val filtered = filterRecipes(allRecipes, currentCategory)
                        showRecipes(filtered)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Optional - do nothing
                }
            }
    }

    /**
     * Filters recipes based on the selected category.
     * If "All" or "Category" is selected, shows all recipes.
     *
     * @param recipes The list of all recipes from ViewModel
     * @param category The selected category from the spinner
     * @return Filtered list of recipes
     */
    private fun filterRecipes(
        recipes: List<Recipe>,
        category: String
    ): List<Recipe> {
        return if (category == "All" || category == "Category") {
            recipes
        } else {
            recipes.filter { it.category == category }
        }
    }

    /**
     * Displays the list of recipes in the RecyclerView.
     * Sets up the adapter and handles clicks to navigate to Recipe Detail.
     *
     * @param recipes The recipes to show in the list
     */
    private fun showRecipes(recipes: List<Recipe>) {
        adapter = RecipeAdapter(recipes) { selectedRecipe ->
            val action = MainFragmentDirections
                .actionMainFragmentToRecipeDetailFragment(
                    recipeId = selectedRecipe.id
                )
            findNavController().navigate(action)
        }
        binding.recipeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recipeRecyclerView.adapter = adapter
    }
}