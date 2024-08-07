package com.example.androidinternshipassignment.domain.usecases

import com.example.androidinternshipassignment.data.repositories.CityRepository
import com.example.androidinternshipassignment.domain.mapper.CityMapper
import com.example.androidinternshipassignment.domain.models.City
import javax.inject.Inject

class SearchInteractor @Inject constructor(private val cityRepository: CityRepository, private val cityMapper: CityMapper) {

    suspend operator fun invoke(query: String): List<City> =
        if (query.isBlank() || query.isEmpty()) {
            cityRepository.getCitiesFromMemory().map { cityMapper.map(it) }
        } else {
            binaryPrefixSearch(
                cityRepository.getCitiesFromMemory().map { cityMapper.map(it) },
                query
            )
        }

    private fun binaryPrefixSearch(list: List<City>, prefix: String): List<City> {
        val result = mutableListOf<City>()
        var left = 0
        var right = list.size - 1
        val prefixLength = prefix.length

        // Binary search to find the starting index
        while (left <= right) {
            val mid = (left + right) / 2
            val midString = list[mid].name.substring(0, minOf(list[mid].name.length, prefixLength))
            if (midString.compareTo(prefix, ignoreCase = true) < 0) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }

        // Collect all matches
        var start = left
        while (start < list.size && list[start].name.startsWith(prefix, ignoreCase = true)) {
            result.add(list[start])
            start++
        }

        return result
    }
}

/*

Binary Prefix Search Steps:-
-----------------------------
1- Setup:
    - You have a sorted list of strings.
    - You want to find all strings starting with a given prefix.

2- Find Start Position:
    - Use binary search to find where the prefix could start.
    - Adjust the search range based on comparisons with the prefix.
    - This gives the first index (low) where strings starting with the prefix might appear.

3- Find End Position:
    - Use binary search again to find where the prefix ends.
    - Adjust the search range based on comparisons with a slightly higher prefix (prefix + '\uFFFF').
    - This gives the last index (high) where strings starting with the prefix might appear.

4- Extract Matches:
    - Collect the strings between the start and end positions (low to high).

Comparison between Binary Prefix Search and Linear Search:-
-------------------------------------------------------------
- Binary Prefix Search is more efficient for large sorted lists due to its logarithmic time complexity
  O(logn) for finding the range, compared to the linear time complexity O(n) of linear search.
- Linear Search is simpler and works on unsorted lists but is less efficient for large lists.

*/