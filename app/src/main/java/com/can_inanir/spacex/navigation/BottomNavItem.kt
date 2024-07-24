package com.can_inanir.spacex.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Rockets : BottomNavItem("rockets", "Rockets", Icons.Default.Home)
    data object Login : BottomNavItem("login", "Login", Icons.Default.Lock)
    data object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
    data object Favorites : BottomNavItem("favorites", "Favorites", Icons.Default.Star)
    data object Upcoming : BottomNavItem("upcoming", "Upcoming", Icons.Default.DateRange)
}