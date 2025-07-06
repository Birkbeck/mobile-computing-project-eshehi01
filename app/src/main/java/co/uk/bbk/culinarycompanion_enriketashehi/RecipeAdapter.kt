package co.uk.bbk.culinarycompanion_enriketashehi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.RecipeCardItemBinding

/**
 * RecyclerView Adapter for displaying a list of recipes in the main screen.
 *
 * @param recipes List of Recipe objects to display.
 * @param onItemClick Callback function triggered when a recipe's "View Details" button is clicked.
 */
class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    /**
     * ViewHolder that holds and binds the view for a single recipe item.
     */
    inner class RecipeViewHolder(private val binding: RecipeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the recipe data to the view and sets up the click listener.
         */
        fun bind(recipe: Recipe) {
            binding.recipe = recipe // Sets the recipe variable in the layout (via data binding)
            binding.executePendingBindings() // Ensures bindings are applied immediately

            binding.viewDetailsButton.setOnClickListener {
                onItemClick(recipe) // Triggers navigation to detail view
            }
        }
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeCardItemBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    /**
     * Returns the total number of recipes to display.
     */
    override fun getItemCount(): Int = recipes.size

    /**
     * Binds data to the ViewHolder at the specified position.
     */
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}