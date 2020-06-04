package com.mfriend.djapp.reviewrequests

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.mfriend.djapp.common.db.daos.TrackDao
import com.mfriend.djapp.common.db.entities.Track
import com.mfriend.djapp.spotifyapi.SpotifyApi
import com.mfriend.djapp.helpers.retrofitadapters.ErrorResponse
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.SpotifyErrorContainer
import com.mfriend.djapp.typeconverters.toTrack

/**
 * Repository for interactions with the network and db related to reviewing song request
 */
class ReviewRequestRepo(private val spotifyApi: SpotifyApi, private val trackDao: TrackDao) {

    /**
     * Adds [trackDTO] to [playlistDto]
     */
    suspend fun addSongToPlaylist(trackDTO: Track, playlistDto: PlaylistDto) {
        spotifyApi.addSong(playlistDto.id, trackDTO.spotifyUri)
    }

    /**
     * Gets the list of songs requested to add to the playlist
     */
    suspend fun getRequests(): Either<ErrorResponse<SpotifyErrorContainer>, List<Track>> {
        val allTracks = trackDao.getAll()
        return if (allTracks.isEmpty()) {
            val tracks =
                spotifyApi.getUsersTopTracks().map { it.items }

            // If successful, insert all the tracks into the DB, otherwise return the error
            when (tracks) {
                is Either.Right -> {
                    tracks.b.forEach { trackDao.insert(it.toTrack()) }
                    trackDao.getAll().right()
                }
                is Either.Left -> tracks.a.left()
            }
        } else {
            allTracks.right()
        }
    }

    /**
     * Removes [track] from the list of requests for the user to address
     */
    suspend fun clearRequest(track: Track) {
        trackDao.delete(track)
    }
}