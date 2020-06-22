package com.mfriend.djapp.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

/**
 * Receives cloud messages from firebase
 */
class MyMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Timber.d("New token $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d("Got a message $message")
    }
}
