package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongDetailViewModel(songPath : String) : ViewModel(){
    private val songRepository=SongRepository.get()
    private var song :Song? = null

    init{
        viewModelScope.launch {
            song=songRepository.getSong(songPath)
        }
    }

}

class SongDetailViewModelFactory( private val songPath : String):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongDetailViewModel((songPath)) as T
    }
}