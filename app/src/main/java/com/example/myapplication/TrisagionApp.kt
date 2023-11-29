package com.example.myapplication

import android.app.Application

class TrisagionApp: Application() {

    override fun onCreate(){
        super.onCreate()
        MainActivity.initialize()
        SongRepository.initialize(this)
    }
}