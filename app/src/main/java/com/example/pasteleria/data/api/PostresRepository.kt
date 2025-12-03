package com.example.pasteleria.data.api



class PostresRepository(
    private val api: PostresApi = PostresApiService.api
) {

    suspend fun obtenerPostres(): List<PostreDto> {
        return api.getPostres().meals ?: emptyList()
    }
}
