package com.mfriend.djapp.selectplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.right
import com.mfriend.djapp.common.helper.Event
import com.mfriend.djapp.spotifyapi.SpotifyApi
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.PlaylistRequestDto
import com.mfriend.djapp.spotifyapi.models.SpotifyErrorBody
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for a view that displays the users playlist and allows the users to either select and existing
 * playlist for a party or create a new playlist
 */
class PlaylistViewModel(private val spotifyApi: SpotifyApi) : ViewModel() {
    private val _selectPlaylistDto: MutableLiveData<Event<PlaylistDto>> = MutableLiveData()

    /**
     * LiveData that will emit an [Event] of a [PlaylistDto] when the UI should navigate to the review
     * request screen for that playlist
     */
    val selectedPlaylistDto: LiveData<Event<PlaylistDto>> = _selectPlaylistDto

    /**
     * Livedata that will emit all of the users playlist.
     */
    val playlists: LiveData<Either<SpotifyErrorBody, List<PlaylistDto>>> = liveData {
        emit(emptyList<PlaylistDto>().right())
        emit(spotifyApi.getUsersPlaylists().map { it.items })
    }

    /**
     * Function to call when the user selects. Will allow user to review
     * requests for [selectedPlaylistDto]
     */
    fun playlistSelected(selectedPlaylistDto: PlaylistDto) {
        _selectPlaylistDto.value = Event(selectedPlaylistDto)
    }

    /**
     * Function to call when the user presses the new playlist button. Will create a playlist  with
     * [playlistName] then allow the user to review requests for that playlist
     */
    fun newPlaylistPressed(playlistName: String) {
        viewModelScope.launch {
            // Need user id for the request for some reason.
            // TODO Cache it somewhere so we dont fetch it when creating a new playlist
            val userId = spotifyApi.getCurrentUser().getOrElse { null }?.id ?: return@launch
            val newPlaylist = PlaylistRequestDto(playlistName, "Playlist made by DJ App")

            val createPlaylistResponse = spotifyApi.createPlaylist(newPlaylist, userId)
            createPlaylistResponse.fold(
                ifLeft = {
                    Timber.e("Got error creating playlist $it")
                },
                ifRight = {
                    _selectPlaylistDto.value = Event(it)
                }
            )
        }
    }
}