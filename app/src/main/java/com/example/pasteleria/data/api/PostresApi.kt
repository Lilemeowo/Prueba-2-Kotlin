package com.example.pasteleria.data.api



import retrofit2.http.GET
import retrofit2.http.Query

interface PostresApi {

    // Ejemplo: https://www.themealdb.com/api/json/v1/1/filter.php?c=Dessert
    @GET("https://www.themealdb.com/api/json/v1/1/filter.php?c=Dessert")
    suspend fun getPostres(
        @Query("c") categoria: String = "Dessert"
    ): PostresResponse
}
