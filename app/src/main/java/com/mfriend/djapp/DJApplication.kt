/**
 * TODO MRF Write Class header
 *
 * Created by MFriend on 2020-01-27.
 * Copyright (c) 2020 Lutron. All rights reserved.
 */
package com.mfriend.djapp

import android.app.Application
import com.mfriend.djapp.spotifyapi.ApiFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DJApplication : Application() {
    private val appModule = module {
        single {
            ApiFactory.getSpotifyService(get())
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin { }
    }
}