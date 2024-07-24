package com.can_inanir.spacex.ui.feature.rocketdetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.remote.FetchDataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketDetailScreen(rocketId: String?) {
    val viewModel: FetchDataViewModel = viewModel()
    val rocket by viewModel.selectedRocket.collectAsState(initial = null)
    val favorites by viewModel.favorites.collectAsState(initial = emptySet())

    LaunchedEffect(rocketId) {
        if (rocketId != null) {
            viewModel.fetchRocketById(rocketId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(rocket?.name ?: "Rocket Details") })
        },
        content = { paddingValues ->
            if (rocket != null) {
                RocketDetail(
                    rocket = rocket!!,
                    isFavorite = favorites.contains(rocket!!.name),
                    paddingValues = paddingValues,
                    viewModel = viewModel
                )
            } else {
                Text("Loading...", modifier = Modifier.padding(paddingValues))
            }
        }
    )
}

@Composable
fun RocketDetail(
    rocket: Rocket,
    isFavorite: Boolean,
    paddingValues: PaddingValues,
    viewModel: FetchDataViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        rocket.flickr_images.firstOrNull()?.let { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(text = rocket.name, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "First flight: ${rocket.first_flight}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Cost per launch: \$${rocket.cost_per_launch}")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Success rate: ${rocket.success_rate_pct}%")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = rocket.description)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia))
            context.startActivity(intent)
        }) {
            Text("Learn More")
        }
        IconButton(onClick = { viewModel.toggleFavorite(rocket.name) }) {
            Icon(
                painter = painterResource(
                    id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                ),
                contentDescription = null
            )
        }
    }
}