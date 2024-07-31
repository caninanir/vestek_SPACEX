package com.can_inanir.spacex.ui.feature.easteregg

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.can_inanir.spacex.R
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun EasterEggScreen() {
    val coroutineScope = rememberCoroutineScope()
    var spaceshipPosition by remember { mutableStateOf(Offset.Zero) }
    var targetPosition by remember { mutableStateOf<Offset?>(null) }
    val velocity = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val friction = 0.98f // Coefficient of friction

    LaunchedEffect(targetPosition) {
        while (true) {
            // Update spaceship position
            spaceshipPosition += velocity.value * 0.016f // Assuming 60 updates per second

            // Apply friction to velocity
            val newVelocity = velocity.value * friction
            velocity.snapTo(newVelocity)

            // Accelerate towards target
            targetPosition?.let { target ->
                val direction = target - spaceshipPosition
                if (direction.getDistance() > 5f) {
                    val acceleration = direction.normalize() * 0.5f
                    velocity.snapTo(velocity.value + acceleration)
                }
            }

            // Delay for next frame
            kotlinx.coroutines.delay(16)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                targetPosition = offset
            }
        }
    ) {
        // Space Background
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Spaceship
        Image(
            painter = painterResource(id = R.drawable.rocket_e),
            contentDescription = "Spaceship",
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer(
                    translationX = spaceshipPosition.x - with(LocalDensity.current){25.dp.toPx()}, // Center the spaceship
                    translationY = spaceshipPosition.y - with(LocalDensity.current){25.dp.toPx()}
                )
        )
    }
}

private fun Offset.normalize(): Offset {
    val length = getDistance()
    return if (length != 0f) this / length else Offset.Zero
}

private operator fun Offset.plus(other: Offset) = Offset(x + other.x, y + other.y)
private operator fun Offset.minus(other: Offset) = Offset(x - other.x, y - other.y)
private operator fun Offset.times(scalar: Float) = Offset(x * scalar, y * scalar)
private operator fun Offset.div(scalar: Float) = Offset(x / scalar, y / scalar)