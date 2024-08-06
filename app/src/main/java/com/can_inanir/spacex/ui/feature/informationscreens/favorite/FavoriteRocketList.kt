package com.can_inanir.spacex.ui.feature.informationscreens.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.ui.feature.informationscreens.rockets.RocketCard


@Composable
fun FavoriteRocketList(
    rockets: List<RocketEntity>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onRocketClick: (RocketEntity) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    val favoriteRockets = rockets.filter { favorites.contains(it.name) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        favoriteRockets.forEach { rocket ->
            RocketCard(
                rocket = rocket,
                isFavorite = true,
                onClick = { onRocketClick(rocket) },
                onFavoriteClick = { onFavoriteClick(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}