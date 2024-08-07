package com.caninanir.spacex.presentation.ui.features.informationscreens.upcominglaunches

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.caninanir.spacex.R
import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.presentation.common.bottomnav.BottomNavBar
import com.caninanir.spacex.presentation.utils.AppColors
import com.caninanir.spacex.presentation.viewmodel.LaunchpadDetailViewModel
import com.caninanir.spacex.presentation.viewmodel.RocketDetailViewModel
import com.caninanir.spacex.presentation.viewmodel.UpcomingLaunchesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingLaunchesScreen(navController: NavController) {
    val upcomingLaunchesViewModel: UpcomingLaunchesViewModel = hiltViewModel()
    val rocketDetailViewModel: RocketDetailViewModel = hiltViewModel()
    val launchpadDetailViewModel: LaunchpadDetailViewModel = hiltViewModel()
    val upcomingLaunches by upcomingLaunchesViewModel.upcomingLaunches.collectAsState(initial = emptyList())
    var selectedLaunch by remember { mutableStateOf<Pair<Launch, Int>?>(null) }

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

        // Scaffold wrapper with aligned top bar title
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColors.FullTransparentBackground,
                        titleContentColor = AppColors.White
                    ),
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.upcoming_launches),
                                style = MaterialTheme.typography.headlineLarge,
                                color = AppColors.White,
                                overflow = TextOverflow.Visible,
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    actions = {}
                )
            },
            content = { paddingValues ->
                LaunchList(
                    launches = upcomingLaunches,
                    paddingValues = paddingValues,
                    onLaunchClick = { launch, index -> selectedLaunch = Pair(launch, index) },
                    rocketDetailViewModel = rocketDetailViewModel,
                    launchpadDetailViewModel = launchpadDetailViewModel
                )
            },
            containerColor = AppColors.FullTransparentBackground
        )

        if (selectedLaunch != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.CardBackground)
            ) {
                LaunchDetail(
                    launch = selectedLaunch!!.first,
                    launchIndex = selectedLaunch!!.second,
                    onClose = { selectedLaunch = null },
                    rocketDetailViewModel = rocketDetailViewModel,
                    launchpadDetailViewModel = launchpadDetailViewModel
                )
            }
        }

        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
    }
}
