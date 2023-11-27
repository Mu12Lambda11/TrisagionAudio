package com.example.myapplication

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

private const val DATABASE_NAME= "song-database"
class SongRepository private constructor (context: Context,
  private val coroutineScope: CoroutineScope = GlobalScope
){
    private val database: SongDatabase = Room.databaseBuilder(
      context.applicationContext,

    )

}