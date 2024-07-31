package com.can_inanir.spacex.ui.feature.easteregg

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.can_inanir.spacex.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
@Composable
fun EasterEggScreen(navController: NavController) {
    BackHandler { navController.popBackStack() }

    val debug = false

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.toPx() }
    val screenHeightPx = with(density) { screenHeight.toPx() }

    var spaceshipPosition by remember { mutableStateOf(Offset(screenWidthPx / 2f, screenHeightPx / 2f)) }
    var targetPosition by remember { mutableStateOf<Offset?>(null) }
    val velocity = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val friction = 0.99f // Coefficient of friction
    var angle by remember { mutableStateOf(180f) } // Start upside down

    // Timer
    var elapsedTime by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedTime += 1
        }
    }

    // Asteroids data class
    data class Asteroid(
        val size: Float,
        val position: Offset,
        val velocity: Offset,
        val rotation: Float,
        val imageRes: Int
    )

    val asteroids = remember { mutableStateListOf<Asteroid>() }
    val randomGenerator = Random.Default

    LaunchedEffect(Unit) {
        while (true) {
            val delayTime = (2000 - 50 * elapsedTime).coerceAtLeast(200)
            delay(delayTime.toLong())

            val side = randomGenerator.nextInt(4)
            val (posX, posY) = when (side) {
                0 -> Pair(-50f, randomGenerator.nextFloat() * screenHeightPx) // Left
                1 -> Pair(screenWidthPx + 50f, randomGenerator.nextFloat() * screenHeightPx) // Right
                2 -> Pair(randomGenerator.nextFloat() * screenWidthPx, -50f) // Top
                else -> Pair(randomGenerator.nextFloat() * screenWidthPx, screenHeightPx + 50f) // Bottom
            }

            val angle = randomGenerator.nextFloat() * 360f
            val velocityMagnitude = randomGenerator.nextInt(200, 1000).toFloat()
            val velocityX = cos(Math.toRadians(angle.toDouble())).toFloat() * velocityMagnitude
            val velocityY = sin(Math.toRadians(angle.toDouble())).toFloat() * velocityMagnitude

            val newAsteroid = Asteroid(
                size = randomGenerator.nextInt(30, 300).toFloat(),
                position = Offset(posX, posY),
                velocity = Offset(velocityX, velocityY),
                rotation = (randomGenerator.nextFloat() * 2f - 1f) * 30f,
                imageRes = R.drawable.fav_d
            )

            asteroids.add(newAsteroid)
        }
    }

    LaunchedEffect(targetPosition) {
        while (true) {
            spaceshipPosition += velocity.value * 0.016f // Assuming 60 updates per second
            val newVelocity = velocity.value * friction
            velocity.snapTo(newVelocity)

            targetPosition?.let { target ->
                val direction = target - spaceshipPosition
                if (direction.getDistance() > 5f) {
                    val acceleration = direction.normalize() * 5.5f // Increase speed
                    velocity.snapTo(velocity.value + acceleration)
                }
                val newAngle = atan2(direction.y, direction.x) * 180 / PI.toFloat()
                angle = newAngle - 90 + 180 // Adjust for upside-down spaceship
            }

            // Update asteroid positions
            asteroids.forEachIndexed { index, asteroid ->
                val curving = randomGenerator.nextFloat()
                val curveVelocity = if (index % 2 == 0) asteroid.velocity.rotate(curving * 10f) else asteroid.velocity.rotate(-curving * 10f)
                asteroids[index] = asteroid.copy(position = asteroid.position + curveVelocity * 0.016f)
            }

            // Remove asteroids that are off-screen
            asteroids.removeAll {
                it.position.x < -it.size - 500 ||
                        it.position.x > screenWidthPx + it.size + 500 ||
                        it.position.y < -it.size - 500 ||
                        it.position.y > screenHeightPx + it.size + 500
            }

            // Check collision exactly when they touch
            asteroids.forEach {
                if ((spaceshipPosition - it.position).getDistance() <= ((it.size / 2f) + 25f)) { // Collision detected if they touch
                    Toast.makeText(context, "You crashed! High score = $elapsedTime", Toast.LENGTH_LONG).show()
                    elapsedTime = 0
                    asteroids.clear()
                    velocity.snapTo(Offset.Zero)
                    spaceshipPosition = Offset(screenWidthPx / 2f, screenHeightPx / 2f)
                    targetPosition = null
                }
            }

           delay(16)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures { change, _ -> targetPosition = change.position }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val firstDown = event.changes.firstOrNull()
                        firstDown?.let { pointerInputChange ->
                            pointerInputChange.consume()
                            targetPosition = pointerInputChange.position
                        }
                    }
                }
            }
    ) {
        // Space Background
        Image(
            painter = painterResource(id = R.drawable.space_x_android_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Spaceship
        Image(
            painter = painterResource(id = R.drawable.rocket_e),
            contentDescription = "Spaceship",
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer(
                    translationX = spaceshipPosition.x - with(LocalDensity.current) { 25.dp.toPx() },
                    translationY = spaceshipPosition.y - with(LocalDensity.current) { 25.dp.toPx() },
                    rotationZ = angle
                )
        )

        // Debug spaceship collision outline
        if (debug) {
            Canvas(modifier = Modifier
                .graphicsLayer(
                    translationX = spaceshipPosition.x - with(LocalDensity.current) { 25.dp.toPx() },
                    translationY = spaceshipPosition.y - with(LocalDensity.current) { 25.dp.toPx() },
                    rotationZ = angle
                )
                .size(50.dp)
            ) {
                drawCircle(
                    color = Color.Red,
                    radius = 25.dp.toPx(),
                    style = Stroke(width = 2f)
                )
            }
        }

        // Asteroids
        asteroids.forEach { asteroid ->
            Image(
                painter = painterResource(id = asteroid.imageRes),
                contentDescription = "Asteroid",
                modifier = Modifier
                    .size(asteroid.size.dp) // Scale image with size
                    .graphicsLayer(
                        translationX = asteroid.position.x - (with(LocalDensity.current) {asteroid.size.dp.toPx()} / 2),
                        translationY = asteroid.position.y - (with(LocalDensity.current) {asteroid.size.dp.toPx()} / 2),
                        rotationZ = asteroid.rotation
                    )
            )

            // Debug asteroid collision outlines
            if (debug) {
                Canvas(modifier = Modifier
                    .size(asteroid.size.dp)
                    .graphicsLayer(
                        translationX = asteroid.position.x - (with(LocalDensity.current) {asteroid.size.dp.toPx()} / 2),
                        translationY = asteroid.position.y - (with(LocalDensity.current) {asteroid.size.dp.toPx()} / 2)
                    )
                ) {
                    drawCircle(
                        color = Color.Red,
                        radius = asteroid.size.dp.toPx() / 2,
                        style = Stroke(width = 2f)
                    )
                }
            }
        }
    }
}

// Utility to rotate an Offset
private fun Offset.rotate(degrees: Float): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    val cosTheta = kotlin.math.cos(radians)
    val sinTheta = kotlin.math.sin(radians)
    return Offset(
        (cosTheta * x - sinTheta * y).toFloat(),
        (sinTheta * x + cosTheta * y).toFloat()
    )
}

private fun Offset.normalize(): Offset {
    val length = getDistance()
    return if (length != 0f) this / length else Offset.Zero
}

private operator fun Offset.plus(other: Offset) = Offset(x + other.x, y + other.y)
private operator fun Offset.minus(other: Offset) = Offset(x - other.x, y - other.y)
private operator fun Offset.times(scalar: Float) = Offset(x * scalar, y * scalar)
private operator fun Offset.div(scalar: Float) = Offset(x / scalar, y / scalar)

// Extension function to calculate Euclidean distance
private fun Offset.getDistance() = kotlin.math.sqrt(x * x + y * y)