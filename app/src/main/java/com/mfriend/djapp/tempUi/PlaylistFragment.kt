package com.mfriend.djapp.tempUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.R
import com.mfriend.djapp.observeEvent
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Fragment to display the current users playlists
 */
class PlaylistFragment : Fragment() {


    private val playlistViewModel: PlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Create adapter and notify view model when an item is selected
        val playlistAdapter = PlaylistAdapter(listOf(), playlistViewModel::playlistSelected)
        val dividerItemDecoration =
            DividerItemDecoration(rv_playlists.context, LinearLayoutManager.VERTICAL)

        rv_playlists.layoutManager = LinearLayoutManager(requireContext())
        rv_playlists.adapter = playlistAdapter
        rv_playlists.addItemDecoration(dividerItemDecoration)
        playlistViewModel.playlists.observe(this) {
            playlistAdapter.items = it
            playlistAdapter.notifyDataSetChanged()
        }
        playlistViewModel.selectedPlaylist.observeEvent(this) {
            val action = PlaylistFragmentDirections.actionPlaylistSelected(it)
            findNavController().navigate(action)
        }
    }
}
