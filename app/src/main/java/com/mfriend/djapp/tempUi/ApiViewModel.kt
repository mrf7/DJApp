package com.mfriend.djapp.tempUi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.UserDto
import kotlinx.coroutines.launch

class ApiViewModel(val spotifyService: SpotifyService) : ViewModel() {
    private val _user = MutableLiveData<UserDto>()

    val userDto: LiveData<UserDto>
        get() = _user

    fun fetchUserClicked() {
        viewModelScope.launch {
            _user.value = spotifyService.getCurrentUser()
        }
    }
}
