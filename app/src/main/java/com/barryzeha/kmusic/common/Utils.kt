package com.barryzeha.kmusic.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/****
 * Project KMusic
 * Created by Barry Zea H. on 20/04/25.
 * Copyright (c)  All rights reserved.
 ***/

fun checkPermissions(context:Context, permissionsList: List<String>, granted:(Boolean, List<Pair<String,Boolean>>)-> Unit){
    val permissionsGranted: MutableList<Pair<String, Boolean>> = mutableListOf()
    var grantedCount =0

    permissionsList.forEach {permission->
        if(ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED){
            permissionsGranted.add(Pair(permission, true))
            grantedCount++
        }else{
            permissionsGranted.add(Pair(permission,false))
            granted((grantedCount == permissionsList.size), permissionsGranted)
            return
        }
    }
    granted((grantedCount == permissionsList.size), permissionsGranted)

}