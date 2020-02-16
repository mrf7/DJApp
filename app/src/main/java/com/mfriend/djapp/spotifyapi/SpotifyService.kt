package com.mfriend.djapp.spotifyapi

import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.spotifyapi.models.PlaylistDTO
import com.mfriend.djapp.spotifyapi.models.Response
import com.mfriend.djapp.spotifyapi.models.User
import retrofit2.http.*


/**
 * Interface for methods to interact with the spotify web api.
 *
 * Created by mfriend on 2020-01-05.
 */
interface SpotifyService {

    /**
     * Returns the currently authenticated [User]
     */
    @GET("me")
    suspend fun getCurrentUser(): User

    /**
     * Gets the playlists the currently authenticated user has on their account
     * TODO find a way to intercept the response and unwrap it into either [Response.items] or an error
     *
     * @return A [Response] that contains a list of [Playlist] in [Response.items]
     */
    @GET("me/playlists")
    suspend fun getUsersPlaylists(): Response<Playlist>

    /**
     * Creates a [Playlist] for the currently authenticated [User] from a given [PlaylistDTO] and returns it
     *
     * TODO cache userId so its not required
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(@Body playlist: PlaylistDTO, @Path("user_id") userId: String): Playlist
}