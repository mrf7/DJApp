package com.mfriend.djapp.tempUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.mfriend.djapp.R
import com.mfriend.djapp.spotifyapi.models.User
import kotlinx.android.synthetic.main.api_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApiFragment : Fragment() {
    private val apiViewModel: ApiViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.api_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_fetch_user.setOnClickListener {
            apiViewModel.fetchUserClicked()
        }

        btn_fetch_playlists.setOnClickListener {
            val action =
                ApiFragmentDirections.actionShowPlaylists()
            findNavController().navigate(action)
        }
        apiViewModel.user.observe(this) { user: User ->
            tv_user.text = user.displayName
        }
    }
}
