package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.lang.IllegalStateException


class MediaPlayerDriver constructor(context: Context, uri: Uri) {

    private val mediaPlayerInstance = MediaPlayer.create(context,uri)
    var currentIndex = -1

    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayerInstance
    }
    fun getMediaIndex():Int{
        return currentIndex
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
