package com.example.androidinternshipassignment.ui.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidinternshipassignment.databinding.ItemCitiesBinding
import com.example.androidinternshipassignment.domain.models.City

class CitiesAdapter(private var items: List<City?>? = emptyList()) : RecyclerView.Adapter<CitiesAdapter.CitiesHolder>() {

    var onCityClickListener: OnCityClickListener? = null

    interface OnCityClickListener {
        fun onCityClick(item: City)
    }

    class CitiesHolder(val binding: ItemCitiesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindCity(city: City?) {
            binding.city = city
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val binding = ItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesHolder(binding)
    }

    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        val item = items?.get(position)
        holder.bindCity(item)
        holder.binding.root.setOnClickListener {
            item?.let { city -> onCityClickListener?.onCityClick(city) }
        }
    }

    fun submitList(cities: List<City?>?) {
        items = cities
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items?.size ?: 0
}