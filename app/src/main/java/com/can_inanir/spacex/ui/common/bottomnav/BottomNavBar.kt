package com.can_inanir.spacex.ui.common.bottomnav

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild


@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier, hazeState: HazeState) {
    val items = listOf(
        BottomNavItem.Rockets,
        BottomNavItem.Favorites,
        BottomNavItem.Upcoming,
    )






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
                .align(Alignment.BottomCenter)
                .hazeChild(state = hazeState, shape = RoundedCornerShape(16.dp), HazeStyle(Color(
                    0x26FFFFFF
                ), 20.dp,0f)),

            color = Color.Transparent
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



}

