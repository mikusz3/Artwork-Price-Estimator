package com.mikusz3.artworkpriceestimator.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedApiKeyStorage(
    context: Context
) : ApiKeyStorage {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getApiKey(): String = prefs.getString(KEY_OPENAI_API_KEY, "").orEmpty()

    override fun saveApiKey(apiKey: String) {
        prefs.edit().putString(KEY_OPENAI_API_KEY, apiKey).apply()
    }

    private companion object {
        private const val PREFS_NAME = "artwork_estimator_secure_prefs"
        private const val KEY_OPENAI_API_KEY = "openai_api_key"
    }
}
