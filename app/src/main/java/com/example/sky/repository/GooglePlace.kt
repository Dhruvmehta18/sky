package com.example.sky.repository

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class GooglePlace(context: Context) {
    private val _placesClient: PlacesClient

    init {

        // Initialize the SDK
        Places.initialize(context, "AIzaSyDSNOWDMz4Ab-m1U1O9HFznLC6YGJhgY4I")
        // Create a new Places client instance
        _placesClient = Places.createClient(context)
    }

    fun getInstance(): PlacesClient {
        return _placesClient
    }
}