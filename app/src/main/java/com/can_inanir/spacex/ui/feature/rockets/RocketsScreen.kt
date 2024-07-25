package com.can_inanir.spacex.ui.feature.rockets

import androidx.compose.animation.core.copy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.remote.FetchDataViewModel


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar


@Composable
fun RocketsScreen(navController: NavController) {
    val viewModel: FetchDataViewModel = viewModel()
    val rockets by viewModel.rockets.collectAsState(initial = emptyList())
    val favorites by viewModel.favorites.collectAsState(initial = emptySet())



    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bgl),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Scaffold(
            content = { paddingValues ->
                RocketList(
                    rockets,
                    favorites,
                    paddingValues,
                    navController,
                    viewModel
                )
            },
            containerColor = Color.Transparent
        )

        BottomNavBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }



}


@Composable
fun RocketList(
    rockets: List<Rocket>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: FetchDataViewModel
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
            .clickable(onClick = onClick)
            .padding(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = rocket.name, style = MaterialTheme.typography.headlineLarge, color = Color.White)
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
                Text(text = rocket.description, maxLines = 3, modifier = Modifier.weight(1f), color = Color.White)
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

