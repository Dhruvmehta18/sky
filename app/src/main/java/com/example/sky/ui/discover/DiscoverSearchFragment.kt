package com.example.sky.ui.discover

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sky.R
import com.example.sky.databinding.FragmentDiscoverSearchBinding
import com.google.android.gms.maps.model.LatLng


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class DiscoverSearchFragment : Fragment() {

    private val TAG = "DiscoverSearchFragment"

    // TODO: Customize parameters
    private var columnCount = 1
    private lateinit var discoverSearchViewModel: DiscoverSearchViewModel

    private val discoverSharedViewModel: DiscoverSharedViewModel by activityViewModels()
    private lateinit var binding: FragmentDiscoverSearchBinding
    private lateinit var discoverSearchObserver: DiscoverSearchViewModel.DiscoverSearchObserver
    private val REQUEST_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_discover_search, container, false)
        discoverSearchViewModel =
            ViewModelProvider(this, DiscoverSearchViewModelFactory(requireContext())).get(
                DiscoverSearchViewModel::class.java
            )
        setDiscoverSearchText("")
        binding.lifecycleOwner = this
        val requireContext = requireContext()
        // Set up the toolbar.
        binding.toolbarDiscover.setNavigationOnClickListener(
            DisocverSearchNavigationIconClickListener(
                requireContext,
                AccelerateDecelerateInterpolator(),
                ContextCompat.getDrawable(
                    requireContext,
                    R.drawable.quantum_ic_arrow_back_grey600_24
                )
            )
        ) // Menu close icon

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        // Set the adapter
        val adapter = DiscoverSearchItemRecyclerViewAdapter(
            PlaceItemListener({ placeId ->
                Toast.makeText(requireContext, placeId, Toast.LENGTH_LONG).show()
            }, { placeName ->
                Toast.makeText(requireContext, placeName, Toast.LENGTH_LONG).show()
                setDiscoverSearchText(placeName)
            })
        )
        binding.placesAutocompleteList.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        val sharedPref = requireContext.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )!!
        val latitude = sharedPref.getFloat(getString(R.string.latitude), 32f)
        val longitude = sharedPref.getFloat(getString(R.string.longitude), 32f)
        discoverSearchViewModel.locationDataChanged(
            LatLng(
                latitude.toDouble(),
                longitude.toDouble()
            )
        )
        binding.placesAutocompleteList.adapter = adapter
        discoverSearchViewModel.places.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it?.toMutableList())
            Log.i("DiscoverSearch", it.toString())
        })
        discoverSearchViewModel.query.observe(viewLifecycleOwner, Observer { query ->
            if (query.isBlank()) {
                binding.micSearchPlaces.setImageResource(R.drawable.ic_mic_black_24dp)
                binding.micSearchPlaces.setOnClickListener {

                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

                    try {
                        //Start the Activity and wait for the response//
                        startActivityForResult(intent, REQUEST_CODE)
                    } catch (a: ActivityNotFoundException) {
                    }
                }
            } else {
                binding.micSearchPlaces.setImageResource(R.drawable.ic_close_black_24dp)
                binding.micSearchPlaces.setOnClickListener {
                    setDiscoverSearchText("")
                }
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setDiscoverSearchText(query: String) {
        binding.discoverSearch = discoverSearchViewModel.DiscoverSearchObserver(query)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode == RESULT_OK && null != data) {
                    val result =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (result != null && result.isNotEmpty())
                        setDiscoverSearchText(result[0])
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            DiscoverSearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
