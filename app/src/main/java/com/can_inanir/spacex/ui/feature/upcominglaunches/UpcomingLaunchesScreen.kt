package com.can_inanir.spacex.ui.feature.upcominglaunches

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingLaunchesScreen(navController: NavController) {

    val fetchDataViewModel: FetchDataViewModel = hiltViewModel()
    val launches by fetchDataViewModel.upcomingLaunches.collectAsState(initial = emptyList())
    val upcomingLaunches by fetchDataViewModel.upcomingLaunches.collectAsState(initial = emptyList())
    val hazeState = remember { HazeState() }
    val hazeState2 = remember { HazeState() }
    var selectedLaunch by remember { mutableStateOf<LaunchEntity?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bgl),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .haze(state = hazeState)
                .haze(state = hazeState2),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .haze(state = hazeState)
                .haze(state = hazeState2),
            topBar = {
                TopAppBar(
                    modifier = Modifier.haze(state = hazeState),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    ),
                    title = {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Upcoming Launches",
                                style = MaterialTheme.typography.headlineLarge,
                                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                                color = Color.White
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
                    viewModel = fetchDataViewModel
                )
            },
            containerColor = Color.Transparent
        )
        if (selectedLaunch != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x1AFFFFFF))
                    .haze(state = hazeState2)
            ) {
                LaunchDetail(
                    launch = selectedLaunch!!,
                    onClose = { selectedLaunch = null },
                    hazeState = hazeState,
                    viewModel = fetchDataViewModel
                )
            }
        }
        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            hazeState = hazeState2
        )
    }
}

@Composable
fun LaunchList(
    launches: List<LaunchEntity>,
    paddingValues: PaddingValues,
    onLaunchClick: (LaunchEntity) -> Unit,
    viewModel: FetchDataViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {

        launches.forEach { launch ->
            LaunchCard(launch, onLaunchClick, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }


    }
}

@Suppress("name_shadowing")
@Composable
fun LaunchCard(
    launch: LaunchEntity,
    onLaunchClick: (LaunchEntity) -> Unit,
    viewModel: FetchDataViewModel
) {


    var rocket by remember { mutableStateOf<RocketEntity?>(null) }
    var launchpad by remember { mutableStateOf<LaunchpadEntity?>(null) }

    LaunchedEffect(launch) {
        viewModel.fetchRocketById(launch.rocket) { rocket = it }
        viewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
    }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onLaunchClick(launch) },

        colors = CardDefaults.cardColors(containerColor = Color(0x347C7C7C)),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = Color.White,
                text = launch.name,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold))
            )
            Text(
                color = Color.White,
                text = "Date: ${ZonedDateTime.parse(launch.date_utc).format(DateTimeFormatter.RFC_1123_DATE_TIME)}",
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold)),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
            )

                        if (rocket != null && launchpad != null) {
                            Text(
                                color = Color.White,
                                text = "Rocket: ${rocket!!.name}",
                                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold)),
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                            )

                            val imageUrl = launch.patches?.large ?: rocket!!.flickr_images.randomOrNull()
                            imageUrl?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(model = it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }


                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    color = Color.White,
                    text = "Launchpad: ${launchpad!!.name}",
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold)),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                )
                Spacer(modifier = Modifier.height(2.dp))

                launchpad!!.images.large.randomOrNull()?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }


                Spacer(modifier = Modifier.height(2.dp))
            } else {
                Text("Loading...", color = Color.White)
            }
            launch.links.webcast?.let { webcastUrl ->
                val context = LocalContext.current
                Text(
                    text = "Watch the webcast",
                    color = Color(color = 0xFF58FBC8),
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun LaunchDetail(
    launch: LaunchEntity,
    onClose: () -> Unit,
    hazeState: HazeState,
    viewModel: FetchDataViewModel
) {
    val context = LocalContext.current
    var rocket by remember { mutableStateOf<RocketEntity?>(null) }
    var launchpad by remember { mutableStateOf<LaunchpadEntity?>(null) }

    LaunchedEffect(launch) {
        viewModel.fetchRocketById(launch.rocket) { rocket = it }
        viewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .hazeChild(
                state = hazeState,
                shape = RoundedCornerShape(1.dp),
                HazeStyle(Color(0x80000000), 20.dp, 0f)
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = launch.name,
                style = MaterialTheme.typography.headlineLarge,
                color = colorResource(id = R.color.cool_green),
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                modifier = Modifier.align(Alignment.CenterVertically),
                softWrap = false
            )
            Spacer(modifier = Modifier.weight(1f))
        }


        Text(
            text = launch.details ?: "No details available.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))


        DetailItem(label = "DATE", value = ZonedDateTime.parse(launch.date_utc).format(DateTimeFormatter.RFC_1123_DATE_TIME))
        HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            launch.links.wikipedia?.let { wikipediaUrl ->
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cool_green), contentColor = Color.White),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
                        context.startActivity(intent)
                    }
                ) {
                    Text("Learn More")
                }
            }
            launch.links.webcast?.let { webcastUrl ->
                val appContext = LocalContext.current
                Text(
                    text = "Watch the webcast",
                    color = colorResource(id = R.color.cool_green),
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                        appContext.startActivity(intent)
                    }
                )
            }
            launch.links.article?.let { articleUrl ->
                val appContext = LocalContext.current
                Text(
                    text = "Read Article",
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                    color = colorResource(id = R.color.cool_green),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                        appContext.startActivity(intent)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            rocket?.flickr_images?.forEach { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Rocket Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .heightIn(max = 300.dp),
                    contentScale = ContentScale.Crop
                )
            }
            launchpad?.images?.large?.forEach { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Launchpad Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .heightIn(max = 300.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}