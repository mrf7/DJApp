package com.mfriend.djapp.tempUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.R
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * TODO WRite Class header
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
        val playlistAdapter = PlaylistAdapter(listOf())
        playlists.layoutManager = LinearLayoutManager(requireContext())
        playlists.adapter = playlistAdapter
        playlistViewModel.playlists.observe(this) {
            playlistAdapter.items = it
            playlistAdapter.notifyDataSetChanged()
        }
    }
}
