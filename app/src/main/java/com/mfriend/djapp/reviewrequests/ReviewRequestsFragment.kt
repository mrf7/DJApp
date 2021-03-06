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
import com.mfriend.djapp.common.db.entities.Track
import com.mfriend.djapp.databinding.FragmentReviewRequestsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Fragment to display song requests to the user one at a time and lets them approve or decline each
 * request
 */
class ReviewRequestsFragment : Fragment() {
    // View binding for views and args from the nav host
    private lateinit var binding: FragmentReviewRequestsBinding
    private val args by navArgs<ReviewRequestsFragmentArgs>()

    private val viewModel: ReviewRequestsViewModel by viewModel { parametersOf(args.selectedPlaylist) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.currentTrack.observe(viewLifecycleOwner) { state: Either<TrackReviewErrors, Track> ->
            // Handle Either.Left (error) and Either.Right (success) values.
            // The a and b properties are members of Either.Left and Either.Right respectively which
            // reference the left/right values in a type safe way after an Either has been smart casted
            // to a Left or Right.
            when (state) {
                is Either.Left -> handleError(state.a)
                is Either.Right -> handleNewTrack(state.b)
            }
        }

        // Pass click events to the viewmodel
        binding.btnAddSong.setOnClickListener {
            viewModel.addSongPressed()
        }

        binding.btnDecline.setOnClickListener {
            viewModel.declineSongPressed()
        }
    }

    /**
     * Handle all error statuses from [TrackReviewErrors]
     *
     * @param error the error to handle
     */
    private fun handleError(error: TrackReviewErrors) {
        when (error) {
            TrackReviewErrors.LoadingSongs -> showLoadingState()
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
                return
            }
        }
    }

    /**
     * Display the current [track] to the user
     */
    private fun handleNewTrack(track: Track) {
        binding.tvSongName.text = track.name
        binding.tvAlbumName.text = track.album
        binding.tvArtistName.text = track.artist
        binding.tvAlbumArtwork.load(track.imageUrl) {
            lifecycle(viewLifecycleOwner)
            crossfade(true)
            fallback(android.R.drawable.ic_media_play)
        }
    }

    /**
     * Show a UI to  tell the user that we are loading more tracks
     */
    private fun showLoadingState() {
        binding.tvSongName.text = ""
        binding.tvAlbumName.text = ""
        binding.tvArtistName.text = ""
        binding.tvAlbumArtwork.load(android.R.drawable.btn_star)
    }
}