package com.example.a3rdtimesthecharm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.compose.ui.platform.LocalContext

import android.content.Intent
import android.net.Uri
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceXRocketsApp()
        }
    }
}

@Composable
fun SpaceXRocketsApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "rocketList") {
        composable("rocketList") {
            RocketsListScreen(navController)
        }
        composable(
            "rocketDetail/{rocketId}",
            arguments = listOf(navArgument("rocketId") { type = NavType.StringType })
        ) { backStackEntry ->
            RocketDetailScreen(backStackEntry.arguments?.getString("rocketId"))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsListScreen(navController: NavController) {
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
                navController.navigate("rocketDetail/${rocket.id}")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketDetailScreen(rocketId: String?) {
    val viewModel: RocketsViewModel = viewModel()

    LaunchedEffect(rocketId) {
        if (rocketId != null) {
            viewModel.fetchRocketById(rocketId)
        }
    }

    val rocket by viewModel.selectedRocket.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(rocket?.name ?: "Rocket Details") })
        },
        content = { paddingValues ->
            if (rocket != null) {
                RocketDetail(rocket!!, paddingValues)
            } else {
                Text("Loading...", modifier = Modifier.padding(paddingValues))
            }
        }
    )
}

@Composable
fun RocketDetail(rocket: Rocket, paddingValues: PaddingValues) {
    val context = LocalContext.current // Correctly obtain the context

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
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
        Text(text = rocket.name, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "First flight: ${rocket.first_flight}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Cost per launch: \$${rocket.cost_per_launch}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Success rate: ${rocket.success_rate_pct}%")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = rocket.description)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia))
            context.startActivity(intent)
        }) {
            Text("Learn More")
        }
    }
}