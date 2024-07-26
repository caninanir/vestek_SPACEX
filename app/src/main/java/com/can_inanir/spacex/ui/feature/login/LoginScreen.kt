package com.can_inanir.spacex.ui.feature.login

import androidx.compose.foundation.Image
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

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.welcome_page_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.space_x_white_logo_wine),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(285.dp)
                        .height(38.dp)
                        .padding(bottom = 23.dp)
                )
                LoginInputField(
                    value = email,
                    onValueChange = { email = it },
                    labelIcon = R.drawable.form_elements_text_content_active_white2,
                    leadingIcon = if (email.isEmpty()) R.drawable.form_elements_icons_email_enable_white else R.drawable.form_elements_icons_email_active
                )
                Spacer(modifier = Modifier.height(16.dp))
                LoginInputField(
                    value = password,
                    onValueChange = { password = it },
                    labelIcon = R.drawable.form_elements_text_content_active_white,
                    leadingIcon = if (password.isEmpty()) R.drawable.form_elements_icons_password_enable_white else R.drawable.form_elements_icons_password_active,
                    trailingIcon = {
                        PasswordVisibilityIcon(passwordVisible, onClick = { passwordVisible = !passwordVisible })
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()
                CustomButton(
                    onClick = { authViewModel.login(email, password) },
                    enabled = isLoginEnabled,
                    enabledImageId = R.drawable.buttons_primary_enable,
                    disabledImageId = R.drawable.buttons_primary_disable,
                    contentDescription = "Login"
                ) {
                    Text(text = "Login", color = Color.White)
                }
                Text(
                    text = "or",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
                CustomButton(
                    onClick = { signInWithGoogle() },
                    enabled = true,
                    enabledImageId = R.drawable.buttons_secondary_enable,
                    disabledImageId = R.drawable.buttons_secondary_enable,
                    contentDescription = "Sign in with Google"
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.shape_copy),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Sign in with Google", color = Color.White)
                    }
                }
                Text(
                    text = "Sign up",
                    fontSize = 13.5.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    enabled: Boolean,
    enabledImageId: Int,
    disabledImageId: Int,
    contentDescription: String,
    content: @Composable () -> Unit
) {
    val imageId = if (enabled) enabledImageId else disabledImageId

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        content()
    }
}




@Composable
fun LoginInputField(
    value: String,
    onValueChange: (String) -> Unit,
    labelIcon: Int,
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
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (value.isEmpty()) {
                Image(
                    painter = painterResource(id = labelIcon),
                    contentDescription = "Label Icon",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .padding(start = 40.dp)
                )
            }
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = "Leading Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
            )
        }
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