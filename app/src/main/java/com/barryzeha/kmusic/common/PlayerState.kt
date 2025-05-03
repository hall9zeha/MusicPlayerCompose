package com.barryzeha.kmusic.common

import android.media.session.MediaController
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

/****
 * Project KMusic
 * Created by Barry Zea H. on 27/04/25.
 * Copyright (c)  All rights reserved.
 ***/

fun Player.state():PlayerState{
    val instance = PlayerStateImpl.getInstance(this)
    return PlayerStateImpl(this)
}

interface PlayerState{
    val player: Player
    val currentMediaItem: MediaItem?
    val mediaMetadata: MediaMetadata
    val timeLine: Timeline
    var mediaItemIndex:Int
    val currentPosition: Long
    @get:Player.State
    val playbackState: Int
    val isPlaying: Boolean
    fun attachListener()
    fun dispose()
    fun currentPositionCheck()

}
internal class PlayerStateImpl(override val player: Player): PlayerState{

    override var currentMediaItem: MediaItem? by mutableStateOf(player.currentMediaItem)
        private set
    override var mediaMetadata: MediaMetadata by mutableStateOf(player.mediaMetadata)
        private set
    override var timeLine: Timeline by mutableStateOf(player.currentTimeline)
        private set
    override var mediaItemIndex: Int by mutableStateOf(player.currentMediaItemIndex)
        set
    override var playbackState: Int by mutableIntStateOf(player.playbackState)
        private set
    override var isPlaying: Boolean by mutableStateOf(player.isPlaying)
        private set
    override var currentPosition: Long by mutableStateOf(player.currentPosition)
        private set


    private val listener = object:Player.Listener{
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            this@PlayerStateImpl.currentMediaItem = mediaItem
        }
        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            this@PlayerStateImpl.mediaMetadata = mediaMetadata
        }
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            this@PlayerStateImpl.timeLine = timeline
            this@PlayerStateImpl.mediaItemIndex = player.currentMediaItemIndex
        }
        override fun onPlaybackStateChanged(@Player.State playbackState: Int) {
            this@PlayerStateImpl.playbackState = playbackState
        }
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            this@PlayerStateImpl.isPlaying = isPlaying
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            this@PlayerStateImpl.mediaItemIndex = player.currentMediaItemIndex
        }

    }
    /*init{
        player.addListener(listener)
    }*/
    override fun attachListener() {
        player.addListener(listener)
    }

    override fun dispose() {
        player.removeListener(listener)
    }
    override fun currentPositionCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            //if(isPlaying){
                while (true){
                    withContext(Dispatchers.Main) {
                        currentPosition = player.currentPosition
                    }
                    delay(500)
                    Log.e("CURRENT_POS", currentPosition.toString())
                }
            //}
        }
    }

    companion object{
        private var instance: PlayerStateImpl?=null

        fun getInstance(player: Player): PlayerState?{
            if(instance==null){
                instance= PlayerStateImpl(player)
            }
            return instance
        }
    }
}