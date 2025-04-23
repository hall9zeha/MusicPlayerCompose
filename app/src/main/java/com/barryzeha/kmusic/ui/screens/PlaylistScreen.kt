package com.barryzeha.kmusic.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

@Composable
fun PlayListScreen(mainViewModel: MainViewModel = viewModel() ){
    LaunchedEffect(true){
        mainViewModel.scanSongs()
    }
    val songsList by mainViewModel.songsList.collectAsStateWithLifecycle()
    VerticalRecyclerView(songsList)
}

@Composable
fun VerticalRecyclerView(songsList: List<SongEntity>){
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(songsList) { song->
            SongItem(song) { }
        }

    }
}
