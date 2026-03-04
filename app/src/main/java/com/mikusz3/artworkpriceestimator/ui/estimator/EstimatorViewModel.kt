package com.mikusz3.artworkpriceestimator.ui.estimator

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikusz3.artworkpriceestimator.data.util.ImageBase64Encoder
import com.mikusz3.artworkpriceestimator.domain.usecase.AnalyzeArtworkUseCase
import com.mikusz3.artworkpriceestimator.domain.usecase.GetSavedApiKeyUseCase
import com.mikusz3.artworkpriceestimator.domain.usecase.SaveApiKeyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EstimatorViewModel(
    getSavedApiKeyUseCase: GetSavedApiKeyUseCase,
    private val saveApiKeyUseCase: SaveApiKeyUseCase,
    private val analyzeArtworkUseCase: AnalyzeArtworkUseCase,
    private val imageBase64Encoder: ImageBase64Encoder = ImageBase64Encoder
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        EstimatorUiState(
            apiKey = getSavedApiKeyUseCase()
        )
    )
    val uiState: StateFlow<EstimatorUiState> = _uiState

    fun onApiKeyChanged(value: String) {
        _uiState.update { it.copy(apiKey = value) }
        saveApiKeyUseCase(value)
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri, result = null, errorMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun analyzeArtwork(context: Context) {
        val currentState = _uiState.value
        val apiKey = currentState.apiKey.trim()
        val imageUri = currentState.selectedImageUri
        if (apiKey.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please enter your OpenAI API key.") }
            return
        }
        if (imageUri == null) {
            _uiState.update { it.copy(errorMessage = "Please upload an artwork image first.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, result = null) }
            runCatching {
                analyze(context = context, imageUri = imageUri, apiKey = apiKey)
            }.onSuccess { estimate ->
                _uiState.update { it.copy(isLoading = false, result = estimate) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unexpected error while analyzing artwork."
                    )
                }
            }
        }
    }

    private suspend fun analyze(context: Context, imageUri: Uri, apiKey: String) =
        analyzeArtworkUseCase(
            apiKey = apiKey,
            base64Image = imageBase64Encoder.encode(context, imageUri)
        )
}
