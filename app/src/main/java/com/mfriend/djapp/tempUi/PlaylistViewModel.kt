package com.mfriend.djapp.tempUi

import androidx.lifecycle.*
import com.mfriend.djapp.Event
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.Playlist
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
            val userId = spotifyService.getCurrentUser().id
            val newPlaylist =
                """{"name": "$playlistName"}"""".toRequestBody("application/json".toMediaTypeOrNull())
            val createdPlaylist = try {
                spotifyService.createPlaylist(newPlaylist, userId)
            } catch (e: HttpException) {
                return@launch
            }
            _selectPlaylist.value = Event(createdPlaylist)
        }
    }
}