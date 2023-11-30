package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ListItemSongBinding

class SongListAdapter (
    private val context: Context,
    private val songs: ArrayList<Song>,
    private val onSongClicked: (songPath:String)-> Unit):RecyclerView.Adapter<SongHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ListItemSongBinding.inflate(inflater,parent, false)
        return SongHolder(binding)
    }

    override fun getItemCount()=songs.size

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val songData = songs[position]
        //MediaPlayerDriver.get().currentIndex=position
        holder.bind(songData, onSongClicked, context)
    }



}


class SongHolder(private val binding: ListItemSongBinding) : RecyclerView.ViewHolder(binding.root){


    //val mediaPlayer = MediaPlayerDriver.get().getMediaPlayer()
    fun bind(song:Song, onSongClicked: (songTitle: String) -> Unit, context: Context){
        binding.songTitle.text=song.title
        binding.songLength.text=SongDetailFragment.formattedTime(song.duration.toInt()/1000)

        //album art retrieval
        val image = getAlbumArt(song.path)
        if(image!=null){
            Glide.with(context).asBitmap().load(image).into(binding.albumArt)

        }else{
            Glide.with(context).asBitmap().load(R.drawable.ic_music).into(binding.albumArt)
        }

        //click listener
        binding.root.setOnClickListener(){
            onSongClicked(song.path)
            //mediaPlayer.reset()
        }
    }

    companion object{
        fun getAlbumArt(path: String): ByteArray? {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(path)
            val obtainedArt =retriever.embeddedPicture
            retriever.release()
            return obtainedArt
        }
    }

}