package com.mfriend.djapp.common.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe

/**
 * Data wrapper to represent an Event that should only be handled once.
 */
class Event<out T>(private val content: T) {
    /**
     * Flag to determine if the event has been processed
     */
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again. After this function completes [hasBeenHandled]
     * will be true
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled. Will not change [hasBeenHandled]
     */
    fun peekContent(): T = content
}

/**
 * Helper function to observe LiveData that emits [Event]s. Ensures [onUpdate] is only called once
 * per [Event] emitted, and thus it wont be redelivered to new observers or owners that reobserve
 */
fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, onUpdate: (T) -> Unit) {
    this.observe(owner) { event ->
        event.getContentIfNotHandled()?.let(onUpdate)
    }
}