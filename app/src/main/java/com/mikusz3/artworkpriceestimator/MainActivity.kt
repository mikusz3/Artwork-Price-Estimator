package com.mikusz3.artworkpriceestimator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikusz3.artworkpriceestimator.data.local.EncryptedApiKeyStorage
import com.mikusz3.artworkpriceestimator.data.remote.OpenAiArtworkRemoteDataSource
import com.mikusz3.artworkpriceestimator.data.repository.ArtworkEstimatorRepositoryImpl
import com.mikusz3.artworkpriceestimator.data.util.ImageBase64Encoder
import com.mikusz3.artworkpriceestimator.domain.usecase.AnalyzeArtworkUseCase
import com.mikusz3.artworkpriceestimator.domain.usecase.GetSavedApiKeyUseCase
import com.mikusz3.artworkpriceestimator.domain.usecase.SaveApiKeyUseCase
import com.mikusz3.artworkpriceestimator.ui.estimator.EstimatorScreen
import com.mikusz3.artworkpriceestimator.ui.estimator.EstimatorUiState
import com.mikusz3.artworkpriceestimator.ui.estimator.EstimatorViewModel
import com.mikusz3.artworkpriceestimator.ui.estimator.EstimatorViewModelFactory
import com.mikusz3.artworkpriceestimator.ui.theme.ArtworkPriceEstimatorTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<EstimatorViewModel> {
        val storage = EncryptedApiKeyStorage(applicationContext)
        val remote = OpenAiArtworkRemoteDataSource()
        val repository = ArtworkEstimatorRepositoryImpl(storage, remote)
        EstimatorViewModelFactory(
            getSavedApiKeyUseCase = GetSavedApiKeyUseCase(repository),
            saveApiKeyUseCase = SaveApiKeyUseCase(repository),
            analyzeArtworkUseCase = AnalyzeArtworkUseCase(repository),
            imageBase64Encoder = ImageBase64Encoder
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtworkPriceEstimatorTheme {
                EstimatorScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EstimatorPreview() {
    ArtworkPriceEstimatorTheme {
        EstimatorScreen(
            uiState = EstimatorUiState(),
            onApiKeyChanged = {},
            onImagePickRequested = {},
            onAnalyzeRequested = {}
        )
    }
}