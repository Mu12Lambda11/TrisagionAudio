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

    fun getSongs(context: Context): ArrayList<Song> {
        val allSongs= ArrayList<Song>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection : Array<String> =
            arrayOf(MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST)

        val cursor: Cursor? = context.contentResolver.query(uri, projection, null,null,null)

        if(cursor!=null){
            while(cursor.moveToNext()){
                val newAlbum: String = cursor.getString(0)
                val newTitle: String = cursor.getString(1)
                val newDuration: String = cursor.getString(2)
                val newPath: String = cursor.getString(3)
                val newArtist: String = cursor.getString(4)


                val newSong= Song(newTitle,newPath,newDuration,newArtist,newAlbum)
                //log to check
                Log.e("path "+newPath,"Title:  "+newTitle,)
                allSongs.add(newSong)
            }
            cursor.close()
        }
        val flex :Flow<ArrayList<Song>> =

    }

    suspend fun getSong(){

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