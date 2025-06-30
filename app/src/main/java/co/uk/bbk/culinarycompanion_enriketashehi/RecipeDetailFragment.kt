package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RecipeDetailFragmentArgs by navArgs()

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

        // Update the activity title
        requireActivity().title = getString(R.string.recipe_detail)

        // Set the text fields using arguments passed from MainFragment
        binding.recipeTitleTextView.text = args.recipeTitle
        binding.ingredientsTextView.text = args.recipePreview

        // Handle back button click
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Handle Edit Recipe button click
        binding.editRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_recipeDetailFragment_to_createEditRecipeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}