package com.caninanir.spacex.presentation.ui.features.informationscreens.upcominglaunches

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.caninanir.spacex.R
import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.domain.model.Launchpad
import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.presentation.utils.AppColors
import com.caninanir.spacex.presentation.viewmodel.LaunchpadDetailViewModel
import com.caninanir.spacex.presentation.viewmodel.RocketDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun LaunchDetail(
    launch: Launch,
    launchIndex: Int,
    onClose: () -> Unit,
    rocketDetailViewModel: RocketDetailViewModel,
    launchpadDetailViewModel: LaunchpadDetailViewModel
) {
    val context = LocalContext.current
    var rocket by remember { mutableStateOf<Rocket?>(null) }
    var launchpad by remember { mutableStateOf<Launchpad?>(null) }

    LaunchedEffect(launch) {
        rocketDetailViewModel.fetchRocketById(launch.rocket) { rocket = it }
        launchpadDetailViewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
    }

    // Determine the image resource based on the launch index
    val imageResId = when (launchIndex % 3) {
        0 -> R.drawable.image_1
        1 -> R.drawable.image_2
        else -> R.drawable.image_3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.HalfGrayTransparentBackground)
    ) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bg),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = AppColors.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.upcoming_launches),
                        style = MaterialTheme.typography.headlineLarge,
                        color = AppColors.White,
                        overflow = TextOverflow.Visible,
                        softWrap = false,
                        textAlign = TextAlign.Center
                    )
                }
            }
            // Top Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                // Change alignment to Box modifier
                Box(
                    modifier = Modifier
                        .align(
                            when (imageResId) {
                                R.drawable.image_1 -> Alignment.Center
                                R.drawable.image_2 -> Alignment.CenterStart
                                else -> Alignment.CenterEnd
                            }
                        )
                        .padding(all = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = launch.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = AppColors.White,
                            fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                            softWrap = true,
                            overflow = TextOverflow.Clip
                        )

                        launch.links.webcast?.let { webcastUrl ->
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.NavBackground,
                                    contentColor = AppColors.CoolGreen
                                ),
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(stringResource(id = R.string.watch_the_webcast))
                            }
                        }
                    }
                }
            }
// Date Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, top = 30.dp, start = 4.dp, end = 16.dp)
            ) {
                val dateComponents = extractDateComponents(launch.dateUtc)
                val fontSize = 40f
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Evenly space the columns
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f) // Equal space for each column
                    ) {
                        Row {
                            Text(
                                text = dateComponents.first.split(" ")[0],
                                style = MaterialTheme.typography.displayLarge,
                                color = AppColors.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                lineHeight = TextUnit(16F, TextUnitType.Sp),
                                letterSpacing = TextUnit(0.001f, TextUnitType.Sp),
                                fontSize = TextUnit(fontSize, TextUnitType.Sp),
                                softWrap = false

                            )
                            Text(
                                text = dateComponents.first.split(" ")[1],
                                style = MaterialTheme.typography.displaySmall,
                                color = AppColors.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                lineHeight = TextUnit(16F, TextUnitType.Sp),
                                letterSpacing = TextUnit(0F, TextUnitType.Sp),
                                fontSize = TextUnit(fontSize / 2, TextUnitType.Sp),
                                softWrap = false
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.dateB),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = dateComponents.second,
                            style = MaterialTheme.typography.displayLarge,
                            color = AppColors.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            lineHeight = TextUnit(16F, TextUnitType.Sp),
                            letterSpacing = TextUnit(0F, TextUnitType.Sp),
                            fontSize = TextUnit(fontSize, TextUnitType.Sp),
                            softWrap = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.year),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = dateComponents.third,
                            style = MaterialTheme.typography.displayLarge,
                            color = AppColors.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            lineHeight = TextUnit(16F, TextUnitType.Sp),
                            letterSpacing = TextUnit(0F, TextUnitType.Sp),
                            fontSize = TextUnit(fontSize, TextUnitType.Sp),
                            softWrap = false
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dateComponents.fourth,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            softWrap = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Section
            rocket?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.FullTransparentBackground)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.vehicle),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColors.White
                    )
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppColors.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Launchpad Section
            launchpad?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.FullTransparentBackground)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.launchpadH),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = it.fullName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppColors.White
                    )
                    Text(
                        text = it.details,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppColors.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Launchpad & Rocket Images
            rocket?.flickrImages?.forEach { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
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
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .heightIn(max = 300.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Helper function to extract date components
fun extractDateComponents(dateUtc: String): Quad<String, String, String, String> {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val date: Date? = inputFormat.parse(dateUtc)
    return if (date != null) {
        val dayMonthFormat = SimpleDateFormat("dd MMM", Locale.US).apply { timeZone = TimeZone.getDefault() }
        val yearFormat = SimpleDateFormat("yyyy", Locale.US).apply { timeZone = TimeZone.getDefault() }
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US).apply { timeZone = TimeZone.getDefault() }
        val timeZoneFormat = SimpleDateFormat("z", Locale.US).apply { timeZone = TimeZone.getDefault() }
        Quad(dayMonthFormat.format(date), yearFormat.format(date), timeFormat.format(date), timeZoneFormat.format(date))
    } else {
        Quad("-", "-", "-", "-")
    }
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
