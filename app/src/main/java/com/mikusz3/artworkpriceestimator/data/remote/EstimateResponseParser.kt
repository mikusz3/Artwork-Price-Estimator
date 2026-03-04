package com.mikusz3.artworkpriceestimator.data.remote

import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate
import org.json.JSONObject

object EstimateResponseParser {
    fun parseChatCompletionsResponse(responseJson: String): ArtworkEstimate {
        val json = JSONObject(responseJson)
        val content = json
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")
        return parseModelContent(content)
    }

    fun parseModelContent(content: String): ArtworkEstimate {
        val cleaned = content
            .replace("```json", "")
            .replace("```", "")
            .trim()
            .let { text ->
                val start = text.indexOf('{')
                val end = text.lastIndexOf('}')
                if (start >= 0 && end > start) text.substring(start, end + 1) else text
            }

        val parsed = JSONObject(cleaned)
        return ArtworkEstimate(
            artworkType = parsed.optString("artwork_type", "unknown"),
            talentLevel = parsed.optInt("talent_level", 1),
            experienceYears = parsed.optInt("experience_years", 1),
            estimatedPriceUsd = parsed.optDouble("estimated_price_usd", 0.0),
            reasoning = parsed.optString("reasoning", "No reasoning provided.")
        )
    }
}
