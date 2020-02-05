package com.mfriend.djapp.spotifyapi

import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.spotifyapi.models.Response
import com.mfriend.djapp.spotifyapi.models.User
import okhttp3.RequestBody
import retrofit2.http.*


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

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(@Body playlist: RequestBody, @Path("user_id") userId: String): Playlist
}