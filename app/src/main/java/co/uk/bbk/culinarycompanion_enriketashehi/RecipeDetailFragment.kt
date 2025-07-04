package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeDetailFragmentArgs by navArgs()

    private var currentRecipeId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentRecipeId = args.recipeId

        // Update the activity title
        requireActivity().title = getString(R.string.recipe_detail)

        // Load the recipe data from the ViewModel/database
        if (currentRecipeId != -1) {
            viewLifecycleOwner.lifecycleScope.launch {
                val recipe = viewModel.getRecipeById(currentRecipeId)
                if (recipe != null) {
                    displayRecipe(recipe)
                } else {
                    // Handle recipe not found
                    binding.recipeTitleTextView.text = getString(R.string.recipe_not_found)
                }
            }
        }

        // Handle back button click
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Handle Edit Recipe button click
        binding.editRecipeButton.setOnClickListener {
            // Pass recipeId to Create/Edit screen for editing
            val action = RecipeDetailFragmentDirections
                .actionRecipeDetailFragmentToCreateEditRecipeFragment(recipeId = currentRecipeId)
            findNavController().navigate(action)
        }
    }

    private fun displayRecipe(recipe: Recipe) {
        binding.recipeTitleTextView.text = recipe.title
        binding.ingredientsTextView.text = recipe.ingredients
        binding.instructionsTextView.text = recipe.instructions
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}