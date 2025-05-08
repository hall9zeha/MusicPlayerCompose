package com.barryzeha.kmusic.common

import android.content.Context
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

/****
* Project KMusic
* Created by Barry Zea H. on 4/05/25.
* Copyright (c)  All rights reserved.
***/


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
    var isDraggingProgressSlider: Boolean
    fun registerListener()
    fun dispose()
    fun close()
    fun startTrackingPlaybackPosition(context: Context)

}
internal class PlayerStateImpl(): PlayerState{
    private var enableLoop=false
    override var player: Player by mutableStateOf(playerInstance)
        set
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
    override var isDraggingProgressSlider: Boolean by mutableStateOf(false)
        set
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
    override fun registerListener() {
        player.addListener(listener)
    }

    override fun dispose() {
        player.removeListener(listener)
    }
    override fun close() {

    }
    override fun startTrackingPlaybackPosition(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // Para sincroizar la nueva posición de desplazamiento del slider con la vista y no haya cambios erráticos
            // al mover el thumb y el desplazamiento sea más suave
            val frameTime = (1f / context.display.refreshRate).toDouble().milliseconds

            while (isActive) {
                withContext(Dispatchers.Main) {
                    val currentPosition = player.currentPosition
                    if (!isDraggingProgressSlider) {
                        this@PlayerStateImpl.currentPosition = currentPosition
                    }
                }
                delay(frameTime)
            }
        }
    }

    companion object{
        private var instance: PlayerStateImpl?=null
        private lateinit var playerInstance: Player
        fun getInstance(player: Player): PlayerState?{
            playerInstance=player
            if(instance==null){
                instance= PlayerStateImpl()
            }
            return instance
        }
    }
}