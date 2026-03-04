package com.mikusz3.artworkpriceestimator.data.repository

import com.mikusz3.artworkpriceestimator.data.local.ApiKeyStorage
import com.mikusz3.artworkpriceestimator.data.remote.OpenAiArtworkRemoteDataSource
import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate
import com.mikusz3.artworkpriceestimator.domain.repository.ArtworkEstimatorRepository

class ArtworkEstimatorRepositoryImpl(
    private val apiKeyStorage: ApiKeyStorage,
    private val remoteDataSource: OpenAiArtworkRemoteDataSource
) : ArtworkEstimatorRepository {
    override fun getSavedApiKey(): String = apiKeyStorage.getApiKey()

    override fun saveApiKey(apiKey: String) {
        apiKeyStorage.saveApiKey(apiKey)
    }

    override suspend fun analyzeArtwork(apiKey: String, base64Image: String): ArtworkEstimate {
        return remoteDataSource.estimateArtwork(apiKey = apiKey, base64Image = base64Image)
    }
}
