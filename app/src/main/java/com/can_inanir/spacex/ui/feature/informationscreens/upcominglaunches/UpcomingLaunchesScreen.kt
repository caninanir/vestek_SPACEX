package com.can_inanir.spacex.ui.feature.informationscreens.upcominglaunches

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.remote.LaunchpadDetailViewModel
import com.can_inanir.spacex.data.remote.RocketDetailViewModel
import com.can_inanir.spacex.data.remote.UpcomingLaunchesViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.feature.informationscreens.LaunchCard
import com.can_inanir.spacex.ui.feature.informationscreens.LaunchDetail
import com.can_inanir.spacex.ui.main.AppColors
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.unit.sp
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingLaunchesScreen(navController: NavController) {
    val upcomingLaunchesViewModel: UpcomingLaunchesViewModel = hiltViewModel()
    val rocketDetailViewModel: RocketDetailViewModel = hiltViewModel()
    val launchpadDetailViewModel: LaunchpadDetailViewModel = hiltViewModel()
    val upcomingLaunches by upcomingLaunchesViewModel.upcomingLaunches.collectAsState(initial = emptyList())
    var selectedLaunch by remember { mutableStateOf<LaunchEntity?>(null) }

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
                            modifier = Modifier.padding(start = 78.dp, end = 78.dp, bottom = 36.dp),
                            text = stringResource(R.string.upcoming_launches),
                            style = MaterialTheme.typography.headlineLarge,
                            color = AppColors.White,
                            overflow = TextOverflow.Visible,
                            softWrap = false,
                        )
                    },
                    actions = {}
                )
            },
            content = { paddingValues ->
                LaunchList(
                    launches = upcomingLaunches,
                    paddingValues = paddingValues,
                    onLaunchClick = { launch -> selectedLaunch = launch },
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
                    launch = selectedLaunch!!,
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

@Composable
fun LaunchList(
    launches: List<LaunchEntity>,
    paddingValues: PaddingValues,
    onLaunchClick: (LaunchEntity) -> Unit,
    rocketDetailViewModel: RocketDetailViewModel,
    launchpadDetailViewModel: LaunchpadDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        launches.take(3).forEachIndexed { index, launch ->
            var rocket by remember { mutableStateOf<RocketEntity?>(null) }
            var launchpad by remember { mutableStateOf<LaunchpadEntity?>(null) }

            LaunchedEffect(launch) {
                rocketDetailViewModel.fetchRocketById(launch.rocket) { rocket = it }
                launchpadDetailViewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(
                        id = when (index) {
                            0 -> R.drawable.image_1
                            1 -> R.drawable.image_2
                            else -> R.drawable.image_3
                        }
                    ),
                    contentDescription = stringResource(R.string.background),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .align(
                            when (index) {
                                0, 2 -> Alignment.TopEnd
                                else -> Alignment.TopStart
                            }
                        )
                        .padding(top = 75.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .background(AppColors.CardBackground, RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Text(launch.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                    if (rocket != null && launchpad != null) {
                        Text("Rocket: ${rocket!!.name}", color = Color.White, fontSize = 16.sp)
                        Text("Launchpad: ${launchpad!!.name}", color = Color.White, fontSize = 16.sp)
                    }
                    Button(
                        onClick = { onLaunchClick(launch) },
                        modifier = Modifier
                            .size(120.dp, 50.dp)
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.NavBackground
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(stringResource(R.string.explore), color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        launches.drop(3).forEach { launch ->
            LaunchCard(
                launch = launch,
                onLaunchClick = onLaunchClick,
                rocketDetailViewModel = rocketDetailViewModel,
                launchpadDetailViewModel = launchpadDetailViewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

