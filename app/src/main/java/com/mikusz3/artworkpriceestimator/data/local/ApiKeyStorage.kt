package com.mikusz3.artworkpriceestimator.data.local

interface ApiKeyStorage {
    fun getApiKey(): String
    fun saveApiKey(apiKey: String)
}
