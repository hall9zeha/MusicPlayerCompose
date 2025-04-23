package com.barryzeha.kmusic

import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.barryzeha.kmusic.common.checkPermissions
import com.barryzeha.kmusic.ui.screens.PlayListScreen
import com.barryzeha.kmusic.ui.theme.KMusicTheme
import com.barryzeha.kmusic.ui.screens.PlayerScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var launcherPermission: ActivityResultLauncher<String>

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityResultForPermission()

        setContent {
            LaunchedEffect(true) {
                initCheckPermissions()
            }
           /* val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = object:LifecycleEventObserver{
                    override fun onStateChanged(
                        source: LifecycleOwner,
                        event: Lifecycle.Event
                    ) {
                        if (event == Lifecycle.Event.ON_START) {

                            initCheckPermissions()
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }*/
            KMusicTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding.calculateTopPadding()
                    PlayListScreen()
                }
            }
        }
       // initCheckPermissions()
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
}

