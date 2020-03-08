package com.mfriend.djapp.reviewrequests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.TrackDTO
import kotlinx.coroutines.launch
import java.util.*

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

    fun addSongPressed() {
        val trackToAdd = _currentTrack.value ?: return
        viewModelScope.launch {
            reviewRequestRepo.addSongToPlaylist(trackToAdd, playlist)

            if (songsStack.peekFirst() == null) {
                refreshSongs()
            } else {
                _currentTrack.value = songsStack.pop()
            }
        }
    }

    private suspend fun refreshSongs() {
        reviewRequestRepo.getUsersTopTracks().forEach { songsStack.push(it) }
        _currentTrack.value = songsStack.pop()
    }

    fun declineSongPressed() {
        _currentTrack.value = songsStack.pop()
    }

}
