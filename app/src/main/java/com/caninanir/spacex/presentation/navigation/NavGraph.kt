package com.caninanir.spacex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.caninanir.spacex.presentation.common.bottomnav.BottomNavItem
import com.caninanir.spacex.presentation.ui.features.easteregg.EasterEggScreen
import com.caninanir.spacex.presentation.ui.features.informationscreens.favorite.FavoritesScreen
import com.caninanir.spacex.presentation.ui.features.informationscreens.rockets.RocketsScreen
import com.caninanir.spacex.presentation.ui.features.informationscreens.upcominglaunches.UpcomingLaunchesScreen
import com.caninanir.spacex.presentation.ui.features.login.LoginScreen

@Composable
fun NavGraph(navController: NavHostController, signInWithGoogle: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Rockets.route,
    ) {
        composable(BottomNavItem.Rockets.route) { RocketsScreen(navController = navController) }
        composable(BottomNavItem.Favorites.route) { FavoritesScreen(navController = navController) }
        composable(BottomNavItem.Upcoming.route) { UpcomingLaunchesScreen(navController = navController) }
        composable(BottomNavItem.Login.route) { LoginScreen(navController, signInWithGoogle) }
        composable(BottomNavItem.Profile.route) { LoginScreen(navController, signInWithGoogle) }
        composable("easter_egg") { EasterEggScreen(navController) }
    }
}
