package com.barryzeha.kmusic.common

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.barryzeha.kmusic.MainApp
import com.barryzeha.kmusic.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

/****
 * Project KMusic
 * Created by Barry Zea H. on 26/04/25.
 * Copyright (c)  All rights reserved.
 ***/

@Composable
fun rememberManagedMediaController(lifecycle:Lifecycle = LocalLifecycleOwner.current.lifecycle):State<MediaController?>{

    val appContext = LocalContext.current.applicationContext
    val controllerManager = remember { MediaControllerManager.getInstance(appContext) }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver{_,event->
            when(event){
                Lifecycle.Event.ON_START->{controllerManager.initialize()}
                Lifecycle.Event.ON_STOP->{controllerManager.release()}
                else->{}
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }

    }
    return controllerManager.controller
}

@Stable
internal class MediaControllerManager private constructor(context: Context): RememberObserver{
    private val appContext = context.applicationContext
    private var factory: ListenableFuture<MediaController>? = null
    var controller = mutableStateOf<MediaController?>(null)

    init {
        initialize()
    }

    internal fun initialize(){
        if(factory == null || factory?.isDone == true){
            factory = MediaController.Builder(
                appContext,
                SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
            ).buildAsync()
        }

        factory?.addListener({controller.value = factory?.let{if(it.isDone) it.get() else null}}, MoreExecutors.directExecutor())
    }
    internal fun release(){
        factory?.let{
            MediaController.releaseFuture(it)
            controller.value = null
        }
        factory = null
    }
    override fun onAbandoned() {release()}
    override fun onForgotten() {release() }
    override fun onRemembered() {}
    companion object{
        @Volatile
        private var instance: MediaControllerManager? = null

        fun getInstance(context: Context): MediaControllerManager{
            return instance?:synchronized(this) {
                instance?: MediaControllerManager(context).also { instance = it }
            }
        }
    }
}