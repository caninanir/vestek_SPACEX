package com.can_inanir.spacex.ui.feature.informationscreens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.LaunchpadDetailViewModel
import com.can_inanir.spacex.data.remote.RocketDetailViewModel
import com.can_inanir.spacex.ui.main.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun RocketDetail(
    rocket: RocketEntity,
    isFavorite: Boolean,
    onClose: () -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(AppColors.HalfGrayTransparentBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = AppColors.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = rocket.name,
                style = MaterialTheme.typography.headlineLarge,
                color = AppColors.CoolGreen,
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                modifier = Modifier.align(Alignment.CenterVertically),
                softWrap = false
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onFavoriteClick(rocket.name) }) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.buttons_icons_favorites_active_pressed else R.drawable.buttons_icons_favorites_enable
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
        rocket.flickrImages.firstOrNull()?.let { imageUrl ->
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
        Text(
            text = rocket.description,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        DetailItem(label = "Height", value = "${rocket.height.meters}m / ${rocket.height.feet} ft")
        HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        DetailItem(
            label = "Diameter",
            value = "${rocket.diameter.meters}m / ${rocket.diameter.feet} ft"
        )
        HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        DetailItem(label = "Mass", value = "${rocket.mass.kg} kg / ${rocket.mass.lb} lb")
        HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        rocket.payloadWeights.find { it.id == "leo" }?.let {
            DetailItem(
                label = "Payload to LEO",
                value = "${it.kg} kg / ${it.lb} lb"
            )
            HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        }
        rocket.payloadWeights.find { it.id == "gto" }?.let {
            DetailItem(
                label = "Payload to GTO",
                value = "${it.kg} kg / ${it.lb} lb"
            )
            HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        }
        rocket.payloadWeights.find { it.id == "mars" }?.let {
            DetailItem(
                label = "Payload to Mars",
                value = "${it.kg} kg / ${it.lb} lb"
            )
            HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.CoolGreen,
                contentColor = AppColors.White
            ),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia))
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Learn More")
        }
        Spacer(modifier = Modifier.height(16.dp))
        rocket.flickrImages.drop(1).forEach { imageUrl ->
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
    }
}

@Composable
fun LaunchDetail(
    launch: LaunchEntity,
    onClose: () -> Unit,
    rocketDetailViewModel: RocketDetailViewModel,
    launchpadDetailViewModel: LaunchpadDetailViewModel
) {
    val context = LocalContext.current
    var rocket by remember { mutableStateOf<RocketEntity?>(null) }
    var launchpad by remember { mutableStateOf<LaunchpadEntity?>(null) }
    LaunchedEffect(launch) {
        rocketDetailViewModel.fetchRocketById(launch.rocket) { rocket = it }
        launchpadDetailViewModel.fetchLaunchpadById(launch.launchpad) { launchpad = it }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(AppColors.HalfGrayTransparentBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = AppColors.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = launch.name,
                style = MaterialTheme.typography.headlineLarge,
                color = AppColors.CoolGreen,
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                modifier = Modifier.align(Alignment.CenterVertically),
                softWrap = false
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Text(
            text = launch.details ?: "No details available",
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        DetailItem(label = "DATE", value = formatUtcToRfc1123(launch.dateUtc))
        HorizontalDivider(color = AppColors.DividerColor, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            launch.links.wikipedia?.let { wikipediaUrl ->
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.CoolGreen,
                        contentColor = AppColors.White
                    ),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
                        context.startActivity(intent)
                    }
                ) {
                    Text("Learn More")
                }
            }
            launch.links.webcast?.let { webcastUrl ->
                Text(
                    text = "Watch the webcast",
                    color = AppColors.CoolGreen,
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webcastUrl))
                        context.startActivity(intent)
                    }
                )
            }
            launch.links.article?.let { articleUrl ->
                Text(
                    text = "Read Article",
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                    color = AppColors.CoolGreen,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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
            color = AppColors.White
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.White
        )
    }
}

fun formatUtcToRfc1123(dateUtc: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).apply {
        timeZone = TimeZone.getDefault()
    }
    val date: Date? = inputFormat.parse(dateUtc)
    return date?.let { outputFormat.format(it) }.toString()
}