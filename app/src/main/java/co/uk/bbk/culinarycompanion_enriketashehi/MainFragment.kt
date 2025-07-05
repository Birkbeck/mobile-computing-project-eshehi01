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

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: RecipeAdapter

    private val viewModel: RecipeViewModel by viewModels()

    // Keep track of selected category
    private var currentCategory: String = "All"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.setTitle(R.string.home_screen_title)

        setupCategorySpinner()

        // Observe LiveData once
        viewModel.allRecipes.observe(viewLifecycleOwner) { allRecipes ->
            val filteredRecipes = filterRecipes(allRecipes, currentCategory)
            showRecipes(filteredRecipes)
        }

        binding.addRecipeButton.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToCreateEditRecipeFragment(recipeId = -1)
            findNavController().navigate(action)
        }
    }

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