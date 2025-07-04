package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: RecipeAdapter

    private val viewModel: RecipeViewModel by viewModels()

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

        // --- Spinner setup ---

        val categories = resources.getStringArray(R.array.recipe_categories)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.categoryFilterSpinner.adapter = spinnerAdapter

        binding.categoryFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = categories[position]
                filterRecipes(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Initially load all recipes
        loadAllRecipes()

        binding.addRecipeButton.setOnClickListener {
            val action = MainFragmentDirections
                .actionMainFragmentToCreateEditRecipeFragment(recipeId = -1)
            findNavController().navigate(action)
        }
    }

    private fun loadAllRecipes() {
        filterRecipes("All")
    }

    private fun filterRecipes(category: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val allRecipes = viewModel.getAllRecipes()

            val filteredRecipes = if (category == "All" || category == "Category") {
                allRecipes
            } else {
                allRecipes.filter { it.category == category }
            }

            adapter = RecipeAdapter(filteredRecipes) { selectedRecipe ->
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
}