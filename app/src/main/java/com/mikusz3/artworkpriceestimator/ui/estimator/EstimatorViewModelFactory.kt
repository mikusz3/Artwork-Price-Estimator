package com.mikusz3.artworkpriceestimator.ui.estimator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikusz3.artworkpriceestimator.data.util.ImageBase64Encoder
import com.mikusz3.artworkpriceestimator.domain.usecase.AnalyzeArtworkUseCase
import com.mikusz3.artworkpriceestimator.domain.usecase.GetSavedApiKeyUseCase
import com.mikusz3.artworkpriceestimator.domain.usecase.SaveApiKeyUseCase

class EstimatorViewModelFactory(
    private val getSavedApiKeyUseCase: GetSavedApiKeyUseCase,
    private val saveApiKeyUseCase: SaveApiKeyUseCase,
    private val analyzeArtworkUseCase: AnalyzeArtworkUseCase,
    private val imageBase64Encoder: ImageBase64Encoder = ImageBase64Encoder
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimatorViewModel::class.java)) {
            return EstimatorViewModel(
                getSavedApiKeyUseCase = getSavedApiKeyUseCase,
                saveApiKeyUseCase = saveApiKeyUseCase,
                analyzeArtworkUseCase = analyzeArtworkUseCase,
                imageBase64Encoder = imageBase64Encoder
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
