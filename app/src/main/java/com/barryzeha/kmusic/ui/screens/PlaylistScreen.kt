package com.barryzeha.kmusic.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.barryzeha.kmusic.data.SongEntity
import com.barryzeha.kmusic.ui.viewmodel.MainViewModel


/****
 * Project KMusic
 * Created by Barry Zea H. on 18/04/25.
 * Copyright (c)  All rights reserved.
 ***/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayListScreen(mainViewModel: MainViewModel = viewModel() ){
    LaunchedEffect(true){
        mainViewModel.scanSongs()
    }
    val songsList by mainViewModel.songsList.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {MyToolbar {  }},
        content = {padding->
            VerticalRecyclerView(songsList, Modifier.padding(padding))
        }
        )
}

@Composable
fun VerticalRecyclerView(songsList: List<SongEntity>, modifier: Modifier){
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
    ) {
        items(songsList) { song->
            SongItem(song) { }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyToolbar(onClick: (String) -> Unit) {
    TopAppBar(title = { Text(text = "Library") },
        modifier = Modifier.background(Color.Transparent)
    )
}

