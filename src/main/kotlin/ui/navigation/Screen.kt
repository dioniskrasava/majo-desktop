package app.majodesk.ui.navigation

sealed class Screen(val route: String) {
    object Activities : Screen("activities")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
}