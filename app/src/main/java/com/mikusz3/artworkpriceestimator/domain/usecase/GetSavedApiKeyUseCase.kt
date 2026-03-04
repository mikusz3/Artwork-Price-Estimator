package com.mikusz3.artworkpriceestimator.domain.usecase

import com.mikusz3.artworkpriceestimator.domain.repository.ArtworkEstimatorRepository

class GetSavedApiKeyUseCase(
    private val repository: ArtworkEstimatorRepository
) {
    operator fun invoke(): String = repository.getSavedApiKey()
}
