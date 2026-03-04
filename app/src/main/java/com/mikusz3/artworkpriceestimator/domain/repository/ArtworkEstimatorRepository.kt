package com.mikusz3.artworkpriceestimator.domain.repository

import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate

interface ArtworkEstimatorRepository {
    fun getSavedApiKey(): String
    fun saveApiKey(apiKey: String)
    suspend fun analyzeArtwork(apiKey: String, base64Image: String): ArtworkEstimate
}
