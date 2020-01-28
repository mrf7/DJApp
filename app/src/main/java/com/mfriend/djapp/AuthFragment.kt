package com.mfriend.djapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.android.ext.android.getKoin
import org.koin.core.logger.KOIN_TAG

/**
 * A simple [Fragment] subclass.
 */
class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            val authIntent =
                AuthenticationClient.createLoginActivityIntent(requireActivity(), authRequest)
            startActivityForResult(authIntent, REQUEST_CODE)
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
        Log.d(KOIN_TAG, "Setting koin property for auth token")
        getKoin().setProperty("authToken", response.accessToken)
        val action = AuthFragmentDirections.actionAuthFragmentToApiFragment()
        Log.d("MRF", "Navigating to api fragment")
        findNavController().navigate(action)
    }

    private fun handleResponseError(response: AuthenticationResponse) {
        Log.d(LOGGER_TAG, "response: ${response.error}")
    }

    companion object {
        const val REQUEST_CODE = 68
        const val REDIRECT_URI = "http://com.mfriend.djapp/callback"
        const val CLIENT_ID = "bc77027fdfd54c0091c11fcc1895c5dd"
        const val CLIENT_SECRET = "2be165a3b18a42d8b6e48db65dea8a92"
    }
}
