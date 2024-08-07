package com.example.androidinternshipassignment.domain.mapper

interface Mapper<I ,O> {
    fun map(input: I): O
}