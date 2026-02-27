package app.majodesk.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

fun iconFromName(name: String): ImageVector = when (name) {
    "fitness_center" -> Icons.Default.FitnessCenter
    "menu_book" -> Icons.Default.MenuBook
    "category" -> Icons.Default.Category

    // новые
    "home" -> Icons.Default.Home
    "work" -> Icons.Default.Work
    "school" -> Icons.Default.School
    "favorite" -> Icons.Default.Favorite
    "star" -> Icons.Default.Star
    "shopping_cart" -> Icons.Default.ShoppingCart
    "restaurant" -> Icons.Default.Restaurant
    "directions_run" -> Icons.Default.DirectionsRun
    "music_note" -> Icons.Default.MusicNote
    "movie" -> Icons.Default.Movie
    "palette" -> Icons.Default.Palette
    "pets" -> Icons.Default.Pets
    "sports_tennis" -> Icons.Default.SportsTennis
    else -> Icons.Default.Category
}

fun colorFromHex(hex: String): Color {
    val hexString = hex.trimStart('#')
    val intValue = when (hexString.length) {
        6 -> {
            // RGB без альфы – добавляем FF (полная непрозрачность)
            (0xFF000000.toInt() or hexString.toLong(16).toInt())
        }
        8 -> {
            // ARGB
            hexString.toLong(16).toInt()
        }
        else -> throw IllegalArgumentException("Invalid hex color: $hex")
    }
    return Color(intValue)
}