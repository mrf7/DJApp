package com.mfriend.djapp.tempUi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.Playlist
import kotlinx.coroutines.launch

/**
 * ViewModel for the screen to add a song to a slected playlist
 *
 * @param playlist to add songs to
 *
 * Created by mfriend on 2020-02-16.
 */
class AddSongViewModel(private val spotifyService: SpotifyService, private val playlist: Playlist) :
    ViewModel() {

    /**
     *  LiveData for result of request
     */
    val requestResult: LiveData<String>
        get() = _requestResult

    private val _requestResult = MutableLiveData<String>()

    private val songUriRegex: Regex = """https://open.spotify.com/track/(.*)\?.*""".toRegex()

    /**
     * Add a song to the selected playlist
     *
     * @param songLink the share link of the song to add to the playlist
     */
    fun addSong(songLink: String) {
        val uri = songUriRegex.matchEntire(songLink)?.groupValues?.get(1) ?: run {
            Log.e("MRF", "didnt match $songLink")
            return
        }
        Log.d("MRF", "found $uri")
        viewModelScope.launch {
            try {
                spotifyService.addSong(playlist.id, "spotify:track:$uri")
                _requestResult.value = "Added the song"
            } catch (e: Exception) {
                _requestResult.value = e.message

                Log.e("MRF", "failed to add $uri", e)
            }

        }
    }
}