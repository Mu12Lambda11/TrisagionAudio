package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentSongListBinding
import kotlinx.coroutines.launch

class SongListFragment : Fragment(){

    private var _binding: FragmentSongListBinding? =null
    private val binding
        get()= checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

     private val songListViewModel: SongListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CrimeListFragment", "Total Songs: ${songListViewModel.songs.size}")
        //TODO: update
        //setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSongListBinding.inflate(inflater,container,false)
        binding.songRecyclerView.layoutManager=LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                val songs = songListViewModel.songs
                binding.songRecyclerView.adapter= context?.let {
                    SongListAdapter(it,songs){ songPath ->
                        findNavController().navigate(
                            SongListFragmentDirections.showSongDetail(songPath)
                        )
                    }
                }
            }
        }
    }



}