package com.example.androidinternshipassignment.data.repositories

import com.example.androidinternshipassignment.data.datasources.file.CitiesFileDataSource
import com.example.androidinternshipassignment.data.datasources.memory.CitiesMemoryDataSource
import com.example.androidinternshipassignment.data.datasources.models.CityDto
import javax.inject.Inject

class CityRepository @Inject constructor(private val fdb: CitiesFileDataSource,
                                         private val mdb: CitiesMemoryDataSource
){
    suspend fun fetchCitiesFromFile (): List<CityDto> {
        mdb.cacheCitiesInMemory(fdb.fetchCitiesFromFile())
        return getCitiesFromMemory()
    }

    suspend fun getCitiesFromMemory (): List<CityDto> = mdb.getCitiesFromMemory()
}