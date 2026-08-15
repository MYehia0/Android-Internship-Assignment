package com.example.androidinternshipassignment.data.repositories

import com.example.androidinternshipassignment.data.datasources.file.CitiesFileDataSource
import com.example.androidinternshipassignment.data.datasources.models.CityDto
import com.example.androidinternshipassignment.data.datasources.models.Coordinate
import com.example.androidinternshipassignment.data.mapper.CityMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CityRepositoryImplTest {
    private val cityMapper = CityMapper()
    private val citiesFileDataSource:CitiesFileDataSource = mockk(relaxed = true)
    lateinit var cityRepositoryImpl: CityRepositoryImpl

    @BeforeEach
    fun setUp() {
        cityRepositoryImpl = CityRepositoryImpl(citiesFileDataSource, cityMapper)
    }

    @Test
    fun `fetchCitiesFromFile when file data source throws exception should propagate exception`() = runTest {
        // Given
        coEvery { citiesFileDataSource.fetchCitiesFromFile() } throws IllegalStateException("Failed to fetch cities")

        // When & Then
        assertThrows<IllegalStateException> {
            cityRepositoryImpl.fetchCitiesFromFile()
        }
    }

    @Test
    fun `fetchCitiesFromFile when cache is empty should fetch cities from file and cache in memory`() = runTest{
        // Given
        val fakeCitiesDto = listOf(
            CityDto(id = 1, name = "City1", country = "Country1", coordinate = Coordinate(latitude = 10.0, longitude = 20.0)),
            CityDto(id = 2, name = "City2", country = "Country2", coordinate = Coordinate(latitude = 30.0, longitude = 40.0))
        )
        coEvery { citiesFileDataSource.fetchCitiesFromFile() } returns fakeCitiesDto

        // When
        val result = cityRepositoryImpl.fetchCitiesFromFile()

        // Then
        coVerify(exactly = 1) { citiesFileDataSource.fetchCitiesFromFile() }
        val expectedCities = fakeCitiesDto.map { cityMapper.map(it) }
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `fetchCitiesFromFile when cache is not empty should return cached cities`() = runTest {
        // Given
        val fakeCitiesDto = listOf(
            CityDto(id = 1, name = "City1", country = "Country1", coordinate = Coordinate(latitude = 10.0, longitude = 20.0)),
            CityDto(id = 2, name = "City2", country = "Country2", coordinate = Coordinate(latitude = 30.0, longitude = 40.0))
        )
        coEvery { citiesFileDataSource.fetchCitiesFromFile() } returns fakeCitiesDto

        // When
        val firstFetchResult = cityRepositoryImpl.fetchCitiesFromFile()
        val secondFetchResult = cityRepositoryImpl.fetchCitiesFromFile()

        // Then
        coVerify(exactly = 1) { citiesFileDataSource.fetchCitiesFromFile() }
        assertThat(secondFetchResult).isEqualTo(firstFetchResult)
    }

    @Test
    fun `getCitiesFromMemory when cache is empty should return empty list`() = runTest {
        // Given
        // No cities are fetched yet, so cache is empty

        // When
        val result = cityRepositoryImpl.getCitiesFromMemory()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getCitiesFromMemory when cache is not empty should return cached cities`() = runTest {
        // Given
        val fakeCitiesDto = listOf(
            CityDto(id = 1, name = "City1", country = "Country1", coordinate = Coordinate(latitude = 10.0, longitude = 20.0)),
            CityDto(id = 2, name = "City2", country = "Country2", coordinate = Coordinate(latitude = 30.0, longitude = 40.0))
        )
        coEvery { citiesFileDataSource.fetchCitiesFromFile() } returns fakeCitiesDto

        // When
        cityRepositoryImpl.fetchCitiesFromFile() // Populate cache
        val result = cityRepositoryImpl.getCitiesFromMemory()

        // Then
        val expectedCities = fakeCitiesDto.map { cityMapper.map(it) }
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(2)
        assertThat(result).isEqualTo(expectedCities)
    }
}