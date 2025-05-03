package com.barryzeha.kmusic

import android.app.Application
import android.content.Context
import com.barryzeha.kmusic.common.MyPreferences

/****
 * Project KMusic
 * Created by Barry Zea H. on 22/04/25.
 * Copyright (c)  All rights reserved.
 ***/

class MainApp: Application() {
    companion object{
        var mPrefs: MyPreferences?=null
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mPrefs = MyPreferences(this)
    }
}