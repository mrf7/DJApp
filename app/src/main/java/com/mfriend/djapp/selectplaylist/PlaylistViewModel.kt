package com.mfriend.djapp.selectplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mfriend.djapp.common.helper.Event
import com.mfriend.djapp.spotifyapi.SpotifyApi
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.PlaylistRequestDto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class PlaylistViewModel(private val spotifyApi: SpotifyApi) : ViewModel() {
    private val _selectPlaylistDto: MutableLiveData<Event<PlaylistDto>> = MutableLiveData()
    val selectedPlaylistDto: LiveData<Event<PlaylistDto>> = _selectPlaylistDto

    val playlists: LiveData<List<PlaylistDto>> = liveData {
        emit(emptyList())
        emit(spotifyApi.getUsersPlaylists().items)
    }

    fun playlistSelected(selectedPlaylistDto: PlaylistDto) {
        _selectPlaylistDto.value = Event(selectedPlaylistDto)
    }

    fun newPlaylistPressed(playlistName: String) {
        viewModelScope.launch {
            // Need user id for the request for some reason.
            // TODO Cache it somewhere so we dont fetch it when creating a new playlist
            val userId = spotifyApi.getCurrentUser().id
            val newPlaylist = PlaylistRequestDto(playlistName, "Playlist made by DJ App")
            val createdPlaylist = try {
                spotifyApi.createPlaylist(newPlaylist, userId)
            } catch (e: HttpException) {
                Timber.e(e, "Got an http exception trying to create playlist")
                return@launch
            }
            _selectPlaylistDto.value =
                Event(createdPlaylist)
        }
    }
}