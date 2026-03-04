package com.mikusz3.artworkpriceestimator.domain.usecase

import com.mikusz3.artworkpriceestimator.domain.repository.ArtworkEstimatorRepository

class SaveApiKeyUseCase(
    private val repository: ArtworkEstimatorRepository
) {
    operator fun invoke(apiKey: String) = repository.saveApiKey(apiKey)
}
