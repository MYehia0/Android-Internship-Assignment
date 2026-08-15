package com.example.androidinternshipassignment.data.datasources.file

import android.content.Context
import android.content.res.AssetManager
import com.example.androidinternshipassignment.data.datasources.models.Constants
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonSyntaxException
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.IOException

class CitiesFileDataSourceTest {
    lateinit var citiesFileDataSource: CitiesFileDataSource
    private val mockContext: Context = mockk()
    private val mockAssetManager: AssetManager = mockk()

    @BeforeEach
    fun setUp() {
        every { mockContext.assets } returns mockAssetManager
        citiesFileDataSource = CitiesFileDataSource(mockContext)
    }

    @Test
    fun `fetchCitiesFromFile should parse json and return sorted list`() = runTest{
        // Given
        val fakeJson = """
            [
                {"id": 1, "name": "Zambia City", "country": "Z", "coord": {"lat": 10.0, "lon": 20.0}},
                {"id": 2, "name": "Cairo", "country": "Egypt", "coord": {"lat": 30.0, "lon": 31.0}},
                {"id": 3, "name": "Cairo", "country": "America", "coord": {"lat": 40.0, "lon": 41.0}}
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
        every { mockAssetManager.open(Constants.JSON_FILE_NAME) } returns inputStream

        // When
        val result = citiesFileDataSource.fetchCitiesFromFile()

        // Then
        assertThat(result).hasSize(3)

        assertThat(result[0].name).isEqualTo("Cairo")
        assertThat(result[0].country).isEqualTo("America")

        assertThat(result[1].name).isEqualTo("Cairo")
        assertThat(result[1].country).isEqualTo("Egypt")

        assertThat(result[2].name).isEqualTo("Zambia City")
        assertThat(result[2].country).isEqualTo("Z")
    }

    @Test
    fun `fetchCitiesFromFile should return empty list when json is empty`() = runTest {
        // Given
        val fakeJson = "[]"
        val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
        every { mockAssetManager.open(Constants.JSON_FILE_NAME) } returns inputStream

        // When
        val result = citiesFileDataSource.fetchCitiesFromFile()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `fetchCitiesFromFile should throw exception when json is malformed`() = runTest {
        // Given
        val fakeJson = "{ malformed json }"
        val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
        every { mockAssetManager.open(Constants.JSON_FILE_NAME) } returns inputStream

        // When
        val exception = runCatching { citiesFileDataSource.fetchCitiesFromFile() }.exceptionOrNull()

        // Then
        assertThat(exception).isNotNull()
        assertThat(exception).isInstanceOf(JsonSyntaxException::class.java)
    }

    @Test
    fun `fetchCitiesFromFile should throw exception when file is not found`() = runTest {
        // Given
        every { mockAssetManager.open(Constants.JSON_FILE_NAME) } throws IOException("File not found")

        // When
        val exception = runCatching { citiesFileDataSource.fetchCitiesFromFile() }.exceptionOrNull()

        // Then
        assertThat(exception).isNotNull()
        assertThat(exception).isInstanceOf(IOException::class.java)
    }

    @Test
    fun `fetchCitiesFromFile should throw exception when json is completely empty`() = runTest {
        // Given
        val fakeJson = ""
        val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
        every { mockAssetManager.open(Constants.JSON_FILE_NAME) } returns inputStream

        // When
        val result = citiesFileDataSource.fetchCitiesFromFile()

        // Then
        assertThat(result).isEmpty()
    }



}