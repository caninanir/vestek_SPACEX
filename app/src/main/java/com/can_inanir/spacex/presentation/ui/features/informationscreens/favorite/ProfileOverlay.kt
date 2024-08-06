package com.can_inanir.spacex.presentation.ui.features.informationscreens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.presentation.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.presentation.utils.AppColors
import com.can_inanir.spacex.presentation.viewmodel.AuthViewModel


@Composable
fun ProfileOverlay(
    authViewModel: AuthViewModel,
    navController: NavController,
    onClose: () -> Unit,
) {
    val userState by authViewModel.userState.collectAsState()

    Box(
        modifier = Modifier
            .size(width = 250.dp, height = 150.dp)
            .background(AppColors.HalfGrayTransparentBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.logged_in_as),
                color = AppColors.CoolGreen
            )
            userState?.let { user ->
                Text(text = "${user.email}", color = AppColors.CoolGreen)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = AppColors.White
                    ),
                    onClick = {
                        authViewModel.logout()
                        navController.navigate(BottomNavItem.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                        onClose()
                    }
                ) {
                    Text(text = stringResource(R.string.logout))
                }
            }
        }
    }
}