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
            Recipe("Avocado Toast", "Mashed avocado on toast with lemon and chilli flakes ..."),
            Recipe("Pancakes", "Fluffy pancakes with maple syrup ..."),
            Recipe("Pasta Salad", "Pasta with veggies and Italian dressing ...")
        )

        adapter = RecipeAdapter(recipes) { selectedRecipe ->
            // Handle click on ">"
            val action = MainFragmentDirections
                .actionMainFragmentToRecipeDetailFragment(
                    recipeTitle = selectedRecipe.title,
                    recipePreview = selectedRecipe.preview
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