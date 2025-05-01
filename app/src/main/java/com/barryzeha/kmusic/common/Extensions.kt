package com.barryzeha.kmusic.common

import android.icu.text.Normalizer2
import androidx.media3.common.MediaItem
import androidx.media3.common.Player

/****
 * Project KMusic
 * Created by Barry Zea H. on 22/04/25.
 * Copyright (c)  All rights reserved.
 ***/

fun String.trimAndNormalize(): String{
    return Normalizer2.getNFCInstance().normalize(this.trim())
}
internal val Player.currentMediaItems: List<MediaItem> get() {
    return List(mediaItemCount, ::getMediaItemAt)
}
fun Player.updatePlaylist(mediaItems: List<MediaItem>){
    val oldMediaItems = currentMediaItems.map{it.mediaId}.toSet()
    val itemsToAdd = mediaItems.filterNot { item->item.mediaId in oldMediaItems }
    addMediaItems(itemsToAdd)
}
fun Player.playMediaAtIndex(index: Int) {
    if (currentMediaItemIndex == index)
        return
    seekToDefaultPosition(index)
    playWhenReady = true
    prepare()
}