package com.mfriend.djapp.tempUi

import androidx.lifecycle.*
import com.mfriend.djapp.spotifyapi.ApiFactory
import com.mfriend.djapp.spotifyapi.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(authToken: String) : ViewModel() {

    private val spotifyService = ApiFactory.getSpotifyService(authToken)

    val playlists: LiveData<List<Playlist>> = liveData {
        emit(emptyList())
        emit(spotifyService.getUsersPlaylists().items)
    }

    class ApiViewModelFactory(private val authToken: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PlaylistViewModel(authToken) as T
        }
    }
}