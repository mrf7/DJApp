package com.mfriend.djapp.spotifyapi

import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.spotifyapi.models.Response
import com.mfriend.djapp.spotifyapi.models.User
import retrofit2.http.GET


/**
 * Interface for methods to interact with the spotify web api.
 *
 * Created by mfriend on 2020-01-05.
 */
interface SpotifyService {
    @GET("me")
    suspend fun getCurrentUser(): User

    @GET("me/playlists")
    suspend fun getUsersPlaylists(): Response<Playlist>
}