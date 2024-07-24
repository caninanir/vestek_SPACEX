package com.can_inanir.spacex.ui.feature.profile


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import com.can_inanir.spacex.ui.feature.login.AuthViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem

@Composable
fun ProfileScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()

    LaunchedEffect(userState) {
        if (userState == null) {
            navController.navigate(BottomNavItem.Login.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile")
        Spacer(modifier = Modifier.height(16.dp))
        userState?.let { user ->
            Text(text = "Email: ${user.email}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.logout()
                navController.navigate(BottomNavItem.Login.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }) {
                Text(text = "Logout")
            }
        }
    }
}