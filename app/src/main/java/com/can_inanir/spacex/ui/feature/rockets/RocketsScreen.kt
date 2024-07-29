package com.can_inanir.spacex.ui.feature.rockets

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RocketsScreen(navController: NavController, fetchDataViewModel: FetchDataViewModel = viewModel()) {
    val rockets by fetchDataViewModel.rockets.collectAsState(initial = emptyList())
    val favorites by fetchDataViewModel.favorites.collectAsState(initial = emptySet())
    val hazeStateBottomNav = remember { HazeState() }
    var selectedRocket by remember { mutableStateOf<Rocket?>(null) }


    BackHandler(enabled = selectedRocket != null) {
        selectedRocket = null
    }

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
                .haze(state = hazeStateBottomNav),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .haze(state = hazeStateBottomNav),
            content = { paddingValues ->
                RocketList(
                    rockets = rockets,
                    favorites = favorites,
                    paddingValues = paddingValues,
                    onRocketClick = { rocket -> selectedRocket = rocket },
                    viewModel = fetchDataViewModel,
                    hazeState = hazeStateBottomNav
                )
            },
            containerColor = Color.Transparent
        )

        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            hazeState = hazeStateBottomNav
        )

        if (selectedRocket != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x1AFFFFFF))
            ) {
                RocketDetail(
                    rocket = selectedRocket!!,
                    isFavorite = favorites.contains(selectedRocket!!.name),
                    onClose = { selectedRocket = null },
                    hazeState = hazeStateBottomNav,
                    viewModel = fetchDataViewModel
                )
            }
        }
    }
}

@Composable
fun RocketList(
    rockets: List<Rocket>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onRocketClick: (Rocket) -> Unit,
    viewModel: FetchDataViewModel,
    hazeState: HazeState
) {
    val sortedRockets = rockets.sortedByDescending { favorites.contains(it.name) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        sortedRockets.forEach { rocket ->
            RocketCard(
                rocket = rocket,
                isFavorite = favorites.contains(rocket.name),
                onClick = { onRocketClick(rocket) },
                hazeState = hazeState,
                onFavoriteClick = { viewModel.toggleFavorite(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RocketCard(
    rocket: Rocket,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    hazeState: HazeState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        // .hazeChild(state = hazeState, shape = RoundedCornerShape(16.dp), HazeStyle(Color(0x26FFFFFF), 20.dp, 0f)),
        colors = CardDefaults.cardColors(containerColor = Color(0x0DFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = rocket.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
                )
                IconButton(onClick = onFavoriteClick, modifier = Modifier.size(64.dp)) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        tint = Color.Unspecified,
                        painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            rocket.flickr_images.firstOrNull()?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),

                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}
@Composable
fun RocketDetail(
    rocket: Rocket,
    isFavorite: Boolean,
    onClose: () -> Unit,
    hazeState: HazeState,
    viewModel: FetchDataViewModel
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .hazeChild(state = hazeState, shape = RoundedCornerShape(1.dp), HazeStyle(Color(0x80000000), 20.dp, 0f))
    ) {
        // Header Row with Back Arrow, Text, and Favorite Icon
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = rocket.name,
                style = MaterialTheme.typography.headlineLarge,
                color = colorResource(id = R.color.cool_green),
                fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal)),
                modifier = Modifier.align(Alignment.CenterVertically),
                softWrap = false
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { viewModel.toggleFavorite(rocket.name) }) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

        // Rocket Image
        rocket.flickr_images.firstOrNull()?.let { imageUrl ->
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

        // Rocket Description
        Text(
            text = rocket.description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Rocket Details
        DetailItem(label = "HEIGHT", value = "${rocket.height.meters}m / ${rocket.height.feet} ft")
        DetailItem(label = "DIAMETER", value = "${rocket.diameter.meters}m / ${rocket.diameter.feet} ft")
        DetailItem(label = "MASS", value = "${rocket.mass.kg} kg / ${rocket.mass.lb} lb")

        rocket.payload_weights.find { it.id == "leo" }?.let {
            DetailItem(label = "PAYLOAD TO LEO", value = "${it.kg} kg / ${it.lb} lb")
        }
        rocket.payload_weights.find { it.id == "gto" }?.let {
            DetailItem(label = "PAYLOAD TO GTO", value = "${it.kg} kg / ${it.lb} lb")
        }
        rocket.payload_weights.find { it.id == "mars" }?.let {
            DetailItem(label = "PAYLOAD TO MARS", value = "${it.kg} kg / ${it.lb} lb")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Launch More Button
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia))
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Learn More")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Additional Rocket Images
        rocket.flickr_images.drop(1).forEach { imageUrl ->
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
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
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