package com.barryzeha.kmusic.common

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
    return PlayerStateImpl(this)
}

interface PlayerState{
    val player: Player
    val currentMediaItem: MediaItem?
    val mediaMetadata: MediaMetadata
    val timeLine: Timeline
    val mediaItemIndex:Int
    val currentPosition: Long
    @get:Player.State
    val playbackState: Int
    val isPlaying: Boolean
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
        private set
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

    }
    init{
        player.addListener(listener)
    }
    override fun dispose() {
        player.removeListener(listener)
    }
    override fun currentPositionCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            if(isPlaying){
                while (true){
                    withContext(Dispatchers.Main) {
                        currentPosition = player.currentPosition
                    }
                    delay(500)
                }
            }
        }
    }
}