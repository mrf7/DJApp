/**
 * Stores app level modules for koin
 *
 * Created by MFriend on 2020-01-27.
 */
package com.mfriend.djapp.common.servicelocator

import android.content.Context
import com.mfriend.djapp.authentication.SplashScreenViewModel
import com.mfriend.djapp.common.SharedPrefDataSource
import com.mfriend.djapp.common.db.AppDatabase
import com.mfriend.djapp.reviewrequests.ReviewRequestRepo
import com.mfriend.djapp.reviewrequests.ReviewRequestsViewModel
import com.mfriend.djapp.selectplaylist.PlaylistViewModel
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin Module containt all the App level resources like viewModels, daos, and repos
 * Feel like theres probably a better way to do that
 */
val appModule = module {
    viewModel { PlaylistViewModel(get()) }
    viewModel { (playlistDto: PlaylistDto) ->
        ReviewRequestsViewModel(get(), playlistDto)
    }
    viewModel { SplashScreenViewModel(get()) }
    // DB dependencies
    factory { AppDatabase.buildDb(androidApplication()) }
    factory { get<AppDatabase>().trackDao() }
    single { ReviewRequestRepo(get(), get()) }

    val FILE_NAME: String = "shared"
    single {
        SharedPrefDataSource(
            androidApplication().getSharedPreferences(
                FILE_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}