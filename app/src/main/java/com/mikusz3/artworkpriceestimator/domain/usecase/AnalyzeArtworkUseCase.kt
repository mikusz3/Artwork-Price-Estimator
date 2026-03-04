package com.mikusz3.artworkpriceestimator.domain.usecase

import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate
import com.mikusz3.artworkpriceestimator.domain.repository.ArtworkEstimatorRepository

class AnalyzeArtworkUseCase(
    private val repository: ArtworkEstimatorRepository
) {
    suspend operator fun invoke(apiKey: String, base64Image: String): ArtworkEstimate {
        return repository.analyzeArtwork(apiKey = apiKey, base64Image = base64Image)
    }
}
