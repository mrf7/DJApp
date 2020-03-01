package com.mfriend.djapp.tempUi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.mfriend.djapp.databinding.FragmentAddSongBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class AddSongFragment : Fragment() {
    private lateinit var binding: FragmentAddSongBinding
    private val args by navArgs<AddSongFragmentArgs>()

    private val addSongViewModel: AddSongViewModel by viewModel { parametersOf(args.selectedPlaylist) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddSong.setOnClickListener {
            addSongViewModel.addSong(binding.etSongLink.text.toString())
            binding.etSongLink.text.clear()
        }

        binding.btnPopulate.setOnClickListener {
            addSongViewModel.fillRequestsList()
        }

        addSongViewModel.requestResult.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        addSongViewModel.songs.observe(viewLifecycleOwner) {
            binding.tvSongs.text = it.joinToString(separator = "\n")
        }
    }

}
