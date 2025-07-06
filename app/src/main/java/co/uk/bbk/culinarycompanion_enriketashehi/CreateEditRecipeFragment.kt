package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentCreateEditRecipeBinding

/**
 * Fragment for creating or editing a recipe.
 *
 * - Allows entering title, preview, ingredients, instructions, and category.
 * - Handles saving or deleting recipes via the ViewModel.
 */
class CreateEditRecipeFragment : Fragment() {

    // ViewBinding to access UI components
    private var _binding: FragmentCreateEditRecipeBinding? = null
    private val binding get() = _binding!!

    // ViewModel for accessing recipe data
    private val viewModel: RecipeViewModel by viewModels()

    // Safe Args to receive arguments from navigation (recipeId)
    private val args: CreateEditRecipeFragmentArgs by navArgs()

    // Holds the recipe currently being edited, or null if creating new
    private var currentRecipe: Recipe? = null

    // Filtered list of categories excluding "Category" and "All"
    private lateinit var filteredCategories: List<String>

    /**
     * Inflate the fragment layout using ViewBinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEditRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Set up UI and logic once the view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load full list of categories from resources (including Category and All)
        val fullCategories = resources.getStringArray(R.array.recipe_categories)

        // Exclude "Category" and "All" from dropdown list
        filteredCategories = fullCategories.filter { it != "Category" && it != "All" }

        // Create spinner adapter with filtered categories
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filteredCategories
        ) {
            override fun getCount(): Int {
                return filteredCategories.size
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        // No initial selection
        binding.categorySpinner.setSelection(-1, false)

        // Optional prompt text for spinner
        binding.categorySpinner.prompt = "Category"

        // Listen for spinner selections (currently shows a Toast)
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected category
                val selectedCategory = filteredCategories[position]
                Toast.makeText(requireContext(), "Selected: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        // Hide delete button by default (only shown in edit mode)
        binding.deleteButton.visibility = View.GONE

        // Check if we are editing an existing recipe
        if (args.recipeId != -1) {
            viewModel.getRecipeById(args.recipeId).observe(viewLifecycleOwner) { recipe ->
                if (recipe != null) {
                    currentRecipe = recipe
                    populateFields(recipe)
                    binding.deleteButton.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Navigation for toolbar buttons
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            saveRecipe()
        }

        binding.deleteButton.setOnClickListener {
            deleteRecipe()
        }
    }

    /**
     * Populate all UI fields with data from the given recipe.
     *
     * @param recipe the Recipe to display in the UI
     */
    private fun populateFields(recipe: Recipe) {
        binding.recipeNameEditText.setText(recipe.title)
        binding.previewEditText.setText(recipe.preview)
        binding.ingredientsEditText.setText(recipe.ingredients)
        binding.instructionsEditText.setText(recipe.instructions)

        // Find correct index for spinner selection
        val index = filteredCategories.indexOf(recipe.category)
        if (index >= 0) {
            binding.categorySpinner.setSelection(index, false)
        } else {
            // Keep spinner unselected if category not found
            binding.categorySpinner.setSelection(-1, false)
        }
    }

    /**
     * Save a new or edited recipe to the database.
     */
    private fun saveRecipe() {
        val title = binding.recipeNameEditText.text.toString().trim()
        val preview = binding.previewEditText.text.toString().trim()
        val ingredients = binding.ingredientsEditText.text.toString().trim()
        val instructions = binding.instructionsEditText.text.toString().trim()

        val selectedPosition = binding.categorySpinner.selectedItemPosition
        val category = if (selectedPosition != AdapterView.INVALID_POSITION) {
            filteredCategories[selectedPosition]
        } else {
            ""
        }

        if (title.isBlank() || category.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please enter a recipe title and select a category.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val recipeToSave = Recipe(
            id = currentRecipe?.id ?: 0,
            title = title,
            preview = preview,
            ingredients = ingredients,
            instructions = instructions,
            category = category
        )

        if (currentRecipe == null) {
            viewModel.insertRecipe(recipeToSave)
        } else {
            viewModel.updateRecipe(recipeToSave)
        }

        findNavController().navigateUp()
    }

    /**
     * Delete the currently edited recipe from the database.
     */
    private fun deleteRecipe() {
        currentRecipe?.let { recipe ->
            viewModel.deleteRecipe(recipe)
            findNavController().navigateUp()
        }
    }

    /**
     * Clean up binding when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}