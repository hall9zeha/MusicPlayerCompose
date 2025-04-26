package com.barryzeha.kmusic.ui.screens

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Audio.Media
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.util.Size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryzeha.kmusic.R
import com.barryzeha.kmusic.data.SongEntity
import com.barryzeha.kmusic.ui.theme.Typography
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

/****
 * Project KMusic
 * Created by Barry Zea H. on 18/04/25.
 * Copyright (c)  All rights reserved.
 ***/

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SongItem(song: SongEntity, onItemClick:(song: SongEntity)->Unit){

    val bitmap = loadArtwork(LocalContext.current,song.idSong)
    Row(modifier=Modifier.fillMaxWidth().clickable(
        onClick = {onItemClick(song)},
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple()
    ), verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1f).padding(8.dp)) {
            Card(
                modifier = Modifier.size(48.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                bitmap?.let {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Album cover",
                        contentScale = ContentScale.Crop
                    )
                }?:run{
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "Album cover",
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column (modifier = Modifier.padding(start = 8.dp) ) {
                Text(text = song.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = Typography.bodyMedium)
                Text(text= song.artist, maxLines = 1, overflow = TextOverflow.Ellipsis, style = Typography.bodySmall)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewItem(){
    SongItem(SongEntity()){}
}
private val cachedScreenSize = AtomicInteger(0)
@RequiresApi(Build.VERSION_CODES.R)
fun loadArtwork(context: Context, id: Long, sizeLimit: Int? = null): Bitmap? {
    try {
        val thumbnailSize = sizeLimit
                ?: cachedScreenSize.get().takeIf { it > 0 }
                ?: run {
                    val screenSize = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                            .maximumWindowMetrics
                            .bounds
                    val limit = min(screenSize.width(), screenSize.height()).coerceAtLeast(256)
                    cachedScreenSize.set(limit)
                    limit
                }

        val bitmap = context.contentResolver.loadThumbnail(
                ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id),
                Size(thumbnailSize, thumbnailSize),
                null,
            )
        return bitmap
    } catch (ex: Exception) {
        return null
    }
}

