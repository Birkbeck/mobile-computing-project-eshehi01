package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: RecipeAdapter

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

        (activity as MainActivity).setTitle(R.string.home_screen_title)

        val db = RecipeDatabase.getDatabase(requireContext())
        val recipeDao = db.recipeDao()

        // Load recipes from the database in a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            val recipes = recipeDao.getAllRecipes()

            // Create adapter with the loaded recipes
            adapter = RecipeAdapter(recipes) { selectedRecipe ->
                // Handle click on ">"
                val action = MainFragmentDirections
                    .actionMainFragmentToRecipeDetailFragment(
                        recipeTitle = selectedRecipe.title,
                        recipePreview = selectedRecipe.preview,
                        recipeCategory = selectedRecipe.category,
                        recipeIngredients = selectedRecipe.ingredients,
                        recipeInstructions = selectedRecipe.instructions
                    )
                findNavController().navigate(action)
            }

            binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recipeRecyclerView.adapter = adapter
        }

        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createEditRecipeFragment)
        }
    }
}