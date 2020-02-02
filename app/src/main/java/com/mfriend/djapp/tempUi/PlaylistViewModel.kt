package com.mfriend.djapp.tempUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mfriend.djapp.Event
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.Playlist

class PlaylistViewModel(private val spotifyService: SpotifyService) : ViewModel() {
    private val _selectPlaylist: MutableLiveData<Event<Playlist>> = MutableLiveData()
    val selectedPlaylist: LiveData<Event<Playlist>> = _selectPlaylist

    val playlists: LiveData<List<Playlist>> = liveData {
        emit(emptyList())
        emit(spotifyService.getUsersPlaylists().items)
    }

    fun playlistSelected(selectedPlaylist: Playlist) {
        _selectPlaylist.value = Event(selectedPlaylist)
    }
}