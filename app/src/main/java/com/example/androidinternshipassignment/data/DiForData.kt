package com.example.androidinternshipassignment.data

import android.content.Context
import com.example.androidinternshipassignment.data.datasources.file.CitiesFileDataSource
import com.example.androidinternshipassignment.data.datasources.memory.CitiesMemoryDataSource
import com.example.androidinternshipassignment.data.repositories.CityRepository
import com.example.androidinternshipassignment.domain.mapper.CityMapper
import com.example.androidinternshipassignment.domain.usecases.GetCitiesInteractor
import com.example.androidinternshipassignment.domain.usecases.SearchInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiForData {
    @Provides
    @Singleton
    fun provideCitiesFileDataSource(@ApplicationContext context: Context) = CitiesFileDataSource(context)

    @Provides
    @Singleton
    fun provideCitiesMemoryDataSource() = CitiesMemoryDataSource()

    @Provides
    @Singleton
    fun provideCityRepository(citiesFileDataSource:CitiesFileDataSource,citiesMemoryDataSource:CitiesMemoryDataSource) = CityRepository(citiesFileDataSource,citiesMemoryDataSource)

    @Provides
    @Singleton
    fun provideGetCitiesInteractor(cityRepository:CityRepository,cityMapper: CityMapper) = GetCitiesInteractor(cityRepository,cityMapper)

    @Provides
    @Singleton
    fun provideSearchInteractor(cityRepository:CityRepository,cityMapper: CityMapper) = SearchInteractor(cityRepository,cityMapper)

    @Provides
    @Singleton
    fun provideCityMapper() = CityMapper()
}