package com.example.androidinternshipassignment.domain.usecases

import com.example.androidinternshipassignment.domain.models.City
import com.example.androidinternshipassignment.domain.repositories.CityRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SearchInteractorTest {

    private lateinit var searchInteractor: SearchInteractor
    private val fakeCities = listOf(
        City(id = 1, name = "AnotherCity", country = "Country3", latitude = 50.0, longitude = 60.0),
        City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
        City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
    )
    private val fakeCityRepository: CityRepository = mockk(relaxed = true) {
        coEvery { getCitiesFromMemory() } returns fakeCities
    }
     @BeforeEach
     fun setUp() {
         searchInteractor = SearchInteractor(fakeCityRepository)
     }

    @Test
    fun `search should throw exception when repository fails`() = runTest {
        // Given
        coEvery { fakeCityRepository.getCitiesFromMemory() } throws IllegalStateException("Cities not loaded in memory")

        // When & Then
        assertThrows<IllegalStateException> {
            searchInteractor.invoke("city")
        }
    }

    @Test
    fun `search with empty query should return all cities`() = runTest {
        // Given
        val expectedCities = fakeCities

        // When
        val result = searchInteractor.invoke("")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with blank query should return all cities`() = runTest {
        // Given
        val expectedCities = fakeCities

        // When
        val result = searchInteractor.invoke("   ")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with single character prefix Capital should return matching cities`() = runTest {
        // Given
        val expectedCities = listOf(
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )

        // When
        val result = searchInteractor.invoke("C")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with single character prefix Small should return matching cities`() = runTest {
        // Given
        val expectedCities = listOf(
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )

        // When
        val result = searchInteractor.invoke("c")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with valid prefix should return matching cities`() = runTest {
        // Given
        val expectedCities = listOf(
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )

        // When
        val result = searchInteractor.invoke("City")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with valid full name should return matching cities`() = runTest {
        // Given
        val expectedCities = listOf(
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0)
        )

        // When
        val result = searchInteractor.invoke("City1")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with prefix longer than any city name should return empty list`() = runTest {
        // Given
//        coEvery { fakeCityRepository.getCitiesFromMemory() } returns emptyList()

        // When
        val result = searchInteractor.invoke("City1ExtraLong")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `search with non-matching prefix should return empty list`() = runTest {
        // When
        val result = searchInteractor.invoke("NonExisting")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `search with case-insensitive prefix should return matching cities`() = runTest {
        // Given
        val expectedCities = listOf(
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )

        // When
        val result = searchInteractor.invoke("city")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedCities)
    }

    @Test
    fun `search with prefix that matches only one city should return that city`() = runTest {
        // Given
        val expectedCity = City(id = 1, name = "AnotherCity", country = "Country3", latitude = 50.0, longitude = 60.0)

        // When
        val result = searchInteractor.invoke("Another")

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result[0]).isEqualTo(expectedCity)
    }

    @Test
    fun `search with special characters should return empty list`() = runTest {
        // When
        val result = searchInteractor.invoke("@#$")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `search with valid prefix and repo is empty should return empty list`() = runTest {
        // Given
        coEvery { fakeCityRepository.getCitiesFromMemory() } returns emptyList()

        // When
        val result = searchInteractor.invoke("City")

        // Then
        assertThat(result).isEmpty()
    }

    // think more time about this test
    @Disabled
    @Test
    fun `search when memory is empty should fetch from file`() = runTest {
        coEvery { fakeCityRepository.getCitiesFromMemory() } returns emptyList()
        coEvery { fakeCityRepository.fetchCitiesFromFile() } returns listOf(
            City(id = 1, name = "Aswan", country = "Aswan", latitude = 70.0, longitude = 80.0),
        )

        val result = searchInteractor.invoke("Asw")

        assertThat(result).hasSize(1)
        assertThat(result.first().name).isEqualTo("Aswan")
        coVerify(exactly = 1) { fakeCityRepository.fetchCitiesFromFile() }
    }
}