package com.example.androidinternshipassignment.domain.usecases

import com.example.androidinternshipassignment.data.repositories.CityRepository
import com.example.androidinternshipassignment.domain.mapper.CityMapper
import com.example.androidinternshipassignment.domain.models.City
import javax.inject.Inject

class SearchInteractor @Inject constructor(private val cityRepository: CityRepository, private val cityMapper: CityMapper) {
    suspend operator fun invoke(query: String): List<City> = if (query.isBlank() || query.isEmpty()) {
        cityRepository.getCitiesFromMemory().map { cityMapper.map(it) }
    } else {
        cityRepository.getCitiesFromMemory().map { cityMapper.map(it) }.filter {
            it.name.startsWith(query, ignoreCase = true)
        }
    }
}