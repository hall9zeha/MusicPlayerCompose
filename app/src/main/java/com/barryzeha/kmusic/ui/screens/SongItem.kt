package com.barryzeha.kmusic.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barryzeha.kmusic.R

/****
 * Project KMusic
 * Created by Barry Zea H. on 18/04/25.
 * Copyright (c)  All rights reserved.
 ***/

@Composable
fun SongItem(){
    Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1f).padding(8.dp)) {
            Card(
                modifier = Modifier.size(60.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "Album cover"
                )
            }
            Column (modifier = Modifier.padding(start = 8.dp) ) {
                Text(text = "Title of song", fontSize = 16.sp)
                Text(text= "Artist")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewItem(){
    SongItem()
}