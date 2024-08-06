package com.can_inanir.spacex.presentation.ui.features.informationscreens.upcominglaunches

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.domain.model.Launch
import com.can_inanir.spacex.domain.model.Launchpad
import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.presentation.utils.AppColors
import com.can_inanir.spacex.presentation.viewmodel.LaunchpadDetailViewModel
import com.can_inanir.spacex.presentation.viewmodel.RocketDetailViewModel
import timber.log.Timber

@Composable
fun LaunchCard(
    launch: Launch,
    onLaunchClick: (Launch) -> Unit,
    rocketDetailViewModel: RocketDetailViewModel,
    launchpadDetailViewModel: LaunchpadDetailViewModel
) {
    var rocket by remember { mutableStateOf<Rocket?>(null) }
    var launchpad by remember { mutableStateOf<Launchpad?>(null) }
    LaunchedEffect(launch) {
        rocketDetailViewModel.fetchRocketById(launch.rocket) { rocket = it }
        launchpadDetailViewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onLaunchClick(launch) },
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = AppColors.White,
                text = launch.name,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold))
            )
            Text(
                color = AppColors.White,
                text = "" + extractDateComponents(
                    launch.dateUtc
                ),
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold)),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
            )
            if (rocket != null && launchpad != null) {
                Text(
                    color = AppColors.White,
                    text = stringResource(id = R.string.rocket) + rocket!!.name,
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold)),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
                )
                val imageUrl = rocket!!.flickrImages.randomOrNull()
                Timber.d("Launch image URL: %s", imageUrl)
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
                Spacer(Modifier.height(2.dp))
                Text(
                    color = AppColors.White,
                    text = stringResource(id = R.string.launchpad) + launchpad!!.name,
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Bold)),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
                )
                Spacer(Modifier.height(2.dp))
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
                Spacer(Modifier.height(2.dp))
            } else{
                Text(stringResource(id = R.string.loading), color = AppColors.White)
            }


            launch.links.webcast?.let { webcastUrl ->
                val context = LocalContext.current
                Text(
                    text = stringResource(id = R.string.watch_the_webcast),
                    color = AppColors.CoolGreen,
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