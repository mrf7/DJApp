package com.mfriend.djapp.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mfriend.djapp.common.helper.observeEvent
import com.mfriend.djapp.databinding.FragmentSplashBinding
import com.mfriend.djapp.spotifyapi.SpotifyModule
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Fragment to allow the user to sign in to their spotify account to perform requests that require
 * an auth token
 */
class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val splashViewModel: SplashScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set up the binding
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartParty.setOnClickListener {
            splashViewModel.onStartPartyPressed()
        }
        binding.btnJoinParty.setOnClickListener {
            splashViewModel.onJoinPartyPressed()
        }
        splashViewModel.navigationEvent.observeEvent(viewLifecycleOwner) {
            val direction = when (it) {
                SplashScreenViewModel.NavDirections.JoinParty -> SplashScreenFragmentDirections.showUsersPlaylists()
                SplashScreenViewModel.NavDirections.CreateParty -> TODO()
            }
            findNavController().navigate(direction)
        }

        splashViewModel.startAuthEvent.observeEvent(viewLifecycleOwner) {
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
            AuthenticationResponse.Type.TOKEN -> splashViewModel.onAuthSuccess(response)
            AuthenticationResponse.Type.ERROR -> handleResponseError(response)
            else -> Timber.e("Got different response type: ${response.type}")
        }
    }

    private fun handleResponseError(response: AuthenticationResponse) {
        Timber.d("response: ${response.error}")
    }

    companion object {
        private const val REQUEST_CODE = 69
    }
}