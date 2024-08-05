package com.can_inanir.spacex.ui.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.ui.main.AppColors
@Composable
fun LoginScreen(navController: NavController, signInWithGoogle: () -> Unit) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val userState by authViewModel.userState.collectAsState()
    val loginErrorState by authViewModel.loginErrorState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage = loginErrorState

    LaunchedEffect(userState) {
        if (userState != null) {
            navController.navigate(BottomNavItem.Favorites.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.welcome_page_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Black.copy(alpha = 0.50f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // Add horizontal padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.space_x_white_logo_wine),
                contentDescription = stringResource(R.string.logo),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 52.dp, end = 23.dp)
            )
            Spacer(modifier = Modifier.height(23.dp))
            LoginInputField(
                value = email,
                onValueChange = { email = it },
                labelIcon = R.drawable.form_elements_text_content_active_white_email,
                leadingIcon = if (email.isEmpty()) {
                    R.drawable.form_elements_icons_email_enable_white
                } else {
                    R.drawable.form_elements_icons_email_active
                }
            )
            Spacer(modifier = Modifier.height(22.dp))
            LoginInputField(
                value = password,
                onValueChange = { password = it },
                labelIcon = R.drawable.form_elements_text_content_active_white,
                leadingIcon = if (password.isEmpty()) {
                    R.drawable.form_elements_icons_password_enable_white
                } else {
                    R.drawable.form_elements_icons_password_active
                },
                trailingIcon = {
                    PasswordVisibilityIcon(
                        passwordVisible,
                        onClick = { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) { VisualTransformation.None } else { PasswordVisualTransformation() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
                )
            }
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 14.sp,
                color = AppColors.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
                    .clickable { /* Add forgot password logic */ }
            )
            Spacer(modifier = Modifier.height(64.dp)) // Adjust spacing
            val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()
            CustomButton(
                onClick = {
                    authViewModel.login(email, password)
                },
                enabled = isLoginEnabled,
                enabledImageId = R.drawable.buttons_primary_enable,
                disabledImageId = R.drawable.buttons_primary_disable,
                contentDescription = stringResource(R.string.login)
            ) {
                Text(
                    text = stringResource(R.string.login), // Convert text to uppercase
                    color = AppColors.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp)) // Adjust spacing
            Text(
                text = stringResource(R.string.or),
                fontSize = 14.sp,
                color = AppColors.White,
                modifier = Modifier.padding(16.dp)
            )
            CustomButton(
                onClick = { signInWithGoogle() },
                enabled = true,
                enabledImageId = R.drawable.buttons_secondary_enable,
                disabledImageId = R.drawable.buttons_secondary_enable,
                contentDescription = stringResource(R.string.sign_in_with_google)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.shape_copy),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.sign_in_with_google), // Convert text to uppercase
                        color = AppColors.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(94.dp)) // Adjust spacing
            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 13.5.sp,
                color = AppColors.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { authViewModel.createAccount(email, password) }
            )
        }
        BottomNavBar(navController = navController, modifier = Modifier.fillMaxSize())
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
            .height(48.dp)
            .padding(horizontal = 75.dp)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
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
        modifier = Modifier.padding(horizontal = 16.dp),
        color = AppColors.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(10.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (value.isEmpty()) {
                Image(
                    painter = painterResource(id = labelIcon),
                    contentDescription = stringResource(R.string.label_icon),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 55.dp, top = 11.dp, bottom = 9.dp)
                )
            }
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = stringResource(R.string.leading_icon),
                        modifier = Modifier.size(50.dp).padding(all = 12.dp)
                    )
                },
                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                textStyle = LocalTextStyle.current.copy(color = AppColors.White),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.CoolGreen,
                    unfocusedBorderColor = AppColors.FullTransparentBackground,
                    cursorColor = AppColors.CoolGreen,
                ),
            )
        }
    }
}

@Composable
fun PasswordVisibilityIcon(passwordVisible: Boolean, onClick: () -> Unit) {
    val icon = if (passwordVisible) {
        R.drawable.form_elements_icons_hide_password_active
    } else {
        R.drawable.form_elements_icons_hide_password_enable_white
    }
    Image(
        painter = painterResource(id = icon),
        contentDescription = stringResource(R.string.toggle_password_visibility),
        modifier = Modifier
            .size(50.dp).padding(all = 12.dp)
            .clickable { onClick() }
    )
}