package com.mfriend.djapp.reviewrequests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import com.mfriend.djapp.common.db.entities.Track
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import kotlinx.coroutines.launch
import java.util.Deque
import java.util.LinkedList

class ReviewRequestsViewModel(
    private val reviewRequestRepo: ReviewRequestRepo,
    private val playlist: PlaylistDto
) : ViewModel() {

    /**
     *  Shows the currently seclect track
     */
    val currentTrack: LiveData<Either<TrackReviewErrors, Track>>
        get() = _currentTrack

    private val _currentTrack = MutableLiveData<Either<TrackReviewErrors, Track>>()
    private val songsStack: Deque<Track> = LinkedList()

    init {
        viewModelScope.launch {
            _currentTrack.value = TrackReviewErrors.LoadingSongs.left()
            _currentTrack.value = reviewRequestRepo.getRequests().fold(
                { TrackReviewErrors.CommunicationError.left() },
                { requests ->
                    songsStack.addAll(requests)
                    songsStack.poll().rightIfNotNull { TrackReviewErrors.NoMoreSongs }
                }
            )
        }
    }

    /**
     * Called when the user presses the add song button. Adds the current song to the selected playlist
     * then moves to the next song
     */
    fun addSongPressed() {
        _currentTrack.value?.map { trackToAdd ->
            viewModelScope.launch {
                reviewRequestRepo.addSongToPlaylist(trackToAdd, playlist)
                nextSong()
            }
        }
    }

    /**
     * Call when the user presses the decline song button. Moves to the next song without adding
     * anything to the selected playlist
     */
    fun declineSongPressed() {
        nextSong()
    }

    /**
     * Moves to the next song, loading new songs from the api if there is no next
     */
    private fun nextSong() {
        _currentTrack.value?.map { track ->
            viewModelScope.launch {
                reviewRequestRepo.clearRequest(track)
            }
        }
        _currentTrack.value = songsStack.poll().rightIfNotNull { TrackReviewErrors.NoMoreSongs }
    }
}

enum class TrackReviewErrors {
    LoadingSongs,
    CommunicationError,
    NoMoreSongs
}