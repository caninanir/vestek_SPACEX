package com.can_inanir.spacex.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Card

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.dataclasses.Rocket
import com.can_inanir.spacex.authandapi.RocketsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsScreen(navController: NavController) {
    val viewModel: RocketsViewModel = viewModel()
    val rockets by viewModel.rockets.collectAsState(initial = emptyList())
    val favorites by viewModel.favorites.collectAsState(initial = emptySet())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SpaceX Rockets") })
        },
        content = { paddingValues ->
            RocketList(rockets, favorites, paddingValues, navController, viewModel)
        }
    )
}

@Composable
fun RocketList(
    rockets: List<Rocket>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: RocketsViewModel
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
                onClick = { navController.navigate("rocketDetail/${rocket.id}") },
                onFavoriteClick = { viewModel.toggleFavorite(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RocketCard(
    rocket: Rocket,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = rocket.name, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            rocket.flickr_images.firstOrNull()?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = rocket.description, maxLines = 3, modifier = Modifier.weight(1f))
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        painter = painterResource(
                            id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                        ),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

