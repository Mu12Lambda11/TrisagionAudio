package com.example.myapplication

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
    var currentSongPosition by Delegates.notNull<Int>()
    var allSongs= ArrayList<Song>()
    var backupSongList = ArrayList<Song>()
    var shuffleSongs =false
    var repeatSongs=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Instantiate and setup MediaPlayer
        initMediaPlayer(songDetailViewModel.song)
        //initialize song list
        allSongs=songDetailViewModel.songs

        mediaPlayer = MediaPlayerDriver.get().getMediaPlayer()

        var i = 0;
        while(songDetailViewModel.song!=allSongs.get(i)){
            i++
        }
        currentSongPosition=i;


        Log.d("SongDetailFragment", "Path is: ${args.songPath}")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSongDetailBinding.inflate(layoutInflater, container, false)


        //load the selected song
        loadSongDetails(songDetailViewModel.song)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)


        if(!mediaPlayer.isPlaying){
            startPlaying(songDetailViewModel.song,mediaPlayer)
        }else if(songDetailViewModel.song!!.title!=MediaPlayerDriver.get().getPrevSongTitle()){
            //if the mediaplayer is playing and there is a song difference
            mediaPlayer.reset()
            refreshMediaPlayer(songDetailViewModel.song)
            mediaPlayer = MediaPlayerDriver.get().getMediaPlayer()
            startPlaying(songDetailViewModel.song,mediaPlayer)
        }else{
            //if it's the same song already playing, do nothing
        }



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

                fillSeekbar(this)

            }

        })

        //Binding stuff
        binding.apply {
            //Button Setup
            //TODO: Combine play and pause
            playPauseButton.setOnClickListener(){
                pausePlay()
            }

            skipNextButton.setOnClickListener {
                playNextSong()
            }


            skipPrevButton.setOnClickListener {
                playPrevSong()
            }
            shuffleButton.setOnClickListener {
                if(!shuffleSongs){
                    //on
                    shufflePlaylist()
                }else{
                    //off
                    restorePlayList()
                }

            }
            repeatButton.setOnClickListener {
                if(!repeatSongs){
                    repeatSongs=true
                    binding.repeatButton.setImageResource(R.drawable.ic_repeat_on)
                }else{
                    repeatSongs=false
                    binding.repeatButton.setImageResource(R.drawable.ic_repeat_off)
                }
            }

            backButton.setOnClickListener(){
                closeFragment()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){

                }
            }

        }

    }


    private fun shufflePlaylist() {
        backupSongList.addAll(allSongs)
        allSongs.shuffle()
        shuffleSongs=true
        binding.shuffleButton.setImageResource(R.drawable.ic_shuffle_on)
    }
    private fun restorePlayList(){
        allSongs.clear()
        allSongs.addAll(backupSongList)
        backupSongList.clear()

        //reassess current index
        var i = 0;
        while(songDetailViewModel.song!=allSongs.get(i)){
            i++
        }
        currentSongPosition=i;

        shuffleSongs=false
        binding.shuffleButton.setImageResource(R.drawable.ic_shuffle_off)
    }

    private fun closeFragment(){
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerDriver.get().setPrevSongTitle(songDetailViewModel.song!!.title)
    }


    private fun loadSongDetails(song:Song?){
        binding.apply {

            if (song != null) {
                //set title
                songTitleDetail.text = song.title
                //set artist
                songArtistDetail.text = song.artist
                //set duration
                songDuration.text = formattedTime(song.duration.toInt())
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

    fun fillSeekbar(runner :Runnable){
        //Fill Out seekbar
        val currentPosition = mediaPlayer.currentPosition/1000
        val seekbarDuration = mediaPlayer.duration/1000
        binding.currentSongTime.text = formattedTime(currentPosition)
        binding.songDuration.text = formattedTime(seekbarDuration)
        binding.seekBar.progress = currentPosition
        binding.seekBar.max=seekbarDuration

        val handler = Handler(Looper.getMainLooper()).postDelayed(runner, 1000)
    }

    private fun playNextSong(){
        if(currentSongPosition!=songDetailViewModel.songs.size-1){
            currentSongPosition+=1
            mediaPlayer.stop()

            val currentSong = allSongs[currentSongPosition]
            songDetailViewModel.song=currentSong
            loadSongDetails(songDetailViewModel.song)
            refreshMediaPlayer(songDetailViewModel.song)
            mediaPlayer=MediaPlayerDriver.get().getMediaPlayer()

            startPlaying(songDetailViewModel.song,mediaPlayer)
        }else if(currentSongPosition==songDetailViewModel.songs.size-1&&repeatSongs){
            currentSongPosition=0
            mediaPlayer.stop()

            val currentSong = allSongs[currentSongPosition]
            songDetailViewModel.song=currentSong
            loadSongDetails(songDetailViewModel.song)
            refreshMediaPlayer(songDetailViewModel.song)
            mediaPlayer=MediaPlayerDriver.get().getMediaPlayer()

            startPlaying(songDetailViewModel.song,mediaPlayer)
        }

    }

    private fun playPrevSong(){
        if(currentSongPosition==0|| (mediaPlayer.currentPosition/1000)>3){
            mediaPlayer.reset()
            refreshMediaPlayer(songDetailViewModel.song)
            mediaPlayer=MediaPlayerDriver.get().getMediaPlayer()
            startPlaying(songDetailViewModel.song,mediaPlayer)
        }else {
            currentSongPosition -= 1
            mediaPlayer.stop()

            val currentSong = allSongs[currentSongPosition]
            songDetailViewModel.song = currentSong
            loadSongDetails(songDetailViewModel.song)
            refreshMediaPlayer(songDetailViewModel.song)
            mediaPlayer = MediaPlayerDriver.get().getMediaPlayer()

            startPlaying(songDetailViewModel.song, mediaPlayer)
        }
    }

    private fun pausePlay(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        }else{
            mediaPlayer.start()
            binding.playPauseButton.setImageResource(R.drawable.ic_play)
        }
    }
    private fun startPlaying(song: Song?, mediaPlayer:MediaPlayer){
        if(song!=null){
            mediaPlayer.start()
        }else{
            Toast.makeText(context, "Song pathing error!", Toast.LENGTH_SHORT)
        }

    }

    private fun initMediaPlayer(song: Song?){
        if(song!=null){
            //Initialize the media player
            val uri = Uri.parse(song.path)
            context?.let { MediaPlayerDriver.initialize(it, uri) }
        }else{
            Toast.makeText(context, "Song pathing error!", Toast.LENGTH_SHORT)
        }
    }

    private fun refreshMediaPlayer(song:Song?){
        if(song!=null){
            //Refresh the media player
            val uri = Uri.parse(song.path)
            context?.let { MediaPlayerDriver.get().setMediaPlayer(it,uri) }
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