package com.example.androidinternshipassignment.ui.cities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.androidinternshipassignment.R
import com.example.androidinternshipassignment.databinding.ActivityCitiesBinding
import com.example.androidinternshipassignment.domain.models.City
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CitiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitiesBinding
    private val viewModel: CitiesViewModel by viewModels()
    @Inject lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cities)
        initializeAdapter()
        handleScreenState()
        binding.content.searchEditText.addTextChangedListener{
            viewModel.searchCities(it.toString())
        }
        binding.content.tryAgain.setOnClickListener {
            viewModel.loadCities()
        }
    }

    private fun handleScreenState(){
        lifecycleScope.launch {
            viewModel.uiState.collect {
                showLoadingLayout(it.isLoading)
                if (it.isLoading) {
                    hideErrorLayout()
                } else {
                    if (it.errors.equals(null)) {
                        adapter.changeData(it.searchResult)
                    } else {
                        showErrorLayout(it.errors)
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