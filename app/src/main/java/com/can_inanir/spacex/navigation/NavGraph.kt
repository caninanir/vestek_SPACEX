package com.can_inanir.spacex.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.can_inanir.spacex.authandapi.AuthViewModel
import com.can_inanir.spacex.screens.FavoritesScreen
import com.can_inanir.spacex.screens.LaunchDetailScreen
import com.can_inanir.spacex.screens.LoginScreen
import com.can_inanir.spacex.screens.ProfileScreen
import com.can_inanir.spacex.screens.RocketDetailScreen
import com.can_inanir.spacex.screens.RocketsScreen
import com.can_inanir.spacex.screens.UpcomingLaunchesScreen

@Composable
fun NavGraph(signInWithGoogle: () -> Unit) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Rockets.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Rockets.route) {
                RocketsScreen(navController)
            }
            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen(navController)
            }
            composable(BottomNavItem.Upcoming.route) {
                UpcomingLaunchesScreen(navController)
            }
            composable(BottomNavItem.Login.route) {
                LoginScreen(signInWithGoogle)
            }
            composable(BottomNavItem.Profile.route) {
                if (userState != null) {
                    ProfileScreen(navController)
                } else {
                    LoginScreen(signInWithGoogle)
                }
            }
            composable(
                route = "rocketDetail/{rocketId}",
                arguments = listOf(navArgument("rocketId") { type = NavType.StringType })
            ) { backStackEntry ->
                RocketDetailScreen(backStackEntry.arguments?.getString("rocketId"))
            }
            composable(
                route = "launchDetail/{launchId}",
                arguments = listOf(navArgument("launchId") { type = NavType.StringType })
            ) { backStackEntry ->
                LaunchDetailScreen(backStackEntry.arguments?.getString("launchId"))
            }
        }
    }
}