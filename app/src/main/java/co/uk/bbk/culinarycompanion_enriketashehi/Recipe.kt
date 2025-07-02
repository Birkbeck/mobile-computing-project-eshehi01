package co.uk.bbk.culinarycompanion_enriketashehi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "recipe")
data class Recipe (
    // Unique ID for each recipe, auto-generated
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    // A short description of the recipe for displaying in list
    val preview: String,
    val ingredients: String,
    val instructions: String,
    val category: String
)