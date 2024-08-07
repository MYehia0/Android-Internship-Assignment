package com.example.androidinternshipassignment.data.datasources.memory

import com.example.androidinternshipassignment.data.datasources.models.CityDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitiesMemoryDataSource {
    private val cities = mutableListOf<CityDto>()

    fun cacheCitiesInMemory(items: List<CityDto>) {
        cities.clear()
        cities.addAll(items)
    }

    suspend fun getCitiesFromMemory(): List<CityDto> = withContext(Dispatchers.IO){
        cities
    }
}