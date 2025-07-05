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

class CreateEditRecipeFragment : Fragment() {

    private var _binding: FragmentCreateEditRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by viewModels()
    private val args: CreateEditRecipeFragmentArgs by navArgs()

    private var currentRecipe: Recipe? = null

    // Make these available at class-level so we can reuse them in populateFields
    private lateinit var filteredCategories: List<String>

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

        // Full list from resources (including Category and All)
        val fullCategories = resources.getStringArray(R.array.recipe_categories)

        // Filtered list excludes "Category" and "All"
        filteredCategories = fullCategories.filter { it != "Category" && it != "All" }

        // Create adapter for spinner dropdown with filtered categories
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

        // Set no selection initially
        binding.categorySpinner.setSelection(-1, false)

        // Show a spinner prompt
        binding.categorySpinner.prompt = "Category"

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = filteredCategories[position]
                // Handle the selected category
                Toast.makeText(requireContext(), "Selected: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        // Default state: hide delete button
        binding.deleteButton.visibility = View.GONE

        // Check if editing existing recipe
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

    private fun populateFields(recipe: Recipe) {
        binding.recipeNameEditText.setText(recipe.title)
        binding.ingredientsEditText.setText(recipe.ingredients)
        binding.instructionsEditText.setText(recipe.instructions)

        // Find the index in filteredCategories (not in the full list)
        val index = filteredCategories.indexOf(recipe.category)
        if (index >= 0) {
            binding.categorySpinner.setSelection(index, false)
        } else {
            // Keep unselected if not found
            binding.categorySpinner.setSelection(-1, false)
        }
    }

    private fun saveRecipe() {
        val title = binding.recipeNameEditText.text.toString().trim()
        val preview = binding.instructionsEditText.text.toString().take(30) + "..."
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

    private fun deleteRecipe() {
        currentRecipe?.let { recipe ->
            viewModel.deleteRecipe(recipe)
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}