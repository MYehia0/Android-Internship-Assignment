package com.example.androidinternshipassignment.domain.usecases

import com.example.androidinternshipassignment.domain.models.City
import com.example.androidinternshipassignment.domain.repositories.CityRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetCitiesInteractorTest {

    private lateinit var getCitiesInteractor: GetCitiesInteractor
    private val fakeCities = listOf(
        City(
            name = "City1",
            country = "Country1",
            id = 1,
            latitude = 10.0,
            longitude = 20.0
        ),
        City(
            name = "City2",
            country = "Country2",
            id = 2,
            latitude = 30.0,
            longitude = 40.0
        )
    )
    private val fakeCityRepository: CityRepository = mockk(relaxed = true) {
        coEvery { getCitiesFromMemory() } returns fakeCities
    }

    @BeforeEach
    fun setUp() {
        getCitiesInteractor = GetCitiesInteractor(fakeCityRepository)
    }

    @Test
    fun `get cities should throw exception when repository fails`() = runTest {
        // Given
        coEvery { fakeCityRepository.fetchCitiesFromFile() } throws IllegalStateException("Failed to fetch cities")

        // When & That
        assertThrows<IllegalStateException> {
            getCitiesInteractor.invoke()
        }
    }

    @Test
    fun `get cities should return list of cities when cities are available`() = runTest {
        // Given
        val expectedCities = fakeCities
        coEvery { fakeCityRepository.fetchCitiesFromFile() } returns fakeCities

        // When
        val cities = getCitiesInteractor.invoke()

        // Then
        assertThat(cities).isNotEmpty()
        assertThat(cities).isEqualTo(expectedCities)
    }

    @Test
    fun `get cities should return empty list when no cities are available`() = runTest {
        // Given
        coEvery { fakeCityRepository.fetchCitiesFromFile() } returns emptyList()

        // When
        val cities = getCitiesInteractor.invoke()

        // Then
        assertThat(cities).isEmpty()
    }

    @Test
    fun `get cities should call repository exactly once`() = runTest {
        // Given
        coEvery { fakeCityRepository.fetchCitiesFromFile() } returns emptyList()

        // When
        getCitiesInteractor.invoke()

        // Then
        coVerify(exactly = 1) { fakeCityRepository.fetchCitiesFromFile() }
    }
}
