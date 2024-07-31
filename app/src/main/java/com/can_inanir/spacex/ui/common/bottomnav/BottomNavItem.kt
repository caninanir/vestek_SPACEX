package com.can_inanir.spacex.ui.common.bottomnav

import com.can_inanir.spacex.R


sealed class BottomNavItem(val route: String, val title: String, val enabledIcon: Int, val disabledIcon: Int) {
    data object Rockets : BottomNavItem("rockets", "Rockets", R.drawable.bars_tab_bar_elements_items_active_pressed, R.drawable.rocket_d)
    data object Favorites : BottomNavItem("favorites", "Favorites", R.drawable.fav_e, R.drawable.bars_tab_bar_elements_items_enabled)
    data object Upcoming : BottomNavItem("upcoming", "Upcoming", R.drawable.up_e, R.drawable.up_d)
    data object Login : BottomNavItem("login", "Login", R.drawable.ic_favorite, R.drawable.ic_favorite)
    data object Profile : BottomNavItem("profile", "Profile", R.drawable.ic_favorite, R.drawable.ic_favorite)
}

