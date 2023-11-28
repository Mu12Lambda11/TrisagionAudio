package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongListViewModel(application: Application) : AndroidViewModel(application){


    private val songRepository=SongRepository.get()

    //private val _songs :MutableStateFlow<ArrayList<Song>> =MutableStateFlow(ArrayList<Song>())

    val songs= ArrayList<Song>()

    private val context = getApplication<Application>().applicationContext


    init{
        viewModelScope.launch {
            songRepository.getSongs(context)
        }
    }
}