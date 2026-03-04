package com.mikusz3.artworkpriceestimator.ui.estimator

import android.net.Uri
import com.mikusz3.artworkpriceestimator.domain.model.ArtworkEstimate

data class EstimatorUiState(
    val apiKey: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val result: ArtworkEstimate? = null,
    val errorMessage: String? = null
)
