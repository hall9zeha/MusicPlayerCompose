package com.barryzeha.kmusic.ui.screens

import android.annotation.SuppressLint

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import com.barryzeha.kmusic.MainApp
import com.barryzeha.kmusic.common.PlayerState
import com.barryzeha.kmusic.common.playMediaAtIndex
import com.barryzeha.kmusic.common.updatePlaylist
import com.barryzeha.kmusic.data.SongEntity
import com.barryzeha.kmusic.data.toMediaItem
import com.barryzeha.kmusic.ui.components.SongItem
import com.barryzeha.kmusic.ui.viewmodel.MainViewModel


/****
 * Project KMusic
 * Created by Barry Zea H. on 18/04/25.
 * Copyright (c)  All rights reserved.
 ***/

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayListScreen(mediaController: MediaController?, mainViewModel: MainViewModel = viewModel() ){
    val songsList by mainViewModel.songsList.collectAsStateWithLifecycle()
    LaunchedEffect(songsList.isNotEmpty()) {
        mediaController?.updatePlaylist(songsList.map { it.toMediaItem() })
    }
    LaunchedEffect(true){
        mainViewModel.scanSongs()
    }
    Scaffold(
        topBar = {MyToolbar {  }},
        content = {padding->
            VerticalRecyclerView(mediaController,songsList, Modifier.padding(padding))
        }
        )
}


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun VerticalRecyclerView(mediaController: MediaController?,songsList: List<SongEntity>, modifier: Modifier){
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(songsList) { index,song->
            SongItem(song) { song ->
                mediaController?.playMediaAtIndex(index)
                MainApp.mPrefs?.currentIndexSaved = index
            }
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

