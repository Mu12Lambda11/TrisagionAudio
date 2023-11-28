package com.example.myapplication

import android.app.Application

class TrisagionApp: Application() {
    override fun onCreate(){
        super.onCreate()
        SongRepository.initialize(this)
    }
}