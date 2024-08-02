package com.can_inanir.spacex.ui.feature.informationscreens.upcominglaunches

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.LaunchpadDetailViewModel
import com.can_inanir.spacex.data.remote.RocketDetailViewModel
import com.can_inanir.spacex.data.remote.UpcomingLaunchesViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.feature.informationscreens.LaunchCard
import com.can_inanir.spacex.ui.feature.informationscreens.LaunchDetail
import com.can_inanir.spacex.ui.main.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone@OptIn(ExperimentalMaterial3Api::class)
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
            painter = painterResource(id = R.drawable.space_x_android_bgl),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColors.FullTransparentBackground,
                        titleContentColor = AppColors.White
                    ),
                    title = {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.upcoming_launches),
                                style = MaterialTheme.typography.headlineLarge,
                                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                                color = AppColors.White
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
        launches.forEach { launch ->
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