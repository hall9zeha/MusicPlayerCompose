package com.barryzeha.kmusic.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.barryzeha.kmusic.R
import com.barryzeha.kmusic.common.PlayerState
import com.barryzeha.kmusic.common.loadArtwork
import com.barryzeha.kmusic.ui.viewmodel.MainViewModel

/****
 * Project KMusic
 * Created by Barry Zea H. on 17/04/25.
 * Copyright (c)  All rights reserved.
 ***/
@Composable
fun PlayerScreen(mainViewModel: MainViewModel = viewModel(), playerState: PlayerState ) {
    LaunchedEffect(true){
        mainViewModel.scanSongs()
    }
    val response by mainViewModel.songsList.collectAsStateWithLifecycle()
    /*var count =0
    response.forEach {
        count++
        Log.e("PISTA", "$count -> ${it.title}" )
    }*/
    Box(Modifier
        .fillMaxSize()
        .padding(8.dp)){
        Body(playerState)
    }
}

@Composable
fun Body(playerState: PlayerState) {

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        CoverAlbumArt(playerState.currentMediaItem?.mediaId)
        SongDescription(playerState.mediaMetadata)
        Seekbar(playerState)
        PlayerControls(playerState)
    }
}

@Composable
fun CoverAlbumArt(mediaItemId: String?) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val bitmap = mediaItemId?.let{ loadArtwork(LocalContext.current, mediaItemId.toLong()) }
    Card(
        modifier = Modifier
            .padding(top = 48.dp)
            /*.aspectRatio(1f)*//* aspect ratio square 1:1 */
            .size(screenWidth * 0.8f),
        /* For control the square size we choose this option */
        /*.fillMaxWidth(0.8f)*/
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        shape = RoundedCornerShape(16.dp),

        ) {
        bitmap?.let {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = bitmap.asImageBitmap(),
                contentScale = ContentScale.Crop,
                contentDescription = "Cover album art"
            )
        }?:run{
            Image(
                modifier = Modifier.fillMaxSize(),
                painter= painterResource(R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop,
                contentDescription = "Cover album art"
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun SongDescription(metadata: MediaMetadata){
    Column(modifier= Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier=Modifier.basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
            text=metadata.title.toString(),
            fontSize = 18.sp)
        Text(modifier= Modifier.basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
            text = metadata.artist.toString(), fontSize = 16.sp)
        Text(modifier = Modifier.basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
            text=metadata.albumTitle.toString(), fontSize = 16.sp)

    }
}
@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Seekbar(playerState: PlayerState){
    var sliderValue by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    LaunchedEffect(playerState) {
        duration = if(playerState.player.duration<0) 0 else playerState.player.duration
        snapshotFlow { playerState.currentPosition }
            .collect { sliderValue = it
            }
    }

    val interactionSource = MutableInteractionSource()
    Column(modifier=Modifier
        .fillMaxWidth()
        .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(modifier= Modifier.fillMaxWidth()
            ,value = sliderValue.toFloat(),
            onValueChange = { newValue ->
                playerState.player.seekTo(newValue.toLong())
            },
            valueRange = 0f..(duration.toFloat()),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(16.dp,16.dp)
                )
            },
            track = {sliderState->
                SliderDefaults.Track(
                    modifier = Modifier.height(4.dp),
                    drawStopIndicator = {},
                    colors = SliderDefaults.colors(
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent,
                        thumbColor = Color.Transparent,
                    ),
                    sliderState = sliderState, thumbTrackGapSize = 0.dp)
            }
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(modifier =Modifier.padding(start = 12.dp),
                text = "00:00",
                fontSize = 12.sp)
            Text(modifier=Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
                text = "00:00",
                fontSize = 12.sp
                )
        }
    }
}

@Composable
fun PlayerControls(playerState: PlayerState){
    var playState by remember { mutableStateOf(playerState.isPlaying) }

    val playPause =if(!playState) Icons.Filled.PlayArrow else Icons.Filled.Pause
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(modifier = Modifier
            .wrapContentSize()
            .padding(end = 24.dp),
            onClick = {}){
            Icon(modifier = Modifier.size(20.dp),
                imageVector = Icons.Filled.Shuffle,
                contentDescription = "Shuffle")
        }
        IconButton(modifier= Modifier
            .wrapContentSize()
            .padding(end = 16.dp),
            onClick = {playerState.player.seekToPreviousMediaItem()}) {
            Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
        }
        FloatingActionButton (modifier= Modifier.wrapContentSize(),
            onClick = {
                with(playerState.player){
                    playWhenReady = !playWhenReady
                    playState = playWhenReady
                }
            }) {
            Icon(playPause, contentDescription = "Play and pause")
        }
        IconButton(modifier= Modifier
            .wrapContentSize()
            .padding(start = 16.dp),
            onClick = {playerState.player.seekToNextMediaItem()}) {
            Icon(Icons.Filled.SkipNext, contentDescription = "Next")
        }
        IconButton(modifier=Modifier
            .wrapContentSize()
            .padding(start = 24.dp),
            onClick = {}) {
            Icon(modifier=Modifier.size(20.dp),
                imageVector = Icons.Filled.Repeat,
                contentDescription = "Repeat mode")
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewComposable(){
    Box() {
        //Body()
    }
}