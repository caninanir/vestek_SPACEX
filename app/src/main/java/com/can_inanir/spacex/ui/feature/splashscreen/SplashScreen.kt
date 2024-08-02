package com.can_inanir.spacex.ui.feature.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.can_inanir.spacex.R

@Composable
fun SplashScreen() {
    Image(
        painter = painterResource(id = R.drawable.space_x_android_splash_screen_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}