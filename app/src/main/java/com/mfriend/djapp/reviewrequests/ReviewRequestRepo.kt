package com.mfriend.djapp.reviewrequests

import com.mfriend.djapp.spotifyapi.SpotifyService
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.spotifyapi.models.TrackDTO

/**
 * Repository for interactions with the network and db related to reviewing song request
 */
class ReviewRequestRepo(private val spotifyService: SpotifyService) {

    /**
     * Adds [trackDTO] to [playlistDto]
     */
    suspend fun addSongToPlaylist(trackDTO: TrackDTO, playlistDto: PlaylistDto) {
        spotifyService.addSong(playlistDto.id, trackDTO.uri)
    }

    /**
     * Fetches the users most listened to tracks
     */
    suspend fun getUsersTopTracks(): List<TrackDTO> {
        return spotifyService.getUsersTopTracks().items
    }

}