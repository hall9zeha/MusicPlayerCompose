package com.barryzeha.kmusic.data

import android.R

/****
 * Project KMusic
 * Created by Barry Zea H. on 22/04/25.
 * Copyright (c)  All rights reserved.
 ***/

data class SongEntity(
    val idSong: Long=0,
    val title:String="",
    val artist:String="",
    val album: String="",
    val albumArtist:String="",
    val genre:String="",
    val year:String="",
    val duration: Long=0,
    val bitrate:Long=0,
    val pathFile:String="",
    val artistId:Int=0,
    val albumId:Long=0,
    val size:Long=0,

)
