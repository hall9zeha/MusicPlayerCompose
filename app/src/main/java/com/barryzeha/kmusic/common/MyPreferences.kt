package com.barryzeha.kmusic.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


/****
 * Project KMusic
 * Created by Barry Zea H. on 1/05/25.
 * Copyright (c)  All rights reserved.
 ***/
private const val PREFERENCES_FILE_NAME="myPreferences"
private const val CURRENT_INDEX_SONG = "currentIndexSong"
private const val CURRENT_SONG_DURATION = "currentSongDuration"
class MyPreferences(context: Context) {
    var mPrefs: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    var currentIndexSaved: Int
        get() = mPrefs.getInt(CURRENT_INDEX_SONG,-1)
        set(value) = mPrefs.edit { putInt(CURRENT_INDEX_SONG, value) }
    var currentSongDuration:Long
        get() = mPrefs.getLong(CURRENT_SONG_DURATION,0)
        set(value) = mPrefs.edit{ putLong(CURRENT_SONG_DURATION, value)}

}