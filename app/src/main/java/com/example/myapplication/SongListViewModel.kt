package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongListViewModel() : ViewModel(){



    private val songRepository=SongRepository.get()

    var songs= ArrayList<Song>()



    init{
        viewModelScope.launch {
            songs=songRepository.sendSongs()
        }
    }
}