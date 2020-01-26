package com.mfriend.djapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.tempUi.PlaylistAdapter
import com.mfriend.djapp.tempUi.PlaylistViewModel
import kotlinx.android.synthetic.main.fragment_playlist.*


/**
* TODO WRite Class header
 */
class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val playlistAdapter = PlaylistAdapter(listOf())
        playlists.layoutManager = LinearLayoutManager(requireContext())
        playlists.adapter = playlistAdapter
        viewModel.playlists.observe(this) {
            playlistAdapter.items = it
            playlistAdapter.notifyDataSetChanged()
        }
    }
}
