/**
 * TODO MRF Write Class header
 *
 * Created by MFriend on 2020-01-27.
 * Copyright (c) 2020 Lutron. All rights reserved.
 */
package com.mfriend.djapp

import com.mfriend.djapp.tempUi.ApiViewModel
import com.mfriend.djapp.tempUi.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { ApiViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
}