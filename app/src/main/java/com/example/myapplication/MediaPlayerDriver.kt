package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.lang.IllegalStateException


class MediaPlayerDriver constructor(context: Context, uri: Uri) {

    private var mediaPlayerInstance = MediaPlayer.create(context,uri)
    //boolean for if the last song accessed and the current song are he same
    private var prevSongTitle=""


    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayerInstance
    }

    fun setMediaPlayer(context: Context, uri: Uri){
        mediaPlayerInstance= MediaPlayer.create(context,uri)
    }
    fun setPrevSongTitle(string: String){
        prevSongTitle=string
    }
    fun getPrevSongTitle(): String{
        return prevSongTitle
    }

    //singleton behavior
    companion object{
        private var INSTANCE: MediaPlayerDriver? = null
        fun initialize(context: Context, uri: Uri) {
            if(INSTANCE==null){
                INSTANCE= MediaPlayerDriver(context, uri)
            }
        }
        fun get():MediaPlayerDriver{
            return INSTANCE?:
            throw IllegalStateException("MediaPlayerDriver must be initialized")
        }
    }
}
