package com.mfriend.djapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mfriend.djapp.databinding.FragmentReviewRequestsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewRequestsFragment : Fragment() {
    private lateinit var binding: FragmentReviewRequestsBinding
    private val viewModel: ReviewRequestsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnAddSong.setOnClickListener {
            viewModel.addSongPressed()
        }
        binding.btnDecline.setOnClickListener {
            viewModel.declineSongPressed()
        }

    }

}
