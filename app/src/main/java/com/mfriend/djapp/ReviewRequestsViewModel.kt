package com.mfriend.djapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfriend.djapp.spotifyapi.models.TrackDTO

class ReviewRequestsViewModel : ViewModel() {

    /**
     *  Shows the currently seclect track
     */
    val currentTrack: LiveData<TrackDTO>
        get() = _currentTrack

    private val _currentTrack = MutableLiveData<TrackDTO>()

    fun addSongPressed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun declineSongPressed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
