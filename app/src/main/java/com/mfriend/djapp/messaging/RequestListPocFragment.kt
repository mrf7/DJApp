package com.mfriend.djapp.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfriend.djapp.databinding.RequestListPocFragmentBinding
import com.mfriend.djapp.selectplaylist.PlaylistAdapter
import com.mfriend.djapp.spotifyapi.models.PlaylistDto
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Test implementation to show the list of requests from firebase cloud db
 */
class RequestListPocFragment : Fragment() {
    private val viewModel: RequestListPocViewModel by viewModel()
    private lateinit var binding: RequestListPocFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RequestListPocFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapterA = PlaylistAdapter(emptyList())
        binding.rvRequests.apply {
            val dividerItemDecoration =
                DividerItemDecoration(binding.rvRequests.context, LinearLayoutManager.VERTICAL)

            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterA
        }

        viewModel.requestsList.observe(viewLifecycleOwner) { requests ->
            adapterA.items = requests.map { PlaylistDto(it, "", "", it, false, "") }
            adapterA.notifyDataSetChanged()
        }
        binding.addRequest.setOnClickListener {
            viewModel.addRequest(binding.etRequestData.text.toString())
        }
    }
}
