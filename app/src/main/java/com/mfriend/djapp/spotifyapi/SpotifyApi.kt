package com.mfriend.djapp.spotifyapi

import arrow.core.Either
import com.mfriend.djapp.spotifyapi.adapters.ErrorResponse
import com.mfriend.djapp.spotifyapi.models.Pager
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.PlaylistRequestDto
import com.mfriend.djapp.spotifyapi.models.PlaylistSnapshot
import com.mfriend.djapp.spotifyapi.models.SpotifyErrorContainer
import com.mfriend.djapp.spotifyapi.models.TrackDTO
import com.mfriend.djapp.spotifyapi.models.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

typealias SpotifyResponseEither<T> = Either<ErrorResponse<SpotifyErrorContainer>, T>

/**
 * Interface for methods to interact with the spotify web api. All requests in this interface will
 * return an [Either.left] of [SpotifyErrorContainer] to denote the error instead of throwing an exception
 *
 * Created by mfriend on 2020-01-05.
 */
interface SpotifyApi {
    /**
     * Returns the currently authenticated [UserDto]
     */
    @GET("me")
    suspend fun getCurrentUser(): SpotifyResponseEither<UserDto>

    /**
     * Gets the playlists the currently authenticated user has on their account
     *
     * @return A [Pager] that contains a list of [PlaylistDto] in [Pager.items] or [SpotifyErrorContainer]
     */
    @GET("me/playlists")
    suspend fun getUsersPlaylists(): SpotifyResponseEither<Pager<PlaylistDto>>

    /**
     * Creates a [PlaylistDto] for the currently authenticated [UserDto] from a given [PlaylistRequestDto] and returns it
     *
     * @return Either a [PlaylistDto] representing the created playlist or [SpotifyErrorContainer]
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Body playlist: PlaylistRequestDto,
        @Path("user_id") userId: String
    ): SpotifyResponseEither<PlaylistDto>

    /**
     * Adds song denoted by [songUri] to playlist denoted by [playlistId]
     *
     * @return [PlaylistSnapshot] that can be used to get a snapshot of the [PlaylistDto]
     * represented by [playlistId] the moment after adding [songUri] or a [SpotifyErrorContainer]
     */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("playlists/{playlist_id}/tracks")
    suspend fun addSong(
        @Path("playlist_id") playlistId: String,
        @Query("uris") songUri: String
    ): SpotifyResponseEither<PlaylistSnapshot>

    /**
     * Gets a personalized list of the current users most listened to tracks
     *
     * @param limit max number of tracks to get in the response, default: 50
     * @return Either a [Pager] of [TrackDTO] that contains the users top [limit] tracks and urls to
     * get the next page or a [SpotifyErrorContainer]
     */
    @GET("https://api.spotify.com/v1/me/top/tracks")
    suspend fun getUsersTopTracks(@Query("limit") limit: Int = 50): SpotifyResponseEither<Pager<TrackDTO>>
}