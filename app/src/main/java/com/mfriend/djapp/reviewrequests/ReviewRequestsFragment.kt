package com.mfriend.djapp.reviewrequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Either
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

        viewModel.currentTrack.observe(viewLifecycleOwner) { state: Either<TrackReviewErrors, TrackDTO> ->
            when (state) {
                is Either.Left -> when (state.a) {
                    TrackReviewErrors.LoadingSongs -> {
                        Toast.makeText(context, "Loading songs", Toast.LENGTH_SHORT).show()
                        showBlankUI()
                    }
                    TrackReviewErrors.CommunicationError -> Toast.makeText(
                        context,
                        "CommError",
                        Toast.LENGTH_LONG
                    ).show()
                    TrackReviewErrors.NoMoreSongs -> {
                        // If there are no more songs, show a message to the user and return to the
                        // playlist selection screen for now
                        Toast.makeText(context, "Nore More Songs", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
                is Either.Right -> {
                    val track = state.b
                    binding.tvSongName.text = track.name
                    binding.tvAlbumName.text = track.album.name
                    binding.tvArtistName.text = track.artists.firstOrNull()?.name ?: ""
                    binding.tvAlbumArtwork.load(track.album.images.first().url) {
                        lifecycle(viewLifecycleOwner)
                        crossfade(true)
                        fallback(android.R.drawable.ic_media_play)
                    }
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

    private fun showBlankUI() {
        binding.tvSongName.text = ""
        binding.tvAlbumName.text = ""
        binding.tvArtistName.text = ""
        binding.tvAlbumArtwork.load(android.R.drawable.btn_star)
    }
}
