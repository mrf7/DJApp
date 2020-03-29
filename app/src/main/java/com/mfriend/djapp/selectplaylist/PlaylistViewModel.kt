package com.mfriend.djapp.selectplaylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mfriend.djapp.common.helper.Event
import com.mfriend.djapp.common.helper.extensions.LOGGER_TAG
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.PlaylistRequestDto
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PlaylistViewModel(private val spotifyService: SpotifyService) : ViewModel() {
    private val _selectPlaylistDto: MutableLiveData<Event<PlaylistDto>> = MutableLiveData()
    val selectedPlaylistDto: LiveData<Event<PlaylistDto>> = _selectPlaylistDto

    val playlists: LiveData<List<PlaylistDto>> = liveData {
        emit(emptyList())
        emit(spotifyService.getUsersPlaylists().items)
    }

    fun playlistSelected(selectedPlaylistDto: PlaylistDto) {
        _selectPlaylistDto.value = Event(selectedPlaylistDto)
    }

    fun newPlaylistPressed(playlistName: String) {
        viewModelScope.launch {
            // Need user id for the request for some reason.
            // TODO Cache it somewhere so we dont fetch it when creating a new playlist
            val userId = spotifyService.getCurrentUser().id
            val newPlaylist = PlaylistRequestDto(playlistName, "Playlist made by DJ App")
            val createdPlaylist = try {
                spotifyService.createPlaylist(newPlaylist, userId)
            } catch (e: HttpException) {
                Log.e(
                    this@PlaylistViewModel.LOGGER_TAG,
                    "Got an http exception trying to create playlist",
                    e
                )
                return@launch
            }
            _selectPlaylistDto.value =
                Event(createdPlaylist)
        }
    }
}