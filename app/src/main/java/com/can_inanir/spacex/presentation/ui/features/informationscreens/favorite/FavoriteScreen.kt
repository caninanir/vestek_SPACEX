package com.can_inanir.spacex.presentation.ui.features.informationscreens.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.can_inanir.spacex.presentation.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.presentation.ui.features.informationscreens.rockets.RocketDetail
import com.can_inanir.spacex.presentation.utils.AppColors
import com.can_inanir.spacex.presentation.viewmodel.AuthViewModel
import com.can_inanir.spacex.presentation.viewmodel.FavoritesViewModel
import com.can_inanir.spacex.presentation.viewmodel.RocketListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val rocketListViewModel: RocketListViewModel = hiltViewModel()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val userState by authViewModel.userState.collectAsState()
    val rockets by rocketListViewModel.rockets.collectAsState(initial = emptyList())
    val favorites by favoritesViewModel.favorites.collectAsState(initial = emptySet())
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }
    var showProfile by remember { mutableStateOf(false) }

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
                            text = stringResource(R.string.favorite_rockets),
                            style = MaterialTheme.typography.headlineLarge,
                            color = AppColors.White,
                            overflow = TextOverflow.Visible,
                            softWrap = false,
                        )
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.profile),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { showProfile = true },
                            tint = AppColors.CoolGreen
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
                        onRocketClick = { rocket -> selectedRocket = rocket },
                        onFavoriteClick = { rocketName -> favoritesViewModel.toggleFavorite(rocketName) }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.log_in_to_view_favorites),
                            color = AppColors.White
                        )
                    }
                }
            },
            containerColor = AppColors.FullTransparentBackground
        )
        if (selectedRocket != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.CardBackground)
            ) {
                RocketDetail(
                    rocket = selectedRocket!!,
                    isFavorite = favorites.contains(selectedRocket!!.name),
                    onClose = { selectedRocket = null },
                    onFavoriteClick = { rocketName -> favoritesViewModel.toggleFavorite(rocketName) }
                )
            }
        }
        if (showProfile) {
            if (userState == null) {
                navController.navigate(BottomNavItem.Login.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.FullTransparentBackground)
                    .clickable { showProfile = false },
                contentAlignment = Alignment.Center
            ) {
                ProfileOverlay(
                    authViewModel = authViewModel,
                    navController = navController,
                    onClose = { showProfile = false }
                )
            }
        }
        BottomNavBar(navController = navController, modifier = Modifier.fillMaxSize())
    }
}

