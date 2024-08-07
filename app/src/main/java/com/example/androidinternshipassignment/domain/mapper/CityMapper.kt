package com.example.androidinternshipassignment.domain.mapper

import com.example.androidinternshipassignment.data.datasources.models.CityDto
import com.example.androidinternshipassignment.domain.models.City
import javax.inject.Inject

class CityMapper @Inject constructor(): Mapper<CityDto, City> {
    override fun map(input: CityDto): City {
        return City(
            id = input.id,
            name = input.name,
            country = input.country,
            latitude = input.coordinate.latitude,
            longitude = input.coordinate.longitude
        )
    }
}