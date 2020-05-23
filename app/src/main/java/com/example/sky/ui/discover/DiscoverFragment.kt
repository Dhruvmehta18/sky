package com.example.sky.ui.discover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sky.R
import com.example.sky.databinding.FragmentDiscoverBinding
import com.example.sky.repository.GooglePlace
import com.example.sky.ui.discover.main.DiscoverMainFragment
import com.example.sky.ui.discover.model.PlaceContent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient


class DiscoverFragment : Fragment(), DiscoverSearchFragment.OnListFragmentInteractionListener {

    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var token: AutocompleteSessionToken

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
        binding.discoverSearch = discoverViewModel.DiscoverSearchObserver("")
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        token = AutocompleteSessionToken.newInstance()

        placesClient = GooglePlace(requireContext()).getInstance()

        discoverViewModel.query.observe(viewLifecycleOwner, Observer {
            updateSearchList(it)
        })

        binding.searchDiscover.setOnFocusChangeListener { view, focus ->
            Toast.makeText(context, "sdsdsdsdsdsd", Toast.LENGTH_SHORT).show()
            if (focus) {
                childFragmentManager.commit {
                    replace<DiscoverSearchFragment>(R.id.discoverView, null)
                    addToBackStack(null)
                }
            } else {
                childFragmentManager.commit {
                    replace<DiscoverMainFragment>(R.id.discoverView, null)
                    addToBackStack(null)
                }
            }
        }
        return binding.root
    }

    private fun updateSearchList(query: String) {
        // Create a RectangularBounds object.
        val bounds = RectangularBounds.newInstance(
            LatLng(-33.880490, 151.184363),
            LatLng(-33.858754, 151.229596)
        )
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds) //.setLocationRestriction(bounds)
                .setOrigin(LatLng(-33.8749937, 151.2041382))
                .setCountries("AU", "NZ")
                .setTypeFilter(TypeFilter.CITIES)
                .setSessionToken(token)
                .setQuery(query)
                .build()
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (prediction in response.autocompletePredictions) {
                Log.i(TAG, prediction.placeId)
                Log.i(TAG, prediction.getPrimaryText(null).toString())
                Log.i(TAG, prediction.placeTypes.toString())
            }
            discoverSharedViewModel.placesDataChanged(PlaceContent.setItems(response.autocompletePredictions))
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e(TAG, "Place not found: " + exception.statusCode)
            }
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is DiscoverSearchFragment) {
            fragment.setOnPlaceSelectedListener(this)
        }
    }

    override fun onListFragmentInteraction(item: PlaceContent.PlaceItem?) {
        if (item != null) {
            Log.i(TAG, item.placeName)
        }
    }
}
