package com.example.myapplication

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MixerViewModel:ViewModel() {
    private val mediaDriver = SongDetailFragment.getMediaPlayerDriver()
    var mediaPlayer = MediaPlayer()

    init {
        viewModelScope.launch {
            mediaPlayer=mediaDriver.getMediaPlayer()
        }
    }
}