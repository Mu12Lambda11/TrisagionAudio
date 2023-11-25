package com.example.myapplication
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID
@Entity
data class Song (
    @PrimaryKey val title : String,
        val length : String,
        val artist: String,
        val album: String,
)