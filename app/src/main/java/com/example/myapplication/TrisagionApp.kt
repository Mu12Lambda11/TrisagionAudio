package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class TrisagionApp: Application() {

    var songs= ArrayList<Song>()
     override fun onCreate() {
        super.onCreate()

        if(!permission()){
            requestPermission(this)
            return
        }
        songs=loadSongs(this)

        if(songs.size==0){
            Toast.makeText(this,"There's no music!", Toast.LENGTH_LONG)
        }
        //initialize after loading songs
        SongRepository.initialize(this, songs)


    }

    private fun permission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return result== PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(context: Context){
        val activity = context as Activity
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "A music player needs audio file permissions. Please allow access.", Toast.LENGTH_SHORT)
        }
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123 )
    }
    fun loadSongs(context: Context): ArrayList<Song> {
        val allSongs= ArrayList<Song>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection=
            arrayOf(
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST)
        val selection : String = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection,null,null)

        if(cursor!=null){
            while(cursor.moveToNext()){
                val newAlbum: String = cursor.getString(0)
                val newTitle: String = cursor.getString(1)
                val newDuration: String = cursor.getString(2)
                val newPath: String = cursor.getString(3)
                val newArtist: String = cursor.getString(4)


                val newSong= Song(newTitle,newPath,newDuration,newArtist,newAlbum)
                val file = File(newSong.path)

                var noTitleConflict=true
                var i=0
                while(i<allSongs.size&&!allSongs.isEmpty()){
                    if(allSongs.get(i).title==newTitle)
                        noTitleConflict=false
                    i++
                }

                if(file.exists() && noTitleConflict) {
                    allSongs.add(newSong)
                }
            }
            cursor.close()
        }
        return allSongs

    }


}



