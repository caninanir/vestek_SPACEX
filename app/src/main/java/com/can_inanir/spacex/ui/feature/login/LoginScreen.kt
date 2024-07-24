package com.can_inanir.spacex.ui.feature.login




import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.can_inanir.spacex.ui.feature.profile.ProfileScreen


@Composable
fun LoginScreen(signInWithGoogle: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()
    val navController = rememberNavController()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (userState != null) {
        ProfileScreen(navController)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { authViewModel.createAccount(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Create Account")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { signInWithGoogle() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign in with Google")
            }
        }
    }
}