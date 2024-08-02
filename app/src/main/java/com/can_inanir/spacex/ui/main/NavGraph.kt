package com.can_inanir.spacex.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.ui.feature.easteregg.EasterEggScreen
import com.can_inanir.spacex.ui.feature.informationscreens.favorite.FavoritesScreen
import com.can_inanir.spacex.ui.feature.informationscreens.rockets.RocketsScreen
import com.can_inanir.spacex.ui.feature.informationscreens.upcominglaunches.UpcomingLaunchesScreen
import com.can_inanir.spacex.ui.feature.login.LoginScreen

@Composable
fun NavGraph(signInWithGoogle: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Rockets.route,
    ) {
        composable(BottomNavItem.Rockets.route) {
            RocketsScreen(navController = navController)
        }
        composable(BottomNavItem.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(BottomNavItem.Upcoming.route) {
            UpcomingLaunchesScreen(navController = navController)
        }
        composable(BottomNavItem.Login.route) {
            LoginScreen(navController, signInWithGoogle)
        }
        composable(BottomNavItem.Profile.route) {
            LoginScreen(navController, signInWithGoogle)
        }
        composable("easter_egg") {
            EasterEggScreen(navController)
        }
    }
}
