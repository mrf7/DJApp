package com.mfriend.djapp

import androidx.lifecycle.*
import com.mfriend.djapp.helper.Event
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
                return@launch
            }
            _selectPlaylistDto.value =
                Event(createdPlaylist)
        }
    }
}