package com.mfriend.djapp.reviewrequests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.TrackDTO
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
    val currentTrack: LiveData<TrackDTO>
        get() = _currentTrack

    private val _currentTrack = MutableLiveData<TrackDTO>()
    private val songsStack: Deque<TrackDTO> = LinkedList()

    init {
        viewModelScope.launch {
            refreshSongs()
        }
    }

    /**
     * Called when the user presses the add song button. Adds the current song to the selected playlist
     * then moves to the next song
     */
    fun addSongPressed() {
        val trackToAdd = _currentTrack.value ?: return
        viewModelScope.launch {
            reviewRequestRepo.addSongToPlaylist(trackToAdd, playlist)
            nextSong()
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
        if (songsStack.peekFirst() == null) {
            _currentTrack.value = null
            viewModelScope.launch { refreshSongs() }
        } else {
            _currentTrack.value = songsStack.pop()
        }
    }

    /**
     * Fetches the users top songs from the api and adds them to the back of the stack
     */
    private suspend fun refreshSongs() {
        reviewRequestRepo.getUsersTopTracks().forEach { songsStack.push(it) }
        _currentTrack.value = songsStack.pop()
    }
}
