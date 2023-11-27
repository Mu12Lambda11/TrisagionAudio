package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SongDetailViewModel : ViewModel(){
    private val _songs: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())
    val songs: StateFlow<List<Song>>
        get()=_songs.asStateFlow()

    init{

    }

}