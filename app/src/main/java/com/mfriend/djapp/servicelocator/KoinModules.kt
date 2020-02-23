/**
 * Stores app level modules for koin
 *
 * Created by MFriend on 2020-01-27.
 */
package com.mfriend.djapp.servicelocator

import com.mfriend.djapp.PlaylistViewModel
import com.mfriend.djapp.db.AppDatabase
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import com.mfriend.djapp.tempUi.AddSongViewModel
import com.mfriend.djapp.tempUi.ApiViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ApiViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
    viewModel { (playlistDto: PlaylistDto) ->
        AddSongViewModel(
            get(),
            get(),
            playlistDto
        )
    }
    // DB dependencies
    factory { AppDatabase.buildDb(androidApplication()) }
    factory { get<AppDatabase>().trackDao() }
}