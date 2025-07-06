package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentRecipeDetailBinding

/**
 * RecipeDetailFragment displays the full details of a selected recipe.
 * - It shows the recipe's title, ingredients, and instructions.
 * - Provides buttons to return to the previous screen or edit the recipe.
 */
class RecipeDetailFragment : Fragment() {

    // ViewModel used to retrieve recipe data
    private val viewModel: RecipeViewModel by viewModels()

    // ViewBinding object to access UI elements
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    // Args passed via Safe Args navigation (contains recipeId)
    private val args: RecipeDetailFragmentArgs by navArgs()

    // ID of the recipe currently being viewed
    private var currentRecipeId: Int = -1

    /**
     * Inflate the fragment layout using ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the view is created.
     * - Retrieves the recipe ID passed via arguments.
     * - Observes the recipe data and displays it.
     * - Sets up button navigation (Back and Edit).
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Extract recipe ID from Safe Args
        currentRecipeId = args.recipeId

        // Set ActionBar title
        requireActivity().title = getString(R.string.recipe_detail)

        // Load recipe details if ID is valid
        if (currentRecipeId != -1) {
            viewModel.getRecipeById(currentRecipeId).observe(viewLifecycleOwner) { recipe ->
                if (recipe != null) {
                    displayRecipe(recipe)
                } else {
                    // Handle case where recipe was not found
                    binding.recipeTitleTextView.text = getString(R.string.recipe_not_found)
                    binding.ingredientsTextView.text = ""
                    binding.instructionsTextView.text = ""
                }
            }
        }

        // Handle back button navigation
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Navigate to the Create/Edit screen with current recipe ID
        binding.editRecipeButton.setOnClickListener {
            val action = RecipeDetailFragmentDirections
                .actionRecipeDetailFragmentToCreateEditRecipeFragment(recipeId = currentRecipeId)
            findNavController().navigate(action)
        }
    }

    /**
     * Populates the UI with the recipe's details.
     */
    private fun displayRecipe(recipe: Recipe) {
        binding.recipeTitleTextView.text = recipe.title
        binding.ingredientsTextView.text = recipe.ingredients
        binding.instructionsTextView.text = recipe.instructions
    }

    /**
     * Clean up binding when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}