package com.example.myapplication

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException

class SongRepository private constructor (context: Context, givenSongsList: ArrayList<Song>){

     private val songs =givenSongsList

     fun sendSongs(): ArrayList<Song> {
        return songs
    }

    fun getSong(path:String): Song? {
        var returnSong: Song? = null
         for(thisSong:Song in songs){
             if(thisSong.path==path){
                 returnSong=thisSong
             }
         }
        return returnSong
    }

    //singleton behavior
    companion object{
        private var INSTANCE: SongRepository? = null
        fun initialize(context: Context, givenSongsList: ArrayList<Song>){
            if(INSTANCE==null){
                INSTANCE= SongRepository(context, givenSongsList)
            }
        }
        fun get():SongRepository{
            return INSTANCE?:
            throw IllegalStateException("SongRepository must be initialized")
        }
    }
}