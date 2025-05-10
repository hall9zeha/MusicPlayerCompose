package com.barryzeha.kmusic

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.session.MediaController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.barryzeha.kmusic.common.PlayerState
import com.barryzeha.kmusic.common.checkPermissions
import com.barryzeha.kmusic.ui.components.MiniPlayerView
import com.barryzeha.kmusic.ui.navigation.Routes
import com.barryzeha.kmusic.ui.screens.PlayListScreen
import com.barryzeha.kmusic.ui.screens.PlayerScreen
import com.barryzeha.kmusic.ui.theme.KMusicTheme
import com.barryzeha.kmusic.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var launcherPermission: ActivityResultLauncher<String>
    private lateinit var navController:NavHostController

    private val permissionsList: MutableList<String> = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        mutableListOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.READ_MEDIA_AUDIO
        )
    }else{
        mutableListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityResultForPermission()

        setContent {
            LaunchedEffect(true) {
                initCheckPermissions()
            }
            val context = LocalContext.current
            val lifecycle = LocalLifecycleOwner.current.lifecycle

            var mediaControllerInstance by remember{mutableStateOf(mainViewModel.mediaController)}
            val mediaController  by mainViewModel.controller
            val playerScreenIsActive by mainViewModel.playerScreenIsActive.collectAsState()
            val playerState by  mainViewModel.playerState.observeAsState()

            // For bottom sheet
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            var openBottomSheet by rememberSaveable { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()
            val hasInitialized = remember{mutableStateOf(false)}
            val setUpPlayer by mainViewModel.isPlayerSetup.collectAsStateWithLifecycle()

            DisposableEffect(lifecycle) {
                val observer = LifecycleEventObserver{_, event->
                    when(event){
                        Lifecycle.Event.ON_STOP->{
                            MainApp.mPrefs?.currentSongDuration = playerState?.currentPosition!!
                        }
                        Lifecycle.Event.ON_START->{
                            playerState?.registerListener()
                            mediaControllerInstance.initialize()
                        }
                        Lifecycle.Event.ON_DESTROY->{
                            mediaControllerInstance.release()
                            playerState?.close()
                        }
                        else->{}
                    }
                }
                lifecycle.addObserver(observer)
                onDispose { lifecycle.removeObserver(observer) }
            }
            DisposableEffect(key1 = playerState) {
                mediaController?.run {
                    //playerState = state
                    playerState?.registerListener()
                    if(!hasInitialized.value){
                        playerState?.let{player->
                            val newIndex = if(MainApp.mPrefs?.currentIndexSaved!! > -1) MainApp.mPrefs?.currentIndexSaved!! else 0
                            val currentProgressDuration= MainApp.mPrefs?.currentSongDuration
                            player.mediaItemIndex =newIndex
                            player.player.seekTo(newIndex,currentProgressDuration!!)
                            player.player.prepare()
                        }

                    }
                    hasInitialized.value=true
                    mainViewModel.setUpPlayer()
                }
                onDispose {
                    playerState?.dispose()
                }
            }

            KMusicTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    innerPadding.calculateTopPadding()
                    SetupNavigation(mediaController)
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        Log.e("PLAY_STADO", playerScreenIsActive.toString())
                        if (playerState !=null && !playerScreenIsActive) {
                            MiniPlayerView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .align(Alignment.BottomCenter)
                                    .clickable {
                                        coroutineScope.launch {
                                            mainViewModel.setPlayerScreenVisibility(true)
                                            navController.navigate(Routes.Player.route)
                                            //sheetState.expand()
                                            //openBottomSheet = true
                                        }
                                    },
                                playerState = playerState!!
                            )
                        }
                    }
                }
            }

        }
    }
    private fun activityResultForPermission(){
        launcherPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted->
              initCheckPermissions()
        }
    }
    private fun initCheckPermissions(){
        checkPermissions(this,permissionsList) {isAllGranted,permissions->
            if(!isAllGranted){
                permissions.forEach { (permission, isGranted)->
                    if(!isGranted) launcherPermission.launch(permission)
                }
            }else{
            }
        }
    }
    @Composable
    fun SetupNavigation(mediaController: MediaController?){
        navController = rememberNavController()
        NavHost(navController, startDestination=Routes.Playlist.route){
            composable(Routes.Playlist.route){
                PlayListScreen(mediaController, navController = navController)
            }
            composable(Routes.Player.route){
                PlayerScreen(mainViewModel = mainViewModel, navController = navController)
            }
        }
    }
}

