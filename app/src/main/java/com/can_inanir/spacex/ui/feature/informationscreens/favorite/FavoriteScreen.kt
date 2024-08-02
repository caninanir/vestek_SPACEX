package com.can_inanir.spacex.ui.feature.informationscreens.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.FavoritesViewModel
import com.can_inanir.spacex.data.remote.RocketListViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.ui.feature.informationscreens.RocketCard
import com.can_inanir.spacex.ui.feature.informationscreens.RocketDetail
import com.can_inanir.spacex.ui.feature.login.AuthViewModel
import com.can_inanir.spacex.ui.main.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val rocketListViewModel: RocketListViewModel = hiltViewModel()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val userState by authViewModel.userState.collectAsState()
    val rockets by rocketListViewModel.rockets.collectAsState(initial = emptyList())
    val favorites by favoritesViewModel.favorites.collectAsState(initial = emptySet())
    var selectedRocket by remember { mutableStateOf<RocketEntity?>(null) }
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
                            text = stringResource(R.string.favorite_rockets),
                            style = MaterialTheme.typography.headlineLarge,
                            color = AppColors.White,
                            fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
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

@Composable
fun FavoriteRocketList(
    rockets: List<RocketEntity>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onRocketClick: (RocketEntity) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    val favoriteRockets = rockets.filter { favorites.contains(it.name) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        favoriteRockets.forEach { rocket ->
            RocketCard(
                rocket = rocket,
                isFavorite = true,
                onClick = { onRocketClick(rocket) },
                onFavoriteClick = { onFavoriteClick(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileOverlay(
    authViewModel: AuthViewModel,
    navController: NavController,
    onClose: () -> Unit,
) {
    val userState by authViewModel.userState.collectAsState()

    Box(
        modifier = Modifier
            .size(width = 250.dp, height = 150.dp)
            .background(AppColors.HalfGrayTransparentBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.logged_in_as),
                color = AppColors.CoolGreen
            )
            userState?.let { user ->
                Text(text = "${user.email}", color = AppColors.CoolGreen)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = AppColors.White
                    ),
                    onClick = {
                        authViewModel.logout()
                        navController.navigate(BottomNavItem.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                        onClose()
                    }
                ) {
                    Text(text = stringResource(R.string.logout))
                }
            }
        }
    }
}
