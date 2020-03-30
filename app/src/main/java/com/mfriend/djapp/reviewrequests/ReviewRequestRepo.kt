package com.mfriend.djapp.reviewrequests

import arrow.core.Either
import com.mfriend.djapp.spotifyapi.SpotifyApi
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.TrackDTO

/**
 * Repository for interactions with the network and db related to reviewing song request
 */
class ReviewRequestRepo(private val spotifyApi: SpotifyApi) {

    private var nextPage: String? = null

    /**
     * Adds [trackDTO] to [playlistDto]
     */
    suspend fun addSongToPlaylist(trackDTO: TrackDTO, playlistDto: PlaylistDto) {
        spotifyApi.addSong(playlistDto.id, trackDTO.uri)
    }

    /**
     * Fetches the users most listened to tracks
     */
    suspend fun getUsersTopTracks(): Either<Throwable, List<TrackDTO>> = Either.catch {
        spotifyApi.getUsersTopTracks()
    }.map { trackPager ->
        trackPager.items
    }

    /**
     * Gets the next page of songs
     */
    suspend fun getMoreSongs(): List<TrackDTO> {
        val nextPageGuard = nextPage ?: return emptyList()
        val newPage = spotifyApi.getMoreTracks(nextPageGuard)
        nextPage = newPage.next
        return newPage.items
    }
}