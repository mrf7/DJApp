package com.mfriend.djapp.reviewrequests

import arrow.core.Either
import com.mfriend.djapp.common.db.daos.TrackDao
import com.mfriend.djapp.common.db.entities.Track
import com.mfriend.djapp.spotifyapi.SpotifyApi
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
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
    suspend fun getRequests(): Either<Throwable, List<Track>> = Either.catch {
        if (trackDao.getAll().isEmpty()) {
            spotifyApi.getUsersTopTracks().items.map { trackDTO ->
                trackDao.insert(trackDTO.toTrack())
            }
        }
        trackDao.getAll()
    }

    /**
     * Removes [track] from the list of requests for the user to address
     */
    suspend fun clearRequest(track: Track) {
        trackDao.delete(track)
    }
}

