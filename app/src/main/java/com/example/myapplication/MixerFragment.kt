package com.example.myapplication

import android.media.MediaPlayer
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentMixerBinding


class MixerFragment : Fragment() {
    private var _binding: FragmentMixerBinding? =null
    private val binding
        get()= checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val mixerViewModel: MixerViewModel by viewModels()
    lateinit var mediaPlayer: MediaPlayer
    lateinit var eq: Equalizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer=mixerViewModel.mediaPlayer
        eq= Equalizer(1,mediaPlayer.audioSessionId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentMixerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Runnable object to parse the current song position
        this.activity?.runOnUiThread(object: Runnable{
            override fun run() {
                //low band bar
                binding.lowBandBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })
                //mid band bar
                binding.midBandBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })
                //high band bar
                binding.highBandBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })

                fillSeekbar(this)

            }

        })


    }
    fun fillSeekbar(runner :Runnable){
        //Fill Out seekbar
        val currentPosition = 50
        val seekbarDuration = 100

        binding.lowBandBar.progress=currentPosition
        binding.midBandBar.progress=currentPosition
        binding.highBandBar.progress=currentPosition

        binding.lowBandBar.max=seekbarDuration
        binding.midBandBar.max=seekbarDuration
        binding.highBandBar.max=seekbarDuration
        val handler = Handler(Looper.getMainLooper()).postDelayed(runner, 1000)
    }
}