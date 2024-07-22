package com.can_inanir.spacex

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsScreen(navController: NavController) {
    val viewModel: RocketsViewModel = viewModel()
    val rockets by viewModel.rockets.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SpaceX Rockets") })
        },
        content = { paddingValues ->
            RocketList(rockets, paddingValues, navController)
        }
    )
}

@Composable
fun RocketList(rockets: List<Rocket>, paddingValues: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        rockets.forEach { rocket ->
            RocketCard(rocket) {
                navController.navigate("${BottomNavItem.Rockets.route}/${rocket.id}")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RocketCard(rocket: Rocket, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = rocket.name, style = MaterialTheme.typography.h6)
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
            Text(text = rocket.description, maxLines = 3)
        }
    }
}