package com.mfriend.djapp.selectplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.common.helper.observeEvent
import com.mfriend.djapp.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment to display the current users playlists
 */
class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create adapter and notify view model when an item is selected
        val playlistAdapter = PlaylistAdapter(
            listOf(),
            playlistViewModel::playlistSelected
        )
        // Add dividers
        binding.rvPlaylists.apply {
            val dividerItemDecoration =
                DividerItemDecoration(binding.rvPlaylists.context, LinearLayoutManager.VERTICAL)

            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }

        binding.btnNewPlaylist.setOnClickListener {
            playlistViewModel.newPlaylistPressed(binding.etPlaylistName.text.toString())
        }

        playlistViewModel.playlists.observe(viewLifecycleOwner) {
            // Show either the list of playlists or an error
            it.fold(
                ifRight = {
                    playlistAdapter.items = it
                    playlistAdapter.notifyDataSetChanged()
                },
                ifLeft = {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
        playlistViewModel.selectedPlaylistDto.observeEvent(this) {
            val action =
                PlaylistFragmentDirections.actionReviewRequests(
                    it
                )
            findNavController().navigate(action)
        }
    }
}