package com.mfriend.djapp.reviewrequests

import com.mfriend.djapp.common.db.daos.TrackDao
import com.mfriend.djapp.common.db.entities.Track
import com.mfriend.djapp.spotifyapi.SpotifyApi
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.TrackDTO

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
    suspend fun getRequests(): List<Track> {
        if (trackDao.getAll().isEmpty()) {
            val topTracks = spotifyApi.getUsersTopTracks(100).items.map { it.toTrack() }
            trackDao.insert(*topTracks.toTypedArray())
        }
        return trackDao.getAll()
    }

    suspend fun clearRequest(track: Track) {
        trackDao.delete(track)
    }
}

/**
 * Convert a [TrackDTO] to a [Track] db object
 * TODO Put this somewhere else where it belongs
 */
fun TrackDTO.toTrack(): Track =
    Track(name, artists.first().name, album.name, uri, album.images.first().url)
