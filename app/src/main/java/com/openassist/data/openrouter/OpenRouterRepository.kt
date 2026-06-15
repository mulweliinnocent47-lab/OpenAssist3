package com.openassist.data.openrouter

import com.openassist.core.UserFacingErrors
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class OpenRouterRepository(
    private val api: OpenRouterApi = Retrofit.Builder()
        .baseUrl("https://openrouter.ai/api/v1/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(
            Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(OpenRouterApi::class.java),
) {
    /**
     * Sends one turn to the chat completions endpoint.
     *
     * @param tools Pass the full list of [ToolDefinition]s on every call.
     *              The list is omitted from the request body when empty so
     *              models that don't support tool-use still work correctly.
     */
    suspend fun chat(
        apiKey: String,
        model: String,
        messages: List<OpenRouterMessage>,
        tools: List<ToolDefinition> = emptyList(),
    ): Choice {
        require(apiKey.isNotBlank()) { "Add your OpenRouter API key in Settings before using cloud models." }
        val response = api.chat(
            authorization = "Bearer $apiKey",
            request = ChatRequest(
                model = model,
                messages = messages,
                tools = tools.ifEmpty { null },
            ),
        )
        return response.choices.firstOrNull()
            ?: error(UserFacingErrors.openRouterEmptyResponse())
    }

    suspend fun availableModels(): List<ModelInfo> = api.models().data
}
