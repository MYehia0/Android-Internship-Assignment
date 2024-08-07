package com.example.androidinternshipassignment.ui.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.androidinternshipassignment.databinding.ItemCitiesBinding
import com.example.androidinternshipassignment.domain.models.City

class CitiesAdapter (private var items: List<City?>?) : Adapter<CitiesAdapter.CitiesHolder>() {

    var onCityClickListener: OnCityClickListener? = null
    private lateinit var binding: ItemCitiesBinding

    interface OnCityClickListener {
        fun onCityClick(item: City)
    }

    class CitiesHolder(val binding: ItemCitiesBinding) : ViewHolder(binding.root) {
        fun bindCity(city: City?) {
            binding.city = city
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        binding = ItemCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesHolder(binding)
    }

    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        val item = items?.get(position)
        holder.bindCity(item)
        holder.binding.apply {
            root.setOnClickListener {
                item?.let { city -> onCityClickListener?.onCityClick(city) }
            }
        }
    }

    fun changeData(cities: List<City?>?) {
        items = cities
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items?.size?:0

}