package com.mfriend.djapp.reviewrequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.mfriend.djapp.databinding.FragmentReviewRequestsBinding
import com.mfriend.djapp.spotifyapi.models.TrackDTO
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ReviewRequestsFragment : Fragment() {
    private lateinit var binding: FragmentReviewRequestsBinding
    private val args by navArgs<ReviewRequestsFragmentArgs>()

    private val viewModel: ReviewRequestsViewModel by viewModel { parametersOf(args.selectedPlaylist) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.currentTrack.observe(viewLifecycleOwner) { track: TrackDTO? ->
            binding.apply {
                if (track == null) {
                    Toast.makeText(context, "No More songs", Toast.LENGTH_LONG).show()
                    return@observe
                }
                tvSongName.text = track.name
                tvAlbumName.text = track.album.name
                tvArtistName.text = track.artists.firstOrNull()?.name ?: ""
                tvAlbumArtwork.load(track.album.images.first().url) {
                    lifecycle(this@ReviewRequestsFragment)
                    crossfade(true)
                    fallback(android.R.drawable.ic_media_play)
                }
            }
        }
        binding.btnAddSong.setOnClickListener {
            viewModel.addSongPressed()
        }
        binding.btnDecline.setOnClickListener {
            viewModel.declineSongPressed()
        }
    }

}
