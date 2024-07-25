package com.can_inanir.spacex.ui.common.bottomnav
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import com.google.accompanist.blur.Blur


@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) {
    val items = listOf(
        BottomNavItem.Rockets,
        BottomNavItem.Favorites,
        BottomNavItem.Profile,
        BottomNavItem.Upcoming,
    )
    val greenColor = Color(color = 0xFF58FBC8)

    Box(modifier = modifier) {
        // Apply BlurEffect behind the navigation bar
        Blur(10.dp) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.3f)) // Light white tint for foggy effect
            )
        }

        // Surface for the rounded corners (outline)
        Surface(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp) // Padding above the navbar
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent), // Make the Surface itself transparent
        ) {
            NavigationBar(containerColor = Color.Transparent) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = {
                            Text(
                                text = item.title,
                                color = if (currentRoute == item.route) {
                                    greenColor
                                } else {
                                    Color.White
                                }
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}