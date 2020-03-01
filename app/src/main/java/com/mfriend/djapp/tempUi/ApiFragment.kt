package com.mfriend.djapp.tempUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.mfriend.djapp.databinding.ApiFragmentBinding
import com.mfriend.djapp.spotifyapi.models.UserDto
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApiFragment : Fragment() {
    private lateinit var binding: ApiFragmentBinding
    private val apiViewModel: ApiViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ApiFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.btnFetchUser.setOnClickListener {
            apiViewModel.fetchUserClicked()
        }

        binding.btnFetchPlaylists.setOnClickListener {
            val action =
                ApiFragmentDirections.actionShowPlaylists()
            findNavController().navigate(action)
        }
        apiViewModel.userDto.observe(this) { userDto: UserDto ->
            binding.tvUser.text = userDto.displayName
        }
    }
}
