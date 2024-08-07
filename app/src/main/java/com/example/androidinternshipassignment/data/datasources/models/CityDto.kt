package com.example.androidinternshipassignment.data.datasources.models

import com.google.gson.annotations.SerializedName

data class Coordinate(
    @SerializedName("lon") val longitude: Double,
    @SerializedName("lat") val latitude: Double
)

data class CityDto(
    @SerializedName("country") val country: String,
    @SerializedName("name") val name: String,
    @SerializedName("_id") val id: Int,
    @SerializedName("coord") val coordinate: Coordinate
)