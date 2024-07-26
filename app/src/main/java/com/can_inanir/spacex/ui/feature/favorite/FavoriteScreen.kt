package com.can_inanir.spacex.ui.feature.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.feature.login.AuthViewModel
import com.can_inanir.spacex.ui.feature.rockets.RocketCard
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun FavoritesScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val viewModel: FetchDataViewModel = viewModel()
    val userState by authViewModel.userState.collectAsState()
    val rockets by viewModel.rockets.collectAsState(initial = emptyList())
    val favorites by viewModel.favorites.collectAsState(initial = emptySet())
    val hazeState = remember { HazeState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bgl),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize().haze(state = hazeState),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.haze(state = hazeState),
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent, titleContentColor = Color.White),
                    title = { Text("Favorite Rockets") },
                    actions = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    if (userState != null) {
                                        navController.navigate("profile")
                                    } else {
                                        navController.navigate("login")
                                    }
                                },
                            tint = Color.Green

                        )
                    }
                )
            },
            content = { paddingValues ->
                if (userState != null) {
                    FavoriteRocketList(
                        rockets = rockets,
                        favorites = favorites,
                        paddingValues = paddingValues,
                        navController = navController,
                        viewModel = viewModel
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Log in to view favorites", color = Color.White)
                    }
                }
            },
            containerColor = Color.Transparent
        )

        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            hazeState = hazeState
        )
    }
}

@Composable fun FavoriteRocketList(
    rockets: List<Rocket>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: FetchDataViewModel
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