package com.mfriend.djapp.tempUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.AuthFragment
import com.mfriend.djapp.R
import com.mfriend.djapp.spotifyapi.models.User
import kotlinx.android.synthetic.main.api_fragment.*

class ApiFragment : Fragment() {
    private val args: ApiFragmentArgs by navArgs()

    private val viewModel: ApiViewModel by viewModels {
        ApiViewModel.ApiViewModelFactory(args.authToken)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.api_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_fetch_user.setOnClickListener {
            viewModel.fetchUserClicked()
        }

        btn_fetch_playlists.setOnClickListener {
            val action =
                ApiFragmentDirections.actionApiFragmentToPlaylistFragment(args.authToken)
            findNavController().navigate(action)
        }
        viewModel.user.observe(this) { user: User ->
            tv_user.text = user.displayName
        }
    }
}
