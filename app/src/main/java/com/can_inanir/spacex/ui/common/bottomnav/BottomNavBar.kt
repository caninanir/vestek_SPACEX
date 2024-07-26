package com.can_inanir.spacex.ui.common.bottomnav

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.can_inanir.spacex.R

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier, activity: ComponentActivity) {
    val items = listOf(
        BottomNavItem.Rockets,
        BottomNavItem.Favorites,
        BottomNavItem.Upcoming,
    )


    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Transparent)
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(75.dp)
                .width(400.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0x805D5D5D)),


            color = Color.Transparent
        ) {}
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .height(75.dp)
                .width(400.dp)
                .align(Alignment.BottomCenter)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                NavigationBarItem(
                    modifier = Modifier.padding(bottom = 0.dp),
                    icon = {
                        Icon(
                            tint = Color.Unspecified,
                            painter = painterResource(
                                id = if (currentRoute == item.route) {
                                    item.enabledIcon
                                } else {
                                    item.disabledIcon
                                }
                            ),
                            contentDescription = item.title,

                            modifier = Modifier.size(50.dp)

                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    alwaysShowLabel = false  // Remove label
                )
            }
        }
    }
}