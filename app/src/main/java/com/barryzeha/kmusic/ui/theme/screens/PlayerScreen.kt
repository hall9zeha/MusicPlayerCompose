package com.barryzeha.kmusic.ui.theme.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryzeha.kmusic.R
import org.intellij.lang.annotations.JdkConstants
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

/****
 * Project KMusic
 * Created by Barry Zea H. on 17/04/25.
 * Copyright (c)  All rights reserved.
 ***/
@Composable
fun PlayerScreen() {
    Box(Modifier.fillMaxSize().padding(8.dp)){
        Body()
    }
}

@Composable
fun Body(){
    Column(modifier= Modifier.fillMaxSize().padding(top=48.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        CoverAlbumArt()
        SongDescription()
        Seekbar()
        PlayerControls()
    }
}
@Composable
fun CoverAlbumArt(){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Card(
        modifier = Modifier.padding(top = 48.dp)
            /*.aspectRatio(1f)*//* aspect ratio square 1:1 */
            .size(screenWidth * 0.8f) /* For control the square size we choose this option */
            /*.fillMaxWidth(0.8f)*/,
        elevation= CardDefaults.cardElevation(4.dp),
        border=_root_ide_package_.androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent),
        shape = RoundedCornerShape(16.dp),

    ) {
        Image(
            modifier=Modifier.fillMaxSize(),
            painter = painterResource(id= R.drawable.ic_launcher_background),
            contentDescription = "Cover art")
    }
}

@Composable
fun SongDescription(){
    Column(modifier= Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier=Modifier.basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
            text="Song title, Song title, Song title, song title, Song title, Song title",
            fontSize = 18.sp)
        Text(modifier= Modifier.basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
            text = "Artist", fontSize = 16.sp)
        Text(modifier = Modifier.basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
            text="Album", fontSize = 16.sp)

    }
}
@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Seekbar(){
    var sliderValue by remember { mutableFloatStateOf(0f) }
    val interactionsource = MutableInteractionSource()
    Column(modifier=Modifier.fillMaxWidth(). padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(modifier= Modifier.fillMaxWidth()
            ,value = sliderValue,
            onValueChange = { newValue ->
                sliderValue = newValue
            },
            valueRange = 0f..1000f, steps = 999,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionsource,
                    thumbSize = DpSize(24.dp,24.dp)
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
            Text(modifier=Modifier.align(Alignment.CenterEnd).padding(end=12.dp),
                text = "00:00",
                fontSize = 12.sp
                )
        }
    }
}

@Composable
fun PlayerControls(){
    var playState by remember { mutableStateOf(false) }
    val playPause =if(!playState) Icons.Filled.PlayArrow else Icons.Filled.Pause
    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(modifier = Modifier.wrapContentSize().padding(end = 24.dp),
            onClick = {}){
            Icon(modifier = Modifier.size(20.dp),
                imageVector = Icons.Filled.Shuffle,
                contentDescription = "Shuffle")
        }
        IconButton(modifier= Modifier.wrapContentSize().padding(end = 16.dp),
            onClick = {}) {
            Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
        }
        FloatingActionButton (modifier= Modifier.wrapContentSize(),
            onClick = {
                playState = !playState
            }) {
            Icon(playPause, contentDescription = "Play and pause")
        }
        IconButton(modifier= Modifier.wrapContentSize().padding(start = 16.dp),
            onClick = {}) {
            Icon(Icons.Filled.SkipNext, contentDescription = "Next")
        }
        IconButton(modifier=Modifier.wrapContentSize().padding(start = 24.dp),
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
        Body()
    }
}