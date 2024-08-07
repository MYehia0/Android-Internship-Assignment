package com.example.androidinternshipassignment.domain.usecases

import com.example.androidinternshipassignment.data.repositories.CityRepository
import com.example.androidinternshipassignment.domain.mapper.CityMapper
import com.example.androidinternshipassignment.domain.models.City
import javax.inject.Inject

class GetCitiesInteractor @Inject constructor(private val cityRepository: CityRepository,private val cityMapper: CityMapper) {
    suspend operator fun invoke (): List<City> = cityRepository.fetchCitiesFromFile().map { cityMapper.map(it) }
}