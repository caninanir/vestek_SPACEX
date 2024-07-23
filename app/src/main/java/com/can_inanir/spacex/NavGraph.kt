package com.can_inanir.spacex

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument



@Composable
fun NavGraph(signInWithGoogle: () -> Unit) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()

    NavHost(navController = navController, startDestination = BottomNavItem.Rockets.route) {
        composable(BottomNavItem.Rockets.route) {
            RocketsScreen(navController)
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
    }
    BottomNavBar(navController = navController)
}