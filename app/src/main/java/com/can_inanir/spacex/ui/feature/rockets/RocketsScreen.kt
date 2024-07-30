package com.can_inanir.spacex.ui.feature.rockets

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsScreen(navController: NavController, fetchDataViewModel: FetchDataViewModel)  {
    val rockets by fetchDataViewModel.rockets.collectAsState(initial = emptyList())
    val favorites by fetchDataViewModel.favorites.collectAsState(initial = emptySet())
    val hazeStateBottomNav = remember { HazeState() }
    val hazeStateBottomNav2 = remember { HazeState() }
    val hazeStateBottomNav3 = remember { HazeState() }
    var selectedRocket by remember { mutableStateOf<RocketEntity?>(null) }



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
                .haze(state = hazeStateBottomNav)
                .haze(state = hazeStateBottomNav2)
                .haze(state = hazeStateBottomNav3),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .haze(state = hazeStateBottomNav)
                .haze(state = hazeStateBottomNav2),
            topBar = {
                TopAppBar(
                    modifier = Modifier.haze(state = hazeStateBottomNav).align(Alignment.TopCenter),
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, titleContentColor = Color.White),
                    title = {
                        Text(
                            text = "SpaceX Rockets",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
                        )
                    }
                )
            },
            content = { paddingValues ->
                RocketList(
                    rockets = rockets,
                    favorites = favorites,
                    paddingValues = paddingValues,
                    onRocketClick = { rocket -> selectedRocket = rocket },
                    viewModel = fetchDataViewModel
                )
            },
            containerColor = Color.Transparent
        )
        if (selectedRocket != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .haze(state = hazeStateBottomNav2),
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
        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            hazeState = hazeStateBottomNav2
        )
    }
}

@Composable
fun RocketList(
    rockets: List<RocketEntity>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onRocketClick: (RocketEntity) -> Unit,
    viewModel: FetchDataViewModel
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
                onFavoriteClick = { viewModel.toggleFavorite(rocket.name) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RocketCard(
    rocket: RocketEntity,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
            //.hazeChild(state = hazeState, shape = RoundedCornerShape(16.dp), HazeStyle(Color(0x33000000), 40.dp,0f)),
        colors = CardDefaults.cardColors(containerColor = Color(0x347C7C7C)),
        //elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
    rocket: RocketEntity,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onClose, modifier = Modifier.size(48.dp)
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
                    painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
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
        HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)
        DetailItem(label = "DIAMETER", value = "${rocket.diameter.meters}m / ${rocket.diameter.feet} ft")
        HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)
        DetailItem(label = "MASS", value = "${rocket.mass.kg} kg / ${rocket.mass.lb} lb")
        HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)
        rocket.payload_weights.find { it.id == "leo" }?.let {
            DetailItem(label = "PAYLOAD TO LEO", value = "${it.kg} kg / ${it.lb} lb")
            HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)
        }
        rocket.payload_weights.find { it.id == "gto" }?.let {
            DetailItem(label = "PAYLOAD TO GTO", value = "${it.kg} kg / ${it.lb} lb")
            HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)
        }
        rocket.payload_weights.find { it.id == "mars" }?.let {
            DetailItem(label = "PAYLOAD TO MARS", value = "${it.kg} kg / ${it.lb} lb")
            HorizontalDivider(color = Color(0x807A7A7A), thickness = 1.dp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Learn More Button
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cool_green), contentColor = Color.White),
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