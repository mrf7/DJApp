package com.mfriend.djapp.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mfriend.djapp.common.helper.extensions.LOGGER_TAG
import com.mfriend.djapp.databinding.FragmentAuthBinding
import com.mfriend.djapp.spotifyapi.SpotifyModule
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import org.koin.android.ext.android.getKoin
import org.koin.core.logger.KOIN_TAG

/**
 * Fragment to allow the user to sign in to their spotify account to perform requests that require
 * an auth token
 */
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set up the binding
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val authRequest = SpotifyModule.getRequest()
            val authIntent =
                AuthenticationClient.createLoginActivityIntent(requireActivity(), authRequest)
            startActivityForResult(
                authIntent,
                REQUEST_CODE
            )
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
        val action = AuthFragmentDirections.showUsersPlaylists()
        Log.d("MRF", "Navigating to API fragment.")
        findNavController().navigate(action)
    }

    private fun handleResponseError(response: AuthenticationResponse) {
        Log.d(LOGGER_TAG, "response: ${response.error}")
    }

    companion object {
        const val REQUEST_CODE = 69
    }
}
