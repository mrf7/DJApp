/**
 * Stores app level modules for koin
 *
 * Created by MFriend on 2020-01-27.
 */
package com.mfriend.djapp

import com.mfriend.djapp.spotifyapi.models.Playlist
import com.mfriend.djapp.tempUi.ApiViewModel
import com.mfriend.djapp.tempUi.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ApiViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
    viewModel { (playlist: Playlist) -> AddSongViewModel(get(), playlist) }
}