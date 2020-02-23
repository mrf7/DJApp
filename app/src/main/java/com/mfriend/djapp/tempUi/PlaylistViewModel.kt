package com.mfriend.djapp.tempUi

import androidx.lifecycle.*
import com.mfriend.djapp.Event
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.spotifyapi.models.PlaylistDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

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

    fun newPlaylistPressed(playlistName: String) {
        viewModelScope.launch {
            // Need user id for the request for some reason.
            // TODO Cache it somewhere so we dont fetch it when creating a new playlist
            val userId = spotifyService.getCurrentUser().id
            val newPlaylist = PlaylistDTO(playlistName, "Playlist made by DJ App")
            val createdPlaylist = try {
                spotifyService.createPlaylist(newPlaylist, userId)
            } catch (e: HttpException) {
                return@launch
            }
            _selectPlaylist.value = Event(createdPlaylist)
        }
    }
}