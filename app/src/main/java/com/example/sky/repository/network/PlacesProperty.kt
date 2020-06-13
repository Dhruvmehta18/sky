package com.example.sky.repository.network

import com.squareup.moshi.Json

data class PlacesProperty(
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double,
    val rating: Int
)