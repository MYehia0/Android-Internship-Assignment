package com.example.androidinternshipassignment.ui.cities

import app.cash.turbine.test
import com.example.androidinternshipassignment.domain.models.City
import com.example.androidinternshipassignment.domain.usecases.GetCitiesInteractor
import com.example.androidinternshipassignment.domain.usecases.SearchInteractor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelTest {
    lateinit var viewModel: CitiesViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakeSearchInteractor: SearchInteractor = mockk(relaxed = true)
    private val fakeGetCitiesInteractor: GetCitiesInteractor = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCities should update uiState with search results`() = runTest {
        // Given
        val expectedCities = listOf(
            City(id = 1, name = "AnotherCity", country = "Country3", latitude = 50.0, longitude = 60.0),
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )
        coEvery { fakeGetCitiesInteractor() } returns expectedCities

        viewModel = CitiesViewModel(
            searchInteractor = fakeSearchInteractor,
            getCitiesInteractor = fakeGetCitiesInteractor
        )

        // When
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.searchResult).isEmpty()
            assertThat(initialState.errors).isNull()

            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.searchResult).isEmpty()
            assertThat(loadingState.errors).isNull()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.searchResult).isEqualTo(expectedCities)
            assertThat(successState.errors).isNull()
        }
    }

    @Test
    fun `loadCities when getCitiesInteractor throws exception should update uiState with error message`() =
        runTest {
            // Given
            coEvery { fakeGetCitiesInteractor() } throws IllegalStateException("Failed to load cities")
            viewModel = CitiesViewModel(
                searchInteractor = fakeSearchInteractor,
                getCitiesInteractor = fakeGetCitiesInteractor
            )
            // When & Then
            viewModel.uiState.test {
                val initialState = awaitItem()
                assertThat(initialState.isLoading).isFalse()
                assertThat(initialState.searchResult).isEmpty()
                assertThat(initialState.errors).isNull()

                val loadingState = awaitItem()
                assertThat(loadingState.isLoading).isTrue()
                assertThat(loadingState.searchResult).isEmpty()
                assertThat(loadingState.errors).isNull()

                val failureState = awaitItem()
                assertThat(failureState.isLoading).isFalse()
                assertThat(failureState.searchResult).isEmpty()
                assertThat(failureState.errors).isNotNull()
                assertThat(failureState.errors).isNotEmpty()
            }
        }

    @Test
    fun `searchCities should update uiState with search results`() = runTest {
        // Given
        val query = "City"
        val expectedCities = listOf(
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )
        val fakeCities = listOf(
            City(id = 1, name = "AnotherCity", country = "Country3", latitude = 50.0, longitude = 60.0),
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )
        coEvery { fakeGetCitiesInteractor() } returns fakeCities
        coEvery { fakeSearchInteractor(query) } returns expectedCities

        viewModel = CitiesViewModel(
            searchInteractor = fakeSearchInteractor,
            getCitiesInteractor = fakeGetCitiesInteractor
        )

        // When
        viewModel.searchCities(query)

        // Then
        viewModel.uiState.test {
            skipItems(3)

            val loadingSearchState = awaitItem()
            assertThat(loadingSearchState.isLoading).isTrue()
            assertThat(loadingSearchState.searchResult).isEqualTo(fakeCities)
            assertThat(loadingSearchState.errors).isNull()

            val successSearchState = awaitItem()
            assertThat(successSearchState.isLoading).isFalse()
            assertThat(successSearchState.searchResult).isEqualTo(expectedCities)
            assertThat(successSearchState.errors).isNull()
        }
    }

    @Test
    fun `searchCities with empty query should update uiState with search results`() = runTest {
        // Given
        val query = ""
        val expectedCities = listOf(
            City(id = 1, name = "AnotherCity", country = "Country3", latitude = 50.0, longitude = 60.0),
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )
        coEvery { fakeGetCitiesInteractor() } returns expectedCities
        coEvery { fakeSearchInteractor(query) } returns expectedCities

        viewModel = CitiesViewModel(
            searchInteractor = fakeSearchInteractor,
            getCitiesInteractor = fakeGetCitiesInteractor
        )

        // When
        viewModel.searchCities(query)

        // Then
        viewModel.uiState.test {
            skipItems(3)

            val loadingSearchState = awaitItem()
            assertThat(loadingSearchState.isLoading).isTrue()
            assertThat(loadingSearchState.searchResult).isEqualTo(expectedCities)
            assertThat(loadingSearchState.errors).isNull()

            val successSearchState = awaitItem()
            assertThat(successSearchState.isLoading).isFalse()
            assertThat(successSearchState.searchResult).isEqualTo(expectedCities)
            assertThat(successSearchState.errors).isNull()
        }
    }


    @Test
    fun `searchCities when searchInteractor throws exception should update uiState with error message`() = runTest {
        // Given
        val query = "City"
        val fakeCities = listOf(
            City(
                id = 1,
                name = "AnotherCity",
                country = "Country3",
                latitude = 50.0,
                longitude = 60.0
            ),
            City(id = 2, name = "City1", country = "Country1", latitude = 10.0, longitude = 20.0),
            City(id = 3, name = "City2", country = "Country2", latitude = 30.0, longitude = 40.0)
        )
        coEvery { fakeGetCitiesInteractor() } returns fakeCities
        coEvery { fakeSearchInteractor(query) } throws IllegalStateException("Failed to search cities")

        viewModel = CitiesViewModel(
            searchInteractor = fakeSearchInteractor,
            getCitiesInteractor = fakeGetCitiesInteractor
        )

        // When
        viewModel.searchCities(query)

        // Then
        viewModel.uiState.test {
            skipItems(3)

            val loadingSearchState = awaitItem()
            assertThat(loadingSearchState.isLoading).isTrue()
            assertThat(loadingSearchState.searchResult).isEqualTo(fakeCities)
            assertThat(loadingSearchState.errors).isNull()

            val failureSearchState = awaitItem()
            assertThat(failureSearchState.isLoading).isFalse()
            assertThat(failureSearchState.searchResult).isEqualTo(fakeCities)
            assertThat(failureSearchState.errors).isNotNull()
            assertThat(failureSearchState.errors).isNotEmpty()
        }
    }
}
