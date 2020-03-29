package com.mfriend.djapp.spotifyapi

import com.mfriend.djapp.spotifyapi.models.Pager
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.PlaylistRequestDto
import com.mfriend.djapp.spotifyapi.models.TrackDTO
import com.mfriend.djapp.spotifyapi.models.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Interface for methods to interact with the spotify web api.
 *
 * Created by mfriend on 2020-01-05.
 */
interface SpotifyService {
    /**
     * Returns the currently authenticated [UserDto]
     */
    @GET("me")
    suspend fun getCurrentUser(): UserDto

    /**
     * Gets the playlists the currently authenticated user has on their account
     * TODO find a way to intercept the response and unwrap it into either [Pager.items] or an error
     *
     * @return A [Pager] that contains a list of [PlaylistDto] in [Pager.items]
     */
    @GET("me/playlists")
    suspend fun getUsersPlaylists(): Pager<PlaylistDto>

    /**
     * Creates a [PlaylistDto] for the currently authenticated [UserDto] from a given [PlaylistRequestDto] and returns it
     *
     * TODO cache userId so its not required
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Body playlist: PlaylistRequestDto,
        @Path("user_id") userId: String
    ): PlaylistDto

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("playlists/{playlist_id}/tracks")
    suspend fun addSong(@Path("playlist_id") playlistId: String, @Query("uris") songUri: String)

    @GET("https://api.spotify.com/v1/me/top/tracks")
    suspend fun getUsersTopTracks(): Pager<TrackDTO>

    @GET
    suspend fun getMoreTracks(@Url href: String): Pager<TrackDTO>
}