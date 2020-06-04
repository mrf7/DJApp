/**
 * Application to initialize app level things like Koin
 *
 * Created by MFriend on 2020-01-27.
 */
package com.mfriend.djapp.common

import android.app.Application
import com.mfriend.djapp.BuildConfig
import com.mfriend.djapp.common.servicelocator.appModule
import com.mfriend.djapp.spotifyapi.SpotifyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Application instance used to setup app level thigns like service locators and logging
 */
class DJApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        startKoin {
            androidContext(this@DJApplication)
            androidLogger()

            modules(
                listOf(
                    SpotifyModule.get(),
                    appModule
                )
            )
        }
    }

    /**
     * Plant some trees for the one true god
     */
    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}