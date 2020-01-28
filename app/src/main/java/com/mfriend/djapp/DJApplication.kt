/**
 * TODO MRF Write Class header
 *
 * Created by MFriend on 2020-01-27.
 * Copyright (c) 2020 Lutron. All rights reserved.
 */
package com.mfriend.djapp

import android.app.Application
import com.mfriend.djapp.spotifyapi.SpotifyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DJApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DJApplication)
            androidLogger()
            modules(listOf(SpotifyModule.get(), appModule))
        }
    }
}