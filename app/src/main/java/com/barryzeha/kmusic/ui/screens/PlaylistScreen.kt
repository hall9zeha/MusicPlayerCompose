package com.barryzeha.kmusic.ui.screens

import android.annotation.SuppressLint

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.barryzeha.kmusic.data.SongEntity
import com.barryzeha.kmusic.ui.viewmodel.MainViewModel


/****
 * Project KMusic
 * Created by Barry Zea H. on 18/04/25.
 * Copyright (c)  All rights reserved.
 ***/

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayListScreen(mediaController:MediaController?,mainViewModel: MainViewModel = viewModel() ){
    LaunchedEffect(true){
        mainViewModel.scanSongs()
    }
    val songsList by mainViewModel.songsList.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {MyToolbar {  }},
        content = {padding->
            VerticalRecyclerView(mediaController,songsList, Modifier.padding(padding))
        }
        )
}


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun VerticalRecyclerView(mediaController:MediaController?,songsList: List<SongEntity>, modifier: Modifier){
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
    ) {
        items(songsList) { song->
            SongItem(song) {song->
                val mediaItem=MediaItem.Builder()
                    .setMediaId(song.idSong.toString())
                    .setUri(song.pathFile)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setArtist(song.artist)
                            .setTitle(song.title).build()
                    ).build()
                mediaController?.setMediaItem(mediaItem)
                mediaController?.prepare()
                mediaController?.play()
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

