package com.mikusz3.artworkpriceestimator.ui.estimator

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun EstimatorScreen(
    viewModel: EstimatorViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.onImageSelected(uri) }

    EstimatorScreen(
        uiState = uiState,
        onApiKeyChanged = viewModel::onApiKeyChanged,
        onImagePickRequested = { imagePickerLauncher.launch("image/*") },
        onAnalyzeRequested = { viewModel.analyzeArtwork(context) },
        onErrorShown = viewModel::clearError,
        modifier = modifier
    )
}

@Composable
fun EstimatorScreen(
    uiState: EstimatorUiState,
    onApiKeyChanged: (String) -> Unit,
    onImagePickRequested: () -> Unit,
    onAnalyzeRequested: () -> Unit,
    onErrorShown: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Artwork Price Estimator")
            Text("Upload artwork, add your OpenAI API key, then estimate type and market price.")

            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = onApiKeyChanged,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("OpenAI API Key") },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = onImagePickRequested,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (uiState.selectedImageUri == null) {
                        "Upload Artwork"
                    } else {
                        "Choose Different Artwork"
                    }
                )
            }

            uiState.selectedImageUri?.let {
                Text("Selected image: ${it.lastPathSegment ?: it}")
            }

            Button(
                onClick = onAnalyzeRequested,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Analyze and Estimate Price")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(28.dp))
                Text("Analyzing image with OpenAI...")
            }

            uiState.result?.let { result ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Estimated Artwork Type: ${result.artworkType}")
                        Text("Estimated Talent Level: ${result.talentLevel}/10")
                        Text("Estimated Experience: ${result.experienceYears} years")
                        Text("Estimated Price: $${"%.2f".format(result.estimatedPriceUsd)}")
                        Text("Reasoning: ${result.reasoning}")
                    }
                }
            }
        }
    }
}
