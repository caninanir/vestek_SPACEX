package com.can_inanir.spacex.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.can_inanir.spacex.dataclasses.Launch
import com.can_inanir.spacex.authandapi.RocketsViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingLaunchesScreen(navController: NavController) {
    val viewModel: RocketsViewModel = viewModel()
    val upcomingLaunches by viewModel.upcomingLaunches.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Upcoming Launches") })
        },
        content = { paddingValues ->
            LaunchList(upcomingLaunches, paddingValues, navController)
        }
    )
}

@Composable
fun LaunchList(
    launches: List<Launch>,
    paddingValues: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        launches.forEach { launch ->
            LaunchCard(launch, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LaunchCard(launch: Launch, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("launchDetail/${launch.id}") },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = launch.name, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${ZonedDateTime.parse(launch.date_utc).format(DateTimeFormatter.RFC_1123_DATE_TIME)}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rocket: ${launch.rocket}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Launchpad: ${launch.launchpad}")
            Spacer(modifier = Modifier.height(8.dp))
            launch.links.webcast?.let { webcastUrl ->
                val context = LocalContext.current
                Text(
                    text = "Watch the webcast",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}