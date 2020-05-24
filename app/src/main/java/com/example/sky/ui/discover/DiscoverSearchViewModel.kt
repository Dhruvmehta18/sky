package com.example.sky.ui.discover

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sky.repository.GooglePlace
import com.example.sky.ui.discover.model.PlaceContent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class DiscoverSearchViewModel(context: Context) : ViewModel() {
    private var placesClient: PlacesClient = GooglePlace(context).getInstance()
    private var token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()
    private val _query = MutableLiveData<String>()
    private val TAG = "DiscoverSearchViewModel"
    private val query: LiveData<String>
        get() = _query
    private val _places = MutableLiveData<MutableList<PlaceContent.PlaceItem>>()
    val places: LiveData<MutableList<PlaceContent.PlaceItem>>
        get() = _places
    private val _location = MutableLiveData<LatLng>()
    private val location: LiveData<LatLng>
        get() = _location

    fun locationDataChanged(location: LatLng) {
        _location.value = location
        Log.i(TAG, _location.value.toString())
    }

    private fun placesDataChanged(placesTemp: MutableList<PlaceContent.PlaceItem>) {
        _places.value = placesTemp
        Log.i("DiscoverViewModel", places.value.toString())
    }

    fun searchQueryChanged(query: String) {
        _query.value = query
        updateSearchList()
    }

    private fun updateSearchList() {
        // Create a RectangularBounds object.
        val bounds = RectangularBounds.newInstance(
            LatLng(location.value!!.latitude + 5 / 110, location.value!!.longitude + 5 / 110),
            LatLng(location.value!!.latitude - 5 / 110, location.value!!.longitude - 5 / 110)
        )
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds) //.setLocationRestriction(bounds)
                .setOrigin(location.value)
                .setTypeFilter(TypeFilter.CITIES)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query.value)
                .build()
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (prediction in response.autocompletePredictions) {
                Log.i(TAG, prediction.placeId)
                Log.i(TAG, prediction.getPrimaryText(null).toString())
                Log.i(TAG, prediction.placeTypes.toString())
            }
            placesDataChanged(PlaceContent.setItems(response.autocompletePredictions))
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e(TAG, "Place not found: " + exception.statusCode)
            }
        }
    }

    inner class DiscoverSearchObserver(private var query: String) :
        BaseObservable() {

        var search: String
            @Bindable get() = query
            set(value) {
                query = value
                Log.i(TAG, query)
                notifyPropertyChanged(BR.discoverSearch)
                searchQueryChanged(value)
            }
    }
}