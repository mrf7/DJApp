package com.mfriend.djapp.messaging

data class Party(
    val request: List<RequestDto>? = null
)

data class RequestDto(
    val songUri: String? = null
)