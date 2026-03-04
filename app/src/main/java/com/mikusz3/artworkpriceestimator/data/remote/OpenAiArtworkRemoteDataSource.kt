package com.mikusz3.artworkpriceestimator.data.remote

import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OpenAiArtworkRemoteDataSource(
    private val client: OkHttpClient = OkHttpClient()
) {
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun estimateArtwork(
        apiKey: String,
        base64Image: String
    ): ArtworkEstimate = withContext(Dispatchers.IO) {
        val requestBody = buildRequestBody(base64Image)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody(mediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val bodyText = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw IOException("OpenAI error ${response.code}: $bodyText")
            }
            EstimateResponseParser.parseChatCompletionsResponse(bodyText)
        }
    }

    private fun buildRequestBody(base64Image: String): String {
        val prompt = """
            Analyze this artwork image.
            Estimate:
            1) artwork_type: either "traditional" or "digital"
            2) talent_level: integer from 1 to 10 based on observable quality and artistic skill
            3) experience_years: estimated years of artist experience inferred from quality and complexity
            4) estimated_price_usd: fair market-style estimate in US dollars for a commissioned piece of similar complexity
            5) reasoning: one concise sentence

            Return ONLY valid JSON in this exact shape:
            {
              "artwork_type": "traditional|digital",
              "talent_level": 1,
              "experience_years": 1,
              "estimated_price_usd": 0.0,
              "reasoning": "..."
            }
        """.trimIndent()

        return JSONObject()
            .put("model", "gpt-4o-mini")
            .put("temperature", 0.2)
            .put(
                "messages",
                JSONArray().put(
                    JSONObject()
                        .put("role", "user")
                        .put(
                            "content",
                            JSONArray()
                                .put(JSONObject().put("type", "text").put("text", prompt))
                                .put(
                                    JSONObject()
                                        .put("type", "image_url")
                                        .put(
                                            "image_url",
                                            JSONObject().put("url", "data:image/jpeg;base64,$base64Image")
                                        )
                                )
                        )
                )
            )
            .toString()
    }
}
