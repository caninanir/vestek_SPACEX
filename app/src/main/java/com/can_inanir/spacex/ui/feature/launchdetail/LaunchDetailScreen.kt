package com.can_inanir.spacex.ui.feature.launchdetail


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.data.model.Launch
import com.can_inanir.spacex.data.model.Launchpad
import com.can_inanir.spacex.data.model.Rocket
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailScreen(launchId: String?) {
    val viewModel: FetchDataViewModel = viewModel()
    val launches by viewModel.upcomingLaunches.collectAsState()
    val launch = remember(launchId, launches) { launches.find { it.id == launchId } }
    var rocket by remember { mutableStateOf<Rocket?>(null) }
    var launchpad by remember { mutableStateOf<Launchpad?>(null) }

    LaunchedEffect(launch) {
        launch?.rocket?.let { rocketId -> viewModel.fetchRocketById(rocketId) { rocket = it } }
        launch?.launchpad?.let { launchpadId -> viewModel.fetchLaunchpadById(launchpadId) { launchpad = it } }
    }

    Scaffold(
        content = { paddingValues ->
            if (launch != null && rocket != null && launchpad != null) {
                LaunchDetail(
                    launch = launch,
                    rocket = rocket!!,
                    launchpad = launchpad!!,
                    paddingValues = paddingValues
                )
            } else {
                Text("Loading...", modifier = Modifier.padding(paddingValues))
            }
        }
    )
}

@Composable
fun LaunchDetail(launch: Launch, rocket: Rocket, launchpad: Launchpad, paddingValues: PaddingValues) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = launch.name, style = MaterialTheme.typography.headlineLarge, fontFamily = FontFamily(
            Font(R.font.nasalization, FontWeight.Normal)
        )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: ${ZonedDateTime.parse(launch.date_utc).format(DateTimeFormatter.RFC_1123_DATE_TIME)}", fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Rocket: ${rocket.name}", fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)))
        Image(
            painter = rememberAsyncImagePainter(model = rocket.flickr_images.firstOrNull()),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Launchpad: ${launchpad.name}", fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)))
        Image(
            painter = rememberAsyncImagePainter(model = launchpad.images.large.firstOrNull()),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (launch.details != null) {
            Text(text = launch.details, fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)))
            Spacer(modifier = Modifier.height(8.dp))
        }
        launch.links.webcast?.let { webcastUrl ->
            Text(
                text = "Watch the webcast",
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                    context.startActivity(intent)
                }
            )
        }
    }
}