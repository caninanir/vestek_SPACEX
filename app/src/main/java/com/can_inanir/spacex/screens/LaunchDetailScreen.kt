package com.can_inanir.spacex.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.can_inanir.spacex.dataclasses.Launch
import com.can_inanir.spacex.authandapi.RocketsViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailScreen(launchId: String?) {
    val viewModel: RocketsViewModel = viewModel()
    val launches by viewModel.upcomingLaunches.collectAsState()
    val launch = remember(launchId, launches) { launches.find { it.id == launchId } }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(launch?.name ?: "Launch Details") })
        },
        content = { paddingValues ->
            if (launch != null) {
                LaunchDetail(launch = launch, paddingValues = paddingValues)
            } else {
                Text("Loading...", modifier = Modifier.padding(paddingValues))
            }
        }
    )
}

@Composable
fun LaunchDetail(launch: Launch, paddingValues: PaddingValues) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = launch.name, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: ${ZonedDateTime.parse(launch.date_utc).format(DateTimeFormatter.RFC_1123_DATE_TIME)}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Rocket: ${launch.rocket}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Launchpad: ${launch.launchpad}")
        Spacer(modifier = Modifier.height(8.dp))
        if (launch.details != null) {
            Text(text = launch.details)
            Spacer(modifier = Modifier.height(8.dp))
        }
        launch.links.webcast?.let { webcastUrl ->
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