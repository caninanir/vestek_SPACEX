package com.example.a3rdtimesthecharm


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceXRocketsApp()
        }
    }
}

@Composable
fun SpaceXRocketsApp() {
    val viewModel: RocketsViewModel = viewModel()
    val rockets by viewModel.rockets.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SpaceX Rockets") })
        },
        content = { paddingValues ->
            RocketList(rockets, paddingValues)
        }
    )
}

@Composable
fun RocketList(rockets: List<Rocket>, paddingValues: PaddingValues) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        rockets.forEach { rocket ->
            Text(text = rocket.name)
            Text(text = rocket.description)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}