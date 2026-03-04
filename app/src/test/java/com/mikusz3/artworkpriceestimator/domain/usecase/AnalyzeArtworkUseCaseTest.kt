package com.mikusz3.artworkpriceestimator.domain.usecase

import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate
import com.mikusz3.artworkpriceestimator.domain.repository.ArtworkEstimatorRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyzeArtworkUseCaseTest {
    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = ArtworkEstimate(
            artworkType = "traditional",
            talentLevel = 7,
            experienceYears = 3,
            estimatedPriceUsd = 320.0,
            reasoning = "Balanced brushwork and color control."
        )
        val repository = FakeRepository(expected)
        val useCase = AnalyzeArtworkUseCase(repository)

        val result = useCase(apiKey = "key", base64Image = "image")

        assertEquals(expected, result)
    }

    private class FakeRepository(
        private val estimate: ArtworkEstimate
    ) : ArtworkEstimatorRepository {
        override fun getSavedApiKey(): String = ""

        override fun saveApiKey(apiKey: String) = Unit

        override suspend fun analyzeArtwork(apiKey: String, base64Image: String): ArtworkEstimate {
            return estimate
        }
    }
}
