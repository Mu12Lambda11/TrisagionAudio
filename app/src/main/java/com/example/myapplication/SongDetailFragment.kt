package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentSongDetailBinding
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class SongDetailFragment : Fragment(){
    private var _binding: FragmentSongDetailBinding? =null
    private val binding
        get()= checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args : SongDetailFragmentArgs by navArgs()

    private val songDetailViewModel: SongDetailViewModel by viewModels {
        SongDetailViewModelFactory(args.songPath)
    }
    lateinit var mediaPlayer: MediaPlayer
    var currentPosition by Delegates.notNull<Int>()
    var allSongs= ArrayList<Song>()
    var shuffleSongs =false
    var repeatSongs=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Instantiate and setup MediaPlayer
        initMediaPlayer(songDetailViewModel.song)
        mediaPlayer = MediaPlayerDriver.get().getMediaPlayer()
        currentPosition=MediaPlayerDriver.get().getMediaIndex()

        Log.d("SongDetailFragment", "Path is: ${args.songPath}")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSongDetailBinding.inflate(layoutInflater, container, false)

        //initialize song list
        allSongs=songDetailViewModel.songs
        //load the selected song
        loadSong(songDetailViewModel.song)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        startPlaying(songDetailViewModel.song,mediaPlayer)

        //Runnable object to parse the current song position
        this.activity?.runOnUiThread(object: Runnable{
            override fun run() {
                //Seekbar functionality
                binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if(mediaPlayer!=null&&fromUser){
                            mediaPlayer.seekTo(progress*1000)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })

                //Fill Out seekbar
                val currentPosition = mediaPlayer.currentPosition/1000
                val seekbarDuration = mediaPlayer.duration/1000
                binding.seekBar.progress = currentPosition
                binding.seekBar.max=seekbarDuration
                binding.currentSongTime.text = formattedTime(currentPosition)
                binding.songDuration.text = formattedTime(seekbarDuration)
                val handler = Handler(Looper.getMainLooper()).postDelayed(this, 1000)

            }

        })

        //Binding stuff
        binding.apply {
            //Button Setup
            playButton.setOnClickListener(){
                if(!mediaPlayer.isPlaying){
                    mediaPlayer.start()
                }
            }
            pauseButton.setOnClickListener(){
                if(mediaPlayer.isPlaying){
                    mediaPlayer.pause()
                }
            }
            skipNextButton.setOnClickListener(){v->
                playNextSong()
            }
            skipPrevButton.setOnClickListener {v->
                playPrevSong()

            }
            shuffleButton.setOnClickListener {

            }
            repeatButton.setOnClickListener {

            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                }
            }

        }

    }


    fun loadSong(song:Song?){
        binding.apply {

            if (song != null) {
                //set title
                songTitleDetail.setText(song.title)
                //set artist
                songArtistDetail.setText(song.artist)
                //set duration
                songDuration.setText(formattedTime(song.duration.toInt()))
                //set album art
                val image =SongHolder.getAlbumArt(song.path)
                if(image!=null){
                    context?.let { Glide.with(it).asBitmap().load(image).into(binding.detailedAlbumArt) }
                }else{
                    context?.let { Glide.with(it).asBitmap().load(R.drawable.ic_music).into(binding.detailedAlbumArt) }
                }


            }else{
                Toast.makeText(context, "Song pathing error!", Toast.LENGTH_SHORT)
            }
        }
    }

    fun playNextSong(){
        if(currentPosition==songDetailViewModel.songs.size-1){
            currentPosition+=1
            mediaPlayer.reset()
            loadSong(allSongs[currentPosition])
        }
    }
    fun shuffleOn(){
        //TODO implement shuffle stuff by making another songlist
    }

    fun playPrevSong(){
        if(currentPosition==0){
            return
        }
        currentPosition=-1
        mediaPlayer.reset()
        loadSong(allSongs[currentPosition])
    }

    //TODO apply to class later
    fun pausePlay(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }else{
            mediaPlayer.start()
        }
    }
    fun startPlaying(song: Song?, mediaPlayer:MediaPlayer){
        if(song!=null){
            mediaPlayer.start()
        }else{
            Toast.makeText(context, "Song pathing error!", Toast.LENGTH_SHORT)
        }
    }

    fun initMediaPlayer(song: Song?){
        if(song!=null){
            //Initialize the media player
            val uri = Uri.parse(song.path)
            context?.let { MediaPlayerDriver.initialize(it, uri) }
        }else{
            Toast.makeText(context, "Song pathing error!", Toast.LENGTH_SHORT)
        }
    }

    companion object{
        //Time Formatting
        fun formattedTime(currentPosition: Int):String {
            var totalOut=""
            var totalNew=""
            val seconds = (currentPosition%60).toString()
            val minutes = (currentPosition/60).toString()
            totalOut= minutes+":"+seconds
            totalNew= minutes+":"+"0"+seconds
            if(seconds.length==1){
                return totalNew
            }else{
                return totalOut
            }
        }
    }

}