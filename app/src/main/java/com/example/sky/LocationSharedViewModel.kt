package com.example.sky

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationSharedViewModel : ViewModel() {
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location
}