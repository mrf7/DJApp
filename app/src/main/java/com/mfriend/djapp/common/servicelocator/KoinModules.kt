/**
 * Stores app level modules for koin
 *
 * Created by MFriend on 2020-01-27.
 */
package com.mfriend.djapp.common.servicelocator

import com.mfriend.djapp.common.db.AppDatabase
import com.mfriend.djapp.reviewrequests.ReviewRequestRepo
import com.mfriend.djapp.reviewrequests.ReviewRequestsViewModel
import com.mfriend.djapp.selectplaylist.PlaylistViewModel
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PlaylistViewModel(get()) }
    viewModel { (playlistDto: PlaylistDto) ->
        ReviewRequestsViewModel(get(), playlistDto)
    }
    // DB dependencies
    factory { AppDatabase.buildDb(androidApplication()) }
    factory { get<AppDatabase>().trackDao() }
    single { ReviewRequestRepo(get()) }
}