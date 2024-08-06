package com.can_inanir.spacex.ui.feature.informationscreens.upcominglaunches

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.LaunchpadDetailViewModel
import com.can_inanir.spacex.data.remote.RocketDetailViewModel
import com.can_inanir.spacex.ui.main.AppColors

@Composable
fun LaunchList(
    launches: List<LaunchEntity>,
    paddingValues: PaddingValues,
    onLaunchClick: (LaunchEntity, Int) -> Unit,
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
                        id = when (index % 3) {
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
                            when (index % 3) {
                                0 -> Alignment.TopCenter
                                1 -> Alignment.TopStart
                                else -> Alignment.TopEnd
                            }
                        )
                        .padding(top = 200.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .padding(8.dp)
                ) {
                    Text(launch.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                    if (rocket != null && launchpad != null) {
                        Text("Rocket: ${rocket!!.name}", color = Color.White, fontSize = 16.sp)
                        Text("Launchpad: ${launchpad!!.name}", color = Color.White, fontSize = 16.sp)
                    }
                    Button(
                        onClick = { onLaunchClick(launch, index) },
                        modifier = Modifier
                            .size(120.dp, 50.dp)
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.NavBackground),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(stringResource(R.string.explore), color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        launches.drop(3).forEachIndexed { index, launch ->
            LaunchCard(
                launch = launch,
                onLaunchClick = { launchEntity -> onLaunchClick(launchEntity, index + 3) },
                rocketDetailViewModel = rocketDetailViewModel,
                launchpadDetailViewModel = launchpadDetailViewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}