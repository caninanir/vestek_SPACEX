package com.can_inanir.spacex.presentation.ui.features.informationscreens.rockets

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
import com.can_inanir.spacex.domain.model.Rocket


@Composable
fun RocketList(
    rockets: List<Rocket>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onRocketClick: (Rocket) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    val sortedRockets = rockets.sortedByDescending { favorites.contains(it.name) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        sortedRockets.forEach { rocket ->
            RocketCard(
                rocket = rocket,
                isFavorite = favorites.contains(rocket.name),
                onClick = { onRocketClick(rocket) },
                onFavoriteClick = { onFavoriteClick(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}