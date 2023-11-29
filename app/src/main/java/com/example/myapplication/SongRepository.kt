package com.example.myapplication

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException

class SongRepository private constructor (context: Context){

    val _main = MainActivity.get()
    val songs =_main.songs

    suspend fun sendSongs(): ArrayList<Song> {
        return songs
    }

    //singleton behavior
    companion object{
        private var INSTANCE: SongRepository? = null
        fun initialize(context: Context){
            if(INSTANCE==null){
                INSTANCE= SongRepository(context)
            }
        }
        fun get():SongRepository{
            return INSTANCE?:
            throw IllegalStateException("SongRepository must be initialized")
        }
    }
}