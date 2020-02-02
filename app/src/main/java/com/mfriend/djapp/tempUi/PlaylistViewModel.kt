package com.mfriend.djapp.tempUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.Playlist

class PlaylistViewModel(private val spotifyService: SpotifyService) : ViewModel() {
    val playlists: LiveData<List<Playlist>> = liveData {
        emit(emptyList())
        emit(spotifyService.getUsersPlaylists().items)
    }
}