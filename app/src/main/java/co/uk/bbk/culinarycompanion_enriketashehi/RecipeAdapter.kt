package co.uk.bbk.culinarycompanion_enriketashehi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.uk.bbk.culinarycompanion_enriketashehi.databinding.RecipeCardItemBinding

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(private val binding: RecipeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.recipe = recipe
            binding.executePendingBindings()

            binding.viewDetailsButton.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeCardItemBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}