package com.example.sky.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.sky.R
import com.example.sky.databinding.FragmentDiscoverBinding


class DiscoverFragment : Fragment() {

    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var binding: FragmentDiscoverBinding

    private val discoverSharedViewModel: DiscoverSharedViewModel by activityViewModels()
    private val TAG = "DiscoverFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        discoverViewModel =
            ViewModelProvider(this).get(DiscoverViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)
        binding.discoverViewModel = discoverViewModel
        binding.searchDiscoverText.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_navigation_discover_to_navigation_discover_search)
        }
        return binding.root
    }
}
