package com.example.androidinternshipassignment.ui.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidinternshipassignment.domain.models.CitySearchModel
import com.example.androidinternshipassignment.domain.usecases.GetCitiesInteractor
import com.example.androidinternshipassignment.domain.usecases.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val getCitiesInteractor: GetCitiesInteractor
): ViewModel() {

    private val _uiState = MutableStateFlow(CitySearchModel())
    val uiState: StateFlow<CitySearchModel>
        get() = _uiState.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch {
            _uiState.update { old -> old.copy(isLoading = true) }
            try {
                _uiState.update { old ->
                    old.copy(
                        searchResult = getCitiesInteractor(),
                        isLoading = false
                    )
                }
            } catch (ex: Exception) {
                _uiState.update { old ->
                    old.copy(
                        errors = ex.localizedMessage,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update { old ->
                    old.copy(
                        searchResult = searchInteractor(query),
                        isLoading = false
                    )
                }
            } catch (ex: Exception) {
                _uiState.update { old ->
                    old.copy(
                        errors = ex.localizedMessage,
                        isLoading = false
                    )
                }
            }
        }
    }
}