package com.example.a3rdtimesthecharm


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Rockets : BottomNavItem("rockets", "Rockets", Icons.Default.Home)
    object Login : BottomNavItem("login", "Login", Icons.Default.Lock)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}