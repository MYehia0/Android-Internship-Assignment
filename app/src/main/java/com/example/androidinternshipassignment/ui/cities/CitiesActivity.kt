package com.example.androidinternshipassignment.ui.cities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.androidinternshipassignment.R
import com.example.androidinternshipassignment.databinding.ActivityCitiesBinding
import com.example.androidinternshipassignment.domain.models.City
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
@OptIn(FlowPreview::class)
class CitiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitiesBinding
    private val viewModel: CitiesViewModel by viewModels()
    @Inject lateinit var adapter: CitiesAdapter
    private val searchQueryFlow = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cities)

        initializeAdapter()
        handleScreenState()
        setupSearchDebounce()

        binding.content.tryAgain.setOnClickListener {
            viewModel.loadCities()
        }
    }

    private fun setupSearchDebounce() {
        binding.content.searchEditText.addTextChangedListener {
            searchQueryFlow.value = it.toString()
//            viewModel.searchCities(it.toString())
        }

        lifecycleScope.launch {
            searchQueryFlow
                .debounce(300L.milliseconds)
                .distinctUntilChanged()
                .collectLatest { query ->
                    viewModel.searchCities(query)
                }
        }
    }

    private fun handleScreenState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    showLoadingLayout(it.isLoading)
                    if (it.isLoading) {
                        hideErrorLayout()
                    } else {
                        if (it.errors == null) {
                            adapter.submitList(it.searchResult)
                        } else {
                            showErrorLayout(it.errors)
                        }
                    }
                }
            }
        }
    }

    private fun initializeAdapter(){
        binding.content.recyclerCities.adapter = adapter
        adapter.onCityClickListener = object: CitiesAdapter.OnCityClickListener{
            override fun onCityClick(city: City) {
                pinLocationMap(city)
            }
        }
    }

    private fun pinLocationMap(city: City) {
        val geoUri ="http://maps.google.com/maps?q=loc:" + city.latitude + "," + city.longitude + " (" + city.name + ")"
        val mapUri = Uri.parse(geoUri)
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }

    private fun showErrorLayout(message: String?) {
        binding.content.errorLayout.isVisible = true
        binding.content.errorMessage.text = message
        binding.content.recyclerCities.isVisible = false
    }

    private fun showLoadingLayout(flag: Boolean) {
        binding.content.loadingIndicator.isVisible = flag
    }

    private fun hideErrorLayout() {
        binding.content.errorLayout.isVisible = false
        binding.content.recyclerCities.isVisible = true
    }
}