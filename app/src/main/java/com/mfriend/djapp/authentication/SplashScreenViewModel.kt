package com.mfriend.djapp.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mfriend.djapp.common.SharedPrefDataSource
import com.mfriend.djapp.common.UserMode
import com.mfriend.djapp.common.helper.Event

/**
 * View model for back end operations on the initial screen to start/join a party
 */
class SplashScreenViewModel(private val sharedPrefDataSource: SharedPrefDataSource) : ViewModel() {

    /**
     *  Event live data to tell the ui to navigate
     */
    val navigationEvent: LiveData<Event<NavDirections>>
        get() = _navigationEvent

    /**
     *  Event to tell ui to auth with spotify
     */
    val startAuthEvent: LiveData<Event<Unit>>
        get() = _startAuthEvent

    private val _startAuthEvent = MutableLiveData<Event<Unit>>()
    private val _navigationEvent = MutableLiveData<Event<NavDirections>>()

    /**
     * Call when authorization is successfull
     */
    fun onAuthSuccess(token: String) {
        sharedPrefDataSource.spotifyAuthToken = token
        sharedPrefDataSource.userMode = UserMode.Host
        _navigationEvent.value = Event(NavDirections.JoinParty)
    }

    /**
     * Call when start party is pressed
     */
    fun onStartPartyPressed() {
        if (sharedPrefDataSource.spotifyAuthToken != null) {
            sharedPrefDataSource.userMode = UserMode.Host
            _navigationEvent.value = Event(NavDirections.JoinParty)
        } else {
            _startAuthEvent.value = Event(Unit)
        }
    }

    /**
     * Called when join party is pressed
     */
    fun onJoinPartyPressed() {
        sharedPrefDataSource.userMode = UserMode.Attendee
        _navigationEvent.value = Event(NavDirections.CreateParty)
    }

    /**
     * Navigation directions to move away from the current screen
     */
    enum class NavDirections {
        JoinParty, CreateParty
    }
}