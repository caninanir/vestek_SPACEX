package com.can_inanir.spacex.ui.main

import android.annotation.SuppressLint
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
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.ui.feature.login.AuthViewModel
import com.can_inanir.spacex.ui.feature.favorite.FavoritesScreen
import com.can_inanir.spacex.ui.feature.launchdetail.LaunchDetailScreen
import com.can_inanir.spacex.ui.feature.login.LoginScreen
import com.can_inanir.spacex.ui.feature.profile.ProfileScreen
import com.can_inanir.spacex.ui.feature.rocketdetail.RocketDetailScreen
import com.can_inanir.spacex.ui.feature.rockets.RocketsScreen
import com.can_inanir.spacex.ui.feature.upcominglaunches.UpcomingLaunchesScreen

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavGraph(signInWithGoogle: () -> Unit) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()


        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Rockets.route,
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
