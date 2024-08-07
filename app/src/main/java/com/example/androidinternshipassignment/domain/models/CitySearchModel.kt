package com.example.androidinternshipassignment.domain.models

data class CitySearchModel (
    val searchResult: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val errors: String? = null
)
