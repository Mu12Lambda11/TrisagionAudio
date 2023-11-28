package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemSongBinding

class SongListAdapter (
    private val songs: ArrayList<Song>,
    private val onSongClicked: (songTitle:String)-> Unit):RecyclerView.Adapter<SongHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemSongBinding.inflate(inflater,parent, false)
        return SongHolder(binding)
    }

    override fun getItemCount()=songs.size

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, onSongClicked)
    }

}


class SongHolder(private val binding: ListItemSongBinding) : RecyclerView.ViewHolder(binding.root){


    fun bind(song:Song, onSongClicked: (songTitle: String) -> Unit){
        binding.songTitle.text=song.title
        binding.songLength.text=song.duration
        //binding.albumArt.setImageBitmap(song.albumArt)

        binding.root.setOnClickListener(){
            onSongClicked(song.title)
        }
    }
}