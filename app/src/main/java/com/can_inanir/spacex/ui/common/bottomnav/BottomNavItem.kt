package com.can_inanir.spacex.ui.common.bottomnav


import com.can_inanir.spacex.R

sealed class BottomNavItem(val route: String, val title: String, val enabledIcon: Int, val disabledIcon: Int) {
    data object Rockets : BottomNavItem("rockets", "Rockets", R.drawable.rocket_e, R.drawable.rocket_d)
    data object Favorites : BottomNavItem("favorites", "Favorites", R.drawable.fav_e, R.drawable.fav_d)
    data object Upcoming : BottomNavItem("upcoming", "Upcoming", R.drawable.up_e, R.drawable.up_d)
    data object Login : BottomNavItem("login", "Login", R.drawable.ic_favorite, R.drawable.ic_favorite)
    data object Profile : BottomNavItem("profile", "Profile", R.drawable.ic_favorite, R.drawable.ic_favorite)
}

