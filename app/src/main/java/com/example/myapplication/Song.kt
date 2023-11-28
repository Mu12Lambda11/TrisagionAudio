package com.example.myapplication

import android.graphics.Bitmap
import android.media.Image
import java.util.Date
import java.util.UUID

data class Song (
        var title : String,
        val path: String,
        val duration : String,
        val artist: String,
        val album: String
)

