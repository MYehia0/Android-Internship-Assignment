package com.example.androidinternshipassignment.data

import android.content.Context
import com.example.androidinternshipassignment.data.datasources.file.CitiesFileDataSource
import com.example.androidinternshipassignment.data.repositories.CityRepositoryImpl
import com.example.androidinternshipassignment.domain.repositories.CityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCityRepository(impl: CityRepositoryImpl): CityRepository
}

@Module
@InstallIn(SingletonComponent::class)
class DiForData {
    @Provides
    @Singleton
    fun provideCitiesFileDataSource(@ApplicationContext context: Context) = CitiesFileDataSource(context)
}