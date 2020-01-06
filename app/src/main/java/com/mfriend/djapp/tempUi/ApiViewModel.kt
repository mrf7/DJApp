package com.mfriend.djapp.tempUi

import androidx.lifecycle.*
import com.mfriend.djapp.spotifyapi.ApiFactory
import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.spotifyapi.models.User
import kotlinx.coroutines.launch

class ApiViewModel(authToken: String) : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val spotifyService = ApiFactory.getSpotifyService(authToken)

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playLists: LiveData<List<Playlist>>
        get() = _playlists

    val user: LiveData<User>
        get() = _user

    fun fetchUserClicked() {
        viewModelScope.launch {
            _user.value = spotifyService.getCurrentUser()
        }
    }

    fun fetchPlaylistsClicked() {
        viewModelScope.launch {
            _playlists.value = spotifyService.getUsersPlaylists().items
        }
    }

    class ApiViewModelFactory(val authToken: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ApiViewModel(authToken) as T
        }

    }
}
