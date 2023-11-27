package com.example.myapplication

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongListAdapter (
    private val songs: List<Song>,
    private val onSongClicked: (songTitle:String)-> Unit):RecyclerView.Adapter<SongHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {

    }

    override fun getItemCount()=songs.size

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val songs = songs[position]
        holder.bind(song, onSongClicked)
    }

}
}

class SongHolder(songView: View) : RecyclerView.ViewHolder(songView){

    val imageView: ImageView = songView.findViewById(R.id.album_art)
    val titleView: TextView = songView.findViewById(R.id.song_title)
    val lengthView: TextView = songView.findViewById(R.id.song_length)

    fun bind(song:Song, onSongClicked: (songTitle: String) -> Unit){

    }
}