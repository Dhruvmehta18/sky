package com.example.sky.ui.discover.model

import android.util.Log
import com.google.android.libraries.places.api.model.AutocompletePrediction
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceContent {

    var PLACES: MutableList<PlaceItem> = ArrayList()

    val PLACE_MAP: MutableMap<String, PlaceItem> = HashMap()

    private const val COUNT = 5

    private fun addItem(item: PlaceItem) {
        PLACES.add(item)
        PLACE_MAP[item.placeId] = item
    }

    private fun removeAllItem() {
        PLACES.clear()
        PLACE_MAP.clear()
    }

    fun setItems(autocompletePredictions: MutableList<AutocompletePrediction>): MutableList<PlaceItem> {
        removeAllItem()
        for (prediction in autocompletePredictions) {
//            Log.i(TAG, prediction.placeId)
//            Log.i(TAG, prediction.getPrimaryText(null).toString())
//            Log.i(TAG, prediction.placeTypes.toString())
            addItem(PlaceItem(prediction.placeId, prediction.getPrimaryText(null).toString()))
        }

        Log.i("PlaceContent", PLACES.toString())
        return PLACES
    }

    data class PlaceItem(
        val placeId: String,
        val placeName: String
    ) {
        override fun toString(): String = placeName
    }
}
