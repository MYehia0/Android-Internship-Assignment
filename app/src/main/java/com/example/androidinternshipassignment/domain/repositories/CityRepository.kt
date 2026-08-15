package com.example.androidinternshipassignment.domain.repositories

import com.example.androidinternshipassignment.domain.models.City

interface CityRepository {
    suspend fun fetchCitiesFromFile (): List<City>
    suspend fun getCitiesFromMemory (): List<City>
}