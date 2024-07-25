package com.can_inanir.spacex.ui.common.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier, activity: ComponentActivity) {
    val items = listOf(
        BottomNavItem.Rockets,
        BottomNavItem.Favorites,
        BottomNavItem.Profile,
        BottomNavItem.Upcoming,
    )
    val greenColor = Color(0xFF58FBC8)

    Box(
        modifier = modifier
            .background(Color.Transparent)
            .padding(horizontal = 50.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 12.dp) // Padding from the sides
                .size(width = 336.dp, height = 56.dp) // Snug fit for the bottom nav bar
                .clip(RoundedCornerShape(16.dp)) // Rounded corners
                .background(Color(0x70FFFFFF)), // Semi-transparent white background
            color = Color.Transparent // Set surface color to transparent
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                modifier = Modifier.size(336.dp, 50.dp) // Ensure NavigationBar fits within the Surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = if (currentRoute == item.route) {
                                    greenColor
                                } else {
                                    Color.White
                                },
                                fontSize = 10.sp
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        alwaysShowLabel = false // Hide the label to avoid extra space
                    )
                }
            }
        }
    }
}