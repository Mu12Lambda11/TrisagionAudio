package com.example.myapplication

import android.graphics.Bitmap
import android.media.Image
import java.util.Date
import java.util.UUID

data class Song (
        val title : String,
        val length : String,
        val artist: String,
        val album: String,
        val albumArt: Bitmap
)