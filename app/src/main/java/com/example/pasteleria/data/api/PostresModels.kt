package com.example.pasteleria.data.api

data class PostreDto(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

data class PostresResponse(
    val meals: List<PostreDto>?
)