package com.can_inanir.spacex.ui.feature.informationscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.can_inanir.spacex.R
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.ui.main.AppColors
import java.util.Locale

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
            //.height(250.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardBackground)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 17.dp, top = 20.dp),
                    text = rocket.name.uppercase(Locale.ROOT),
                    //style = MaterialTheme.typography.headlineMedium,
                    fontSize = 26.sp,
                    color = AppColors.White,
                    fontFamily = FontFamily(Font(R.font.nasalization, FontWeight.Normal))
                )
                IconButton(onClick = onFavoriteClick, modifier = Modifier.size(64.dp)) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        tint = Color.Unspecified,
                        painter = painterResource(
                            id = if (isFavorite) R.drawable.buttons_icons_favorites_active_pressed else R.drawable.buttons_icons_favorites_enable
                        ),
                        contentDescription = null
                    )
                }
            }
            rocket.flickrImages.firstOrNull()?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
//                        .fillMaxHeight(),
                        .height(250.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

