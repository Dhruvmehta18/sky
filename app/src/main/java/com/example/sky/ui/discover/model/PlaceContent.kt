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
            addItem(
                PlaceItem(
                    prediction.placeId,
                    prediction.getPrimaryText(null).toString(),
                    prediction.getFullText(null).toString()
                )
            )
        }

        Log.i("PlaceContent", PLACES.toString())
        return PLACES
    }

    data class PlaceItem(
        val placeId: String,
        val placeName: String,
        val placeSecondaryText: String
    ) {
        override fun toString(): String = placeName
    }
}
