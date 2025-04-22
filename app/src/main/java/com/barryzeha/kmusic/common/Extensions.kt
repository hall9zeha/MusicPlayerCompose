package com.barryzeha.kmusic.common

import android.icu.text.Normalizer2

/****
 * Project KMusic
 * Created by Barry Zea H. on 22/04/25.
 * Copyright (c)  All rights reserved.
 ***/

fun String.trimAndNormalize(): String{
    return Normalizer2.getNFCInstance().normalize(this.trim())
}