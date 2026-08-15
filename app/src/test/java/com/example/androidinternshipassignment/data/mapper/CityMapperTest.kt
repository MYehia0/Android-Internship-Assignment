package com.example.androidinternshipassignment.data.mapper

import com.example.androidinternshipassignment.data.datasources.models.CityDto
import com.example.androidinternshipassignment.data.datasources.models.Coordinate
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CityMapperTest {

    lateinit var cityMapper: CityMapper
    @BeforeEach
    fun setUp() {
        cityMapper = CityMapper()
    }

    @Test
    fun `map should correctly map CityDto to City`() {
        // Given
        val cityDto = CityDto(
            id = 1,
            name = "Test City",
            country = "Test Country",
            coordinate = Coordinate(
                latitude = 10.0,
                longitude = 20.0
            )
        )

        // When
        val city = cityMapper.map(cityDto)

        // Then
        assertThat(city).isNotNull()
        assertThat(city.id).isEqualTo(cityDto.id)
        assertThat(city.name).isEqualTo(cityDto.name)
        assertThat(city.country).isEqualTo(cityDto.country)
        assertThat(city.latitude).isEqualTo(cityDto.coordinate.latitude)
        assertThat(city.longitude).isEqualTo(cityDto.coordinate.longitude)
    }

}