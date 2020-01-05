package com.mfriend.djapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val authRequest: AuthenticationRequest =
                AuthenticationRequest.Builder(
                    CLIENT_ID,
                    AuthenticationResponse.Type.TOKEN,
                    REDIRECT_URI
                )
                    .run {
                        setScopes(
                            arrayOf(
                                "user-library-modify",
                                "user-library-read",
                                "app-remote-control",
                                "playlist-read-collaborative",
                                "playlist-read-private",
                                "playlist-modify-public",
                                "playlist-modify-private"
                            )
                        )
                        build()
                    }
            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, authRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE) return

        val response = AuthenticationClient.getResponse(resultCode, data)

        when (response.type) {
            AuthenticationResponse.Type.TOKEN -> handleResponseSuccess(response)
            AuthenticationResponse.Type.ERROR -> handleResponseError(response)
            else -> Log.e(
                LOGGER_TAG,
                "Got different response type: ${response.type} "
            )
        }
    }

    private fun handleResponseSuccess(response: AuthenticationResponse) {
        Log.d(LOGGER_TAG, "response: ${response.accessToken} expires ${response.expiresIn}")
        val intent = Intent(this, ApiActivity::class.java).apply {
            putExtra(ApiActivity.KEY_API_TOKEN, response.accessToken)
        }
        startActivity(intent)
    }

    private fun handleResponseError(response: AuthenticationResponse) {
        Log.d(LOGGER_TAG, "response: ${response.error}")
    }

    companion object {
        const val REQUEST_CODE = 69
        const val REDIRECT_URI = "http://com.mfriend.djapp/callback"
        const val CLIENT_ID = "bc77027fdfd54c0091c11fcc1895c5dd"
        const val CLIENT_SECRET = "3be165a3b18a42d8b6e48db65dea8a92"
    }
}
