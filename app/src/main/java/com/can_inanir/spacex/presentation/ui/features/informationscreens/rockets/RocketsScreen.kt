package com.can_inanir.spacex.presentation.ui.features.informationscreens.rockets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.presentation.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.presentation.viewmodel.FavoritesViewModel
import com.can_inanir.spacex.presentation.viewmodel.RocketListViewModel

import com.can_inanir.spacex.presentation.utils.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsScreen(navController: NavController) {
    val rocketListViewModel: RocketListViewModel = hiltViewModel()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val rockets by rocketListViewModel.rockets.collectAsState(initial = emptyList())
    val favorites by favoritesViewModel.favorites.collectAsState(initial = emptySet())
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }
    BackHandler(enabled = selectedRocket != null) {
        selectedRocket = null
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bg),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColors.FullTransparentBackground,
                        titleContentColor = AppColors.White
                    ),
                    title = {
                        Text(
                            modifier = Modifier.padding(start = 78.dp,end = 78.dp, bottom = 36.dp),
                            text = stringResource(R.string.spacex_rockets),
                            style = MaterialTheme.typography.headlineLarge,
                            color = AppColors.White,
                            overflow = TextOverflow.Visible,
                            softWrap = false,
                        )
                    }
                )
            },
            content = { paddingValues ->
                RocketList(
                    rockets = rockets,
                    favorites = favorites,
                    paddingValues = paddingValues,
                    onRocketClick = { rocket -> selectedRocket = rocket },
                    onFavoriteClick = { rocketName -> favoritesViewModel.toggleFavorite(rocketName) }
                )
            },
            containerColor = AppColors.FullTransparentBackground
        )
        if (selectedRocket != null) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                RocketDetail(
                    rocket = selectedRocket!!,
                    isFavorite = favorites.contains(selectedRocket!!.name),
                    onClose = { selectedRocket = null },
                    onFavoriteClick = { rocketName -> favoritesViewModel.toggleFavorite(rocketName) }
                )
            }
        }
        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
    }
}

