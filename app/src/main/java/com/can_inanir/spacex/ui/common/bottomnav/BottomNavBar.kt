package com.can_inanir.spacex.ui.common.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
//import dev.chrisbanes.haze.HazeState
//import dev.chrisbanes.haze.HazeStyle
//import dev.chrisbanes.haze.hazeChild

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier/*, hazeState: HazeState*/) {
    val items = listOf(
        BottomNavItem.Rockets,
        BottomNavItem.Favorites,
        BottomNavItem.Upcoming,
    )

    var rocketClickCount by remember { mutableStateOf(0) }
    var lastClickTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(75.dp)
            .width(400.dp)
            .systemBarsPadding()
            .background(Color.Transparent)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(60.dp)
                .width(360.dp)
                .align(Alignment.BottomCenter),
                //.hazeChild(state = hazeState, shape = RoundedCornerShape(16.dp), HazeStyle(Color(0x33000000), 40.dp, 0f)),
            color = Color(0x80000000)
        ) {
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
                            val currentTime = System.currentTimeMillis()

                            if (item is BottomNavItem.Rockets) {
                                if (currentTime - lastClickTime < 1000) {
                                    rocketClickCount++
                                    if (rocketClickCount == 7) {
                                        navController.navigate("easter_egg")
                                    }
                                } else {
                                    rocketClickCount = 1
                                }
                                lastClickTime = currentTime
                            } else {
                                rocketClickCount = 0
                            }

                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Unspecified,
                            unselectedIconColor = Color.Unspecified,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}