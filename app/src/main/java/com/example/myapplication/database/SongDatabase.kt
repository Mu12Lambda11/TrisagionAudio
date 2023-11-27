package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.Song

@Database(entities = [Song::class], version=1)

abstract class SongDatabase : RoomDatabase(){

}