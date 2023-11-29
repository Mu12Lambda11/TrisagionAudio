package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE =1
    var songs= ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission()
        setContentView(R.layout.activity_main)
    }

    private fun permission() {
        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), REQUEST_CODE)
        }else{
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            songs= loadSongs(this)
        }
    }

    fun onRequestPermissionResult(requestCode:Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
        if(requestCode==REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), REQUEST_CODE)
                songs= loadSongs(this)
            }
        }
    }
    fun loadSongs(context: Context): ArrayList<Song> {
        val allSongs= ArrayList<Song>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection : Array<String> =
            arrayOf(
                MediaStore.Audio.Media.ALBUM,
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
        return allSongs

    }

    companion object{
        private var INSTANCE: MainActivity? = null
        fun initialize(){
            if(INSTANCE==null){
                INSTANCE= MainActivity()
            }
        }
        fun get():MainActivity{
            return INSTANCE?:
            throw IllegalStateException("MainActivity must be initialized")
        }
    }
}