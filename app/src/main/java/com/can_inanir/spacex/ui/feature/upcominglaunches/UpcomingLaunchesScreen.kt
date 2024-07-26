package com.can_inanir.spacex.ui.feature.upcominglaunches

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.model.Launch
import com.can_inanir.spacex.data.model.Launchpad
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Composable
fun UpcomingLaunchesScreen(navController: NavController) {
    val viewModel: FetchDataViewModel = viewModel()
    val upcomingLaunches by viewModel.upcomingLaunches.collectAsState(initial = emptyList())
    val hazeState = remember { HazeState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 0.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bgl),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().haze(state = hazeState),
            contentScale = ContentScale.Crop
        )


        Scaffold(
            modifier = Modifier.haze(state = hazeState),
            content = { paddingValues ->
                LaunchList(upcomingLaunches, paddingValues, navController, viewModel)
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
fun LaunchList(
    launches: List<Launch>,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: FetchDataViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        launches.forEach { launch ->
            LaunchCard(launch, navController, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LaunchCard(
    launch: Launch,
    navController: NavController,
    viewModel: FetchDataViewModel
) {
    var rocket by remember { mutableStateOf<Rocket?>(null) }
    var launchpad by remember { mutableStateOf<Launchpad?>(null) }

    LaunchedEffect(launch) {
        viewModel.fetchRocketById(launch.rocket) { rocket = it }
        viewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("launchDetail/${launch.id}") },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                color = Color.White,
                text = launch.name,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                color = Color.White,
                text = "Date: ${ZonedDateTime.parse(launch.date_utc).format(DateTimeFormatter.RFC_1123_DATE_TIME)}"
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (rocket != null && launchpad != null) {
                Text(color = Color.White, text = "Rocket: ${rocket!!.name}")
                Image(
                    painter = rememberAsyncImagePainter(model = rocket!!.flickr_images.firstOrNull()),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(color = Color.White, text = "Launchpad: ${launchpad!!.name}")
                Image(
                    painter = rememberAsyncImagePainter(model = launchpad!!.images.large.firstOrNull()),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text("Loading...", color = Color.White)
            }

            launch.links.webcast?.let { webcastUrl ->
                val context = LocalContext.current
                Text(
                    text = "Watch the webcast",
                    color = Color(color = 0xFF58FBC8),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}