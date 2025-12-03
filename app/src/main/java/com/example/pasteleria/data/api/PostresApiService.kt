package com.example.pasteleria.data.api



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostresApiService {

    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: PostresApi = retrofit.create(PostresApi::class.java)
}
