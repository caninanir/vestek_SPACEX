package com.caninanir.spacex.presentation.common.bottomnav

import com.caninanir.spacex.R

sealed class BottomNavItem(val route: String, val title: String, val enabledIcon: Int, val disabledIcon: Int) {
    data object Rockets : BottomNavItem(
        "rockets",
        "Rockets",
        R.drawable.bottom_nav_rocket_green,
        R.drawable.bottom_nav_rocket_gray
    )
    data object Favorites : BottomNavItem(
        "favorites",
        "Favorites",
        R.drawable.bottom_nav_favorite_green,
        R.drawable.bottom_nav_favorite_gray
    )
    data object Upcoming : BottomNavItem(
        "upcoming",
        "Upcoming",
        R.drawable.bottom_nav_upcoming_green,
        R.drawable.bottom_nav_upcoming_gray
    )
    data object Login : BottomNavItem(
        "login",
        "Login",
        R.drawable.buttons_icons_favorites_enable,
        R.drawable.buttons_icons_favorites_enable
    )
    data object Profile : BottomNavItem(
        "profile",
        "Profile",
        R.drawable.buttons_icons_favorites_enable,
        R.drawable.buttons_icons_favorites_enable
    )
}
