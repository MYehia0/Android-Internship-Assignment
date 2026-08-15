package com.example.androidinternshipassignment.data.mapper

interface Mapper<I ,O> {
    fun map(input: I): O
}