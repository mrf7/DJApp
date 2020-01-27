package com.mfriend.djapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.tempUi.PlaylistAdapter
import com.mfriend.djapp.tempUi.PlaylistViewModel
import kotlinx.android.synthetic.main.fragment_playlist.*


/**
* TODO WRite Class header
 */
class PlaylistFragment : Fragment() {

    private val args: PlaylistFragmentArgs by navArgs()

    private val viewModel: PlaylistViewModel by viewModels {
       PlaylistViewModel.ApiViewModelFactory(args.apiToken)
    }

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
        viewModel.playlists.observe(this) {
            playlistAdapter.items = it
            playlistAdapter.notifyDataSetChanged()
        }
    }
}
