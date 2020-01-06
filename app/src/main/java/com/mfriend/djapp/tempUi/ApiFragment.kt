package com.mfriend.djapp.tempUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.R
import com.mfriend.djapp.spotifyapi.models.User
import kotlinx.android.synthetic.main.api_fragment.*

class ApiFragment private constructor() : Fragment() {


    private val viewModel: ApiViewModel by viewModels {
        ApiViewModel.ApiViewModelFactory(requireArguments()[API_TOKEN] as String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.api_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val playlistAdapter = PlaylistAdapter(listOf())
        playlists.layoutManager = LinearLayoutManager(requireContext())
        playlists.adapter = playlistAdapter


        btn_fetch_user.setOnClickListener {
            viewModel.fetchUserClicked()
        }

        btn_fetch_playlists.setOnClickListener {
            viewModel.fetchPlaylistsClicked()
        }
        viewModel.user.observe(this) { user: User ->
            tv_user.text = user.displayName
        }

        viewModel.playLists.observe(this) {
            playlistAdapter.items = it
            playlistAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val API_TOKEN = "api_token"
        fun newInstance(apiToken: String) = ApiFragment().apply {
            arguments = bundleOf(API_TOKEN to apiToken)
        }
    }
}
