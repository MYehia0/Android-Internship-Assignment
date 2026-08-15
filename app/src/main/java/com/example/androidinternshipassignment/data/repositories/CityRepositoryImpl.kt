package com.example.androidinternshipassignment.data.repositories

import com.example.androidinternshipassignment.data.datasources.file.CitiesFileDataSource
import com.example.androidinternshipassignment.data.mapper.CityMapper
import com.example.androidinternshipassignment.domain.models.City
import com.example.androidinternshipassignment.domain.repositories.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(private val fileDataSource: CitiesFileDataSource,
                                             private val cityMapper: CityMapper) : CityRepository {
    private var cachedCities: List<City>? = null

    override suspend fun fetchCitiesFromFile (): List<City> {
        cachedCities?.let { return it }
        val cities = fileDataSource.fetchCitiesFromFile()
        val mappedCities = cities.map { cityMapper.map(it) }
        cachedCities = mappedCities
        return mappedCities
    }

    override suspend fun getCitiesFromMemory (): List<City> = cachedCities ?: emptyList()
}