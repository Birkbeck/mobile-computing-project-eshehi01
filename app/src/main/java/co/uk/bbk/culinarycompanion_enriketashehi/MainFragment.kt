package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentMainBinding

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

        // Test data
        val recipes = listOf(
            Recipe(
                title ="Avocado Toast",
                preview = "Mashed avocado on toast with lemon and chilli flakes",
                ingredients = "2 slices bread, 1 avocado, lemon juice, chilli flakes, salt, pepper.",
                instructions = "Toast the bread. Mash avocado with lemon juice, salt, and pepper. Spread on toast. Sprinkle chilli flakes.",
                category= "Breakfast"
            ),
            Recipe(
                title = "Chocolate Cake",
                preview = "A rich chocolate sponge cake...",
                ingredients = "Flour, cocoa powder, eggs, sugar, butter.",
                instructions = "Mix ingredients. Bake at 180°C for 30 minutes.",
                category = "Desserts"
            )
        )

        adapter = RecipeAdapter(recipes) { selectedRecipe ->
            // Handle click on ">"
            val action = MainFragmentDirections
                .actionMainFragmentToRecipeDetailFragment(
                    recipeTitle = selectedRecipe.title,
                    recipePreview = selectedRecipe.preview,
                    recipeIngredients = selectedRecipe.ingredients,
                    recipeInstructions = selectedRecipe.instructions,
                    recipeCategory = selectedRecipe.category
                )
            findNavController().navigate(action)
        }

        binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeRecyclerView.adapter = adapter

        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_createEditRecipeFragment)
        }
    }
}