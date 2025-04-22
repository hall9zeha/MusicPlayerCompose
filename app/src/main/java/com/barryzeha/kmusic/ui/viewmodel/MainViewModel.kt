package com.barryzeha.kmusic.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryzeha.kmusic.common.scanTracks
import com.barryzeha.kmusic.data.SongEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/****
 * Project KMusic
 * Created by Barry Zea H. on 22/04/25.
 * Copyright (c)  All rights reserved.
 ***/

class MainViewModel(private val application: Application): AndroidViewModel(application) {
    private var _songsList: MutableStateFlow<List<SongEntity>> = MutableStateFlow(listOf())
    val songsList: StateFlow<List<SongEntity>> = _songsList

    fun scanSongs(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _songsList.value = scanTracks(application.applicationContext)!!
            }
        }
    }
}