package com.example.androidinternshipassignment.domain.usecases

import com.example.androidinternshipassignment.domain.models.City
import com.example.androidinternshipassignment.domain.repositories.CityRepository
import javax.inject.Inject

class GetCitiesInteractor @Inject constructor(private val cityRepository: CityRepository) {
    suspend operator fun invoke (): List<City> = cityRepository.fetchCitiesFromFile()
}