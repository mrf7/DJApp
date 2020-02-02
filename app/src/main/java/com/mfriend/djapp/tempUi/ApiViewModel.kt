package com.mfriend.djapp.tempUi

import androidx.lifecycle.*
import com.mfriend.djapp.spotifyapi.ApiFactory
import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.spotifyapi.models.User
import kotlinx.coroutines.launch

class ApiViewModel(authToken: String) : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val spotifyService = ApiFactory.getSpotifyService(authToken)

    val user: LiveData<User>
        get() = _user

    fun fetchUserClicked() {
        viewModelScope.launch {
            _user.value = spotifyService.getCurrentUser()
        }
    }



    class ApiViewModelFactory(private val authToken: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ApiViewModel(authToken) as T
        }
    }
}
