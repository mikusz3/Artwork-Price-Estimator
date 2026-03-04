package com.mikusz3.artworkpriceestimator

import com.mikusz3.artworkpriceestimator.data.remote.EstimateResponseParser
import org.junit.Test
import org.junit.Assert.assertEquals

class ExampleUnitTest {
    @Test
    fun parseModelContent_parsesExpectedFields() {
        val content = """
            ```json
            {
              "artwork_type": "digital",
              "talent_level": 8,
              "experience_years": 5,
              "estimated_price_usd": 480.5,
              "reasoning": "Strong composition and polished rendering."
            }
            ```
        """.trimIndent()

        val result = EstimateResponseParser.parseModelContent(content)

        assertEquals("digital", result.artworkType)
        assertEquals(8, result.talentLevel)
        assertEquals(5, result.experienceYears)
        assertEquals(480.5, result.estimatedPriceUsd, 0.001)
        assertEquals("Strong composition and polished rendering.", result.reasoning)
    }
}