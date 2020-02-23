package com.mfriend.djapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.helper.observeEvent
import com.mfriend.djapp.tempUi.PlaylistFragmentDirections
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
        val playlistAdapter = PlaylistAdapter(
            listOf(),
            playlistViewModel::playlistSelected
        )
        // Add dividers
        rv_playlists.apply {
            val dividerItemDecoration =
                DividerItemDecoration(rv_playlists.context, LinearLayoutManager.VERTICAL)

            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }

        btn_new_playlist.setOnClickListener {
            playlistViewModel.newPlaylistPressed(et_playlist_name.text.toString())
        }

        playlistViewModel.playlists.observe(this) {
            playlistAdapter.items = it
            playlistAdapter.notifyDataSetChanged()
        }
        playlistViewModel.selectedPlaylist.observeEvent(this) {
            val action =
                PlaylistFragmentDirections.actionPlaylistSelected(
                    it
                )
            findNavController().navigate(action)
        }
    }
}
