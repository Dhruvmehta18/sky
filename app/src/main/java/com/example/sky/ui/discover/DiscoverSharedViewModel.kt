package com.example.sky.ui.discover

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sky.ui.discover.model.PlaceContent

class DiscoverSharedViewModel : ViewModel() {

    private val _places = MutableLiveData<MutableList<PlaceContent.PlaceItem>>()
    val places: LiveData<MutableList<PlaceContent.PlaceItem>>
        get() = _places

    fun placesDataChanged(placesTemp: MutableList<PlaceContent.PlaceItem>) {
        _places.value = placesTemp
        Log.i("DiscoverViewModel", places.value.toString())
    }
}