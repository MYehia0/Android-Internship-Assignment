package com.example.androidinternshipassignment.ui

import com.example.androidinternshipassignment.ui.cities.CitiesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class DiForUi {
    @Provides
    fun provideCitiesAdapter() = CitiesAdapter(emptyList())
}