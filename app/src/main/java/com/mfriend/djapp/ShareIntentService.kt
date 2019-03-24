package com.mfriend.djapp

import android.app.IntentService
import android.content.Intent
import android.util.Log

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class ShareIntentService : IntentService("ShareIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("IntentService", intent?.action)
        when (intent?.action) {
        }
    }
}
