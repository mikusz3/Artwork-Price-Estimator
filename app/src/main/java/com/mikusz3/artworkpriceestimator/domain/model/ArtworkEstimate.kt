package com.mikusz3.artworkpriceestimator.domain.model

data class ArtworkEstimate(
    val artworkType: String,
    val talentLevel: Int,
    val experienceYears: Int,
    val estimatedPriceUsd: Double,
    val reasoning: String
)
