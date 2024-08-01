package com.can_inanir.spacex.ui.feature.favorite

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.FetchDataViewModel
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavBar
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem
import com.can_inanir.spacex.ui.feature.login.AuthViewModel

// import dev.chrisbanes.haze.HazeState
// import dev.chrisbanes.haze.HazeStyle
// import dev.chrisbanes.haze.haze
// import dev.chrisbanes.haze.hazeChild

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val fetchDataViewModel: FetchDataViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val userState by authViewModel.userState.collectAsState()
    val rockets by fetchDataViewModel.rockets.collectAsState(initial = emptyList())
    val favorites by fetchDataViewModel.favorites.collectAsState(initial = emptySet())
//    val hazeState = remember { HazeState() }
//    val hazeState2 = remember { HazeState() }
    var selectedRocket by remember { mutableStateOf<RocketEntity?>(null) }
    var showProfile by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bgl),
            contentDescription = stringResource(R.string.background),
            modifier = Modifier
                .fillMaxSize(),
//                .haze(state = hazeState)
//                .haze(state = hazeState2),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
//                .haze(state = hazeState)
//                .haze(state = hazeState2),
            topBar = {
                TopAppBar(
                    modifier = Modifier
//                        .haze(state = hazeState)
                        .align(Alignment.TopCenter),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.favorite_rockets),
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
                        )
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.profile),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { showProfile = true },
                            tint = colorResource(R.color.cool_green)
                        )
                    }
                )
            },
            content = { paddingValues ->
                if (userState != null) {
                    FavoriteRocketList(
                        rockets = rockets,
                        favorites = favorites,
                        paddingValues = paddingValues,
                        onRocketClick = { rocket -> selectedRocket = rocket },
                        viewModel = fetchDataViewModel
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.log_in_to_view_favorites), color = Color.White)
                    }
                }
            },
            containerColor = Color.Transparent
        )
        if (selectedRocket != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(color = 0x1AFFFFFF))
//                    .haze(state = hazeState2)
            ) {
                RocketDetail(
                    rocket = selectedRocket!!,
                    isFavorite = favorites.contains(selectedRocket!!.name),
                    onClose = { selectedRocket = null },
//                    hazeState = hazeState,
                    viewModel = fetchDataViewModel
                )
            }
        }
        if (showProfile) {
            if (userState == null) {
                navController.navigate(BottomNavItem.Login.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { showProfile = false },
                contentAlignment = Alignment.Center
            ) {
                ProfileOverlay(
                    authViewModel = authViewModel,
                    navController = navController,
                    onClose = { showProfile = false },
//                    hazeState = hazeState2
                )
            }
        }
        BottomNavBar(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
//            hazeState = hazeState2
        )
    }
}

@Composable
fun ProfileOverlay(
    authViewModel: AuthViewModel,
    navController: NavController,
    onClose: () -> Unit,
//    hazeState: HazeState
) {
    val userState by authViewModel.userState.collectAsState()
    Box(
        modifier = Modifier
            .size(width = 250.dp, height = 150.dp)
            .background(colorResource(id = R.color.transparent_background)),
//            .hazeChild(
//                state = hazeState,
//                shape = RoundedCornerShape(24.dp),
//                HazeStyle(Color(0x808A8A8A), 20.dp, 0f)
//            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.logged_in_as), color = colorResource(id = R.color.cool_green))
            userState?.let { user ->
                Text(text = "${user.email}", color = colorResource(id = R.color.cool_green))
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                    onClick = {
                        authViewModel.logout()
                        navController.navigate(BottomNavItem.Login.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                        onClose()
                    }
                ) {
                    Text(text = stringResource(R.string.logout))
                }
            }
        }
    }
}

@Composable
fun FavoriteRocketList(
    rockets: List<RocketEntity>,
    favorites: Set<String>,
    paddingValues: PaddingValues,
    onRocketClick: (RocketEntity) -> Unit,
    viewModel: FetchDataViewModel
) {
    val favoriteRockets = rockets.filter { favorites.contains(it.name) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        favoriteRockets.forEach { rocket ->
            RocketCard(
                rocket = rocket,
                isFavorite = true,
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
        colors = CardDefaults.cardColors(containerColor = Color(color = 0x0DFFFFFF))
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
                        painter = painterResource(
                            id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                        ),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            rocket.flickrImages.firstOrNull()?.let { imageUrl ->
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
//    hazeState: HazeState,
    viewModel: FetchDataViewModel
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorResource(id = R.color.transparent_background))
//            .hazeChild(
//                state = hazeState,
//                shape = RoundedCornerShape(1.dp),
//                HazeStyle(Color(0x80000000), 20.dp, 0f)
//            )
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
                    contentDescription = stringResource(R.string.close),
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
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        DetailItem(label = stringResource(R.string.height),
            value = "${rocket.height.meters}m / ${rocket.height.feet} ft")
        HorizontalDivider(color = Color(color = 0x807A7A7A), thickness = 1.dp)
        DetailItem(label = stringResource(R.string.diameter), value = "${rocket.diameter.meters}m / ${rocket.diameter.feet} ft")
        HorizontalDivider(color = Color(color = 0x807A7A7A), thickness = 1.dp)
        DetailItem(label = stringResource(R.string.mass), value = "${rocket.mass.kg} kg / ${rocket.mass.lb} lb")
        HorizontalDivider(color = Color(color = 0x807A7A7A), thickness = 1.dp)
        rocket.payloadWeights.find { it.id == stringResource(R.string.leo) }?.let {
            DetailItem(label = "leo", value = "${it.kg} kg / ${it.lb} lb")
            HorizontalDivider(color = Color(color = 0x807A7A7A), thickness = 1.dp)
        }
        rocket.payloadWeights.find { it.id == "gto" }?.let {
            DetailItem(label = stringResource(R.string.payload_to_gto), value = "${it.kg} kg / ${it.lb} lb")
            HorizontalDivider(color = Color(color = 0x807A7A7A), thickness = 1.dp)
        }
        rocket.payloadWeights.find { it.id == "mars" }?.let {
            DetailItem(label = stringResource(R.string.payload_to_mars), value = "${it.kg} kg / ${it.lb} lb")
            HorizontalDivider(color = Color(color = 0x807A7A7A), thickness = 1.dp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.cool_green),
                contentColor = Color.White
            ),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia))
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.learn_more))
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
