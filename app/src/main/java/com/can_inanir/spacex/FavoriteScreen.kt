package com.can_inanir.spacex


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val viewModel: RocketsViewModel = viewModel()
    val rockets by viewModel.rockets.collectAsState(initial = emptyList())
    val favorites by viewModel.favorites.collectAsState(initial = emptySet())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favorite Rockets") })
        },
        content = { paddingValues ->
            FavoriteRocketList(rockets, favorites, paddingValues, navController, viewModel)
        }
    )
}

@Composable
fun FavoriteRocketList(
    rockets: List<Rocket>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: RocketsViewModel
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
                onClick = { navController.navigate("rocketDetail/${rocket.id}") },
                onFavoriteClick = { viewModel.toggleFavorite(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}