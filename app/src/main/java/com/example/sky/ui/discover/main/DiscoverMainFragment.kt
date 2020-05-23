package com.example.sky.ui.discover.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sky.R


class DiscoverMainFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoverMainFragment()
    }

    private lateinit var viewModel: DiscoverMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiscoverMainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
