package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentCreateEditRecipeBinding

class CreateEditRecipeFragment : Fragment() {

    private var _binding: FragmentCreateEditRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEditRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            // Navigate back to the previous page
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.recipeNameEditText.text.toString()
            val preview = binding.instructionsEditText.text.toString().take(30) + "..."
            val ingredients = binding.ingredientsEditText.text.toString()
            val instructions = binding.instructionsEditText.text.toString()
            val category = binding.categorySpinner.selectedItem?.toString() ?: ""

            if (title.isBlank() || category.isBlank()) {
                // Handle validation error
                return@setOnClickListener
            }

            val newRecipe = Recipe(
                title = title,
                preview = preview,
                ingredients = ingredients,
                instructions = instructions,
                category = category
            )

            viewModel.insertRecipe(newRecipe)
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}