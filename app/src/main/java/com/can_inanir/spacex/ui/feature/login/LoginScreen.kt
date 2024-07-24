package com.can_inanir.spacex.ui.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.ui.feature.profile.ProfileScreen

@Composable
fun LoginScreen(signInWithGoogle: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()
    val navController = rememberNavController()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    if (userState != null) {
        ProfileScreen(navController)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_page_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.space_x_white_logo_wine),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(9000.dp)
                        .height(38.dp)
                        .padding(bottom = 23.dp)
                )

                LoginInputField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingIcon = if (email.isEmpty()) R.drawable.form_elements_icons_email_enable_white else R.drawable.form_elements_icons_email_active
                )

                Spacer(modifier = Modifier.height(16.dp))

                LoginInputField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    leadingIcon = if (password.isEmpty()) R.drawable.form_elements_icons_password_enable_white else R.drawable.form_elements_icons_password_active,
                    trailingIcon = {
                        PasswordVisibilityIcon(
                            passwordVisible,
                            onClick = { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()
                Button(
                    onClick = { authViewModel.login(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = isLoginEnabled
                ) {
                    Text(text = "Login")
                }

                Text(
                    text = "or",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = { signInWithGoogle() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Sign in with Google")
                }

                Text(
                    text = "Sign up",
                    fontSize = 13.5.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun LoginInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: Int,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF3F3F3).copy(alpha = 0.2f),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 2.dp
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = "$label Icon",
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PasswordVisibilityIcon(passwordVisible: Boolean, onClick: () -> Unit) {
    val icon = if (passwordVisible) R.drawable.form_elements_icons_hide_password_active else R.drawable.form_elements_icons_hide_password_enable_white
    Image(
        painter = painterResource(id = icon),
        contentDescription = "Toggle Password Visibility",
        modifier = Modifier
            .size(20.dp)
            .clickable { onClick() }
    )
}


/*
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

 */