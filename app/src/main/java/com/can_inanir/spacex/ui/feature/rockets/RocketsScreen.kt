package com.can_inanir.spacex.ui.feature.rockets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RocketsScreen(navController: NavController) {
    val viewModel: FetchDataViewModel = viewModel()
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
            modifier = Modifier
                .fillMaxSize()
                .haze(state = hazeState),
            contentScale = ContentScale.Crop
        )


        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .haze(state = hazeState),
            content = { paddingValues ->
                RocketList(
                    rockets = rockets,
                    favorites = favorites,
                    paddingValues = paddingValues,
                    navController = navController,
                    viewModel = viewModel
                )
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
            .padding(bottom = 0.dp, top = 0.dp)
            .padding(horizontal = 10.dp)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Text(
                text = rocket.name,
                style = MaterialTheme.typography.headlineLarge ,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
            )
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
                Text(
                    text = rocket.description,
                    maxLines = 3,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
                )
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        tint = Color.Unspecified,
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