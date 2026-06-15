package com.openassist.data.openrouter

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenRouterApi {
    @POST("chat/completions")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Header("HTTP-Referer") referer: String = "app://openassist",
        @Header("X-Title") title: String = "OpenAssist",
        @Body request: ChatRequest,
    ): ChatResponse

    @GET("models")
    suspend fun models(): ModelsResponse
}
