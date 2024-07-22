package com.example.a3rdtimesthecharm
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = BottomNavItem.Rockets.route) {
        composable(BottomNavItem.Rockets.route) {
            RocketsScreen(navController)
        }
        composable(BottomNavItem.Login.route) {
            LoginScreen()
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
        composable(
            "${BottomNavItem.Rockets.route}/{rocketId}",
            arguments = listOf(navArgument("rocketId") { type = NavType.StringType })
        ) { backStackEntry ->
            RocketDetailScreen(backStackEntry.arguments?.getString("rocketId"))
        }
    }

    BottomNavBar(navController = navController)
}