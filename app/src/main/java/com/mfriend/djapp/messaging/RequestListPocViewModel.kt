package com.mfriend.djapp.messaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel to show requests
 */
class RequestListPocViewModel(private val requestRepo: RequestsRepo) : ViewModel() {
    /**
     * Livedata of the requests
     */
    val requestsList = liveData<List<String>> {
        requestRepo.getRequests("A45542069").collect {
            Timber.d(it.toString())
            emit(it)
        }
    }

    fun addRequest(request: String) {
        viewModelScope.launch {
            Timber.d("Sending request $request")
            requestRepo.addRequest("A45542069", request)
            Timber.d("Finished request $request")
        }
    }
}