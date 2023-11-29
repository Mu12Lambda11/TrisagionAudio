package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentSongDetailBinding


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("SongDetailFragment", "Path is: ${args.songPath}")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSongDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

        }
    }

}