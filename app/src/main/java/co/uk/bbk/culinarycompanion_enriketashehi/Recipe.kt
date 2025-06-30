package co.uk.bbk.culinarycompanion_enriketashehi

data class Recipe (
    val title: String,
    val ingredients: String,
    val instructions: String,
    val category: String,
    val preview: String // A short description of the recipe for displaying in list
)