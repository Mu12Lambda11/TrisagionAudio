package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun getSongs(): Flow<List<Song>>

    @Query("SELECT * FROM song WHERE title=(:title)")
    suspend fun getSong(title: String): Song

    @Update
    suspend fun updateSong(song: Song)

    @Insert
    suspend fun addSong(song: Song)
}