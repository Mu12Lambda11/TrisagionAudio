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
import com.example.myapplication.databinding.FragmentMixerBinding
import kotlin.properties.Delegates


class MixerFragment : Fragment() {
    private var _binding: FragmentMixerBinding? =null
    private val binding
        get()= checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val mixerViewModel: MixerViewModel by viewModels()
    lateinit var mediaPlayer: MediaPlayer
    lateinit var eq: Equalizer
    var lowerBandEqRange by Delegates.notNull<Short>()
    var upperBandEqRange by Delegates.notNull<Short>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer=mixerViewModel.mediaPlayer

        //fill out the eq
        eq= Equalizer(0,mediaPlayer.audioSessionId)
        eq.enabled=false
        lowerBandEqRange= eq.bandLevelRange[0]
        upperBandEqRange= eq.bandLevelRange[1]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentMixerBinding.inflate(layoutInflater, container, false)

        //Fill Out seekbar
        val seekbarDuration = upperBandEqRange-lowerBandEqRange
        val currentPosition = seekbarDuration/2

        binding.lowBandBar.progress=currentPosition
        binding.midBandBar.progress=currentPosition
        binding.highBandBar.progress=currentPosition

        binding.lowBandBar.max=seekbarDuration
        binding.midBandBar.max=seekbarDuration
        binding.highBandBar.max=seekbarDuration

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        eq.setBandLevel(1, binding.lowBandBar.progress.toShort())
        eq.setBandLevel(2, binding.midBandBar.progress.toShort())
        eq.setBandLevel(3, binding.highBandBar.progress.toShort())

        //Runnable object to parse the current song position
        requireActivity().runOnUiThread(object: Runnable{
            override fun run() {
                runSeekbars()

            }

        })

        binding.apply {
            eqButton.setOnClickListener {
                if(!eq.enabled){
                    eq.enabled=true
                    eqButton.setImageResource(R.drawable.ic_equalizer_on)
                    eqBtnText.text="ON"
                }else{
                    eq.enabled=false
                    eqButton.setImageResource(R.drawable.ic_equalizer_off)
                    eqBtnText.text="OFF"
                }
            }
            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    private fun runSeekbars() {
        //low band bar
        binding.lowBandBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                eq.setBandLevel(1, (progress+lowerBandEqRange).toShort())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        //mid band bar
        binding.midBandBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                eq.setBandLevel(2, (progress+lowerBandEqRange).toShort())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        //high band bar
        binding.highBandBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                eq.setBandLevel(3, (progress+lowerBandEqRange).toShort())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }




}