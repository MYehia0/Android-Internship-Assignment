package com.example.androidinternshipassignment.data.datasources.file

import android.content.Context
import com.example.androidinternshipassignment.data.datasources.models.CityDto
import com.example.androidinternshipassignment.data.datasources.models.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject

class CitiesFileDataSource @Inject constructor (private val context: Context) {
    suspend fun fetchCitiesFromFile(): List<CityDto> {
        return withContext(Dispatchers.IO) {
            val jsonFile = context.assets.open(Constants.JSON_FILE_NAME)
            val reader = InputStreamReader(jsonFile)
            val cityListType = object : TypeToken<List<CityDto>>() {}.type
            val cityList: List<CityDto> = Gson().fromJson(reader, cityListType)
            cityList.sortedWith(compareBy<CityDto> { it.name }.thenBy { it.country })
        }
    }
}