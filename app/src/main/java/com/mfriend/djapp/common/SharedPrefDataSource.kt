package com.mfriend.djapp.common

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Wrapper for shared preferences
 */
class SharedPrefDataSource(private val sharedPreferences: SharedPreferences) {
    /**
     * Current [UserMode]
     */
    var userMode: UserMode
        get() = UserMode.valueOf(
            requireNotNull(sharedPreferences.getString(KEY_USER_MODE, UserMode.None.name))
        )
        set(value) = put(KEY_USER_MODE, value.name)

    /**
     * The code of the currently joined party, or null if there is no current party
     */
    var currentPartyCode: String?
        get() = getStringOrNull(KEY_PARTY_CODE)
        set(value) = put(KEY_PARTY_CODE, value)

    /**
     * Puts [value] in shared preferences under [key]
     */
    private fun put(key: String, value: String?) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    /**
     * Gets string associated with [key] or null
     */
    private fun getStringOrNull(key: String): String? = sharedPreferences.getString(key, null)

    private companion object {
        const val KEY_PARTY_CODE: String = "party code"
        const val KEY_USER_MODE: String = "party code"
    }
}

/**
 * Possible User modes
 */
enum class UserMode {
    Host, Attendee, None
}