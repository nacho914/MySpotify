package com.vic.android.myspotify.data.auth

import android.content.Context
import androidx.core.content.edit

class SpotifyAuthStorage(
    private val context: Context
) {

    private val preferences = context.getSharedPreferences(
        "spotify_auth",
        Context.MODE_PRIVATE
    )

    fun saveCodeVerifier(codeVerifier: String) {
        preferences.edit {
            putString(KEY_CODE_VERIFIER, codeVerifier)
        }
    }

    fun getCodeVerifier(): String? {
        return preferences.getString(
            KEY_CODE_VERIFIER,
            null
        )
    }

    fun clearCodeVerifier() {
        preferences.edit {
            remove(KEY_CODE_VERIFIER)
        }
    }

    fun saveAccessToken(
        accessToken: String,
        expiresIn: Int
    ) {
        val expirationTime =
            System.currentTimeMillis() + (expiresIn * 1000L)

        preferences.edit {
            putString(
                KEY_ACCESS_TOKEN,
                accessToken
            )
                .putLong(
                    KEY_ACCESS_TOKEN_EXPIRATION,
                    expirationTime
                )
        }
    }

    fun getAccessToken(): String? {
        return preferences.getString(
            KEY_ACCESS_TOKEN,
            null
        )
    }

    fun isAccessTokenValid(): Boolean {
        val accessToken = getAccessToken()

        val expirationTime = preferences.getLong(
            KEY_ACCESS_TOKEN_EXPIRATION,
            0L
        )

        return !accessToken.isNullOrBlank() &&
                System.currentTimeMillis() < expirationTime
    }

    fun saveRefreshToken(refreshToken: String) {
        preferences.edit {
            putString(
                KEY_REFRESH_TOKEN,
                refreshToken
            )
        }
    }

    fun getRefreshToken(): String? {
        return preferences.getString(
            KEY_REFRESH_TOKEN,
            null
        )
    }

    fun clearTokens() {
        preferences.edit {
            remove(KEY_ACCESS_TOKEN)
                .remove(KEY_ACCESS_TOKEN_EXPIRATION)
                .remove(KEY_REFRESH_TOKEN)
        }
    }

    private companion object {
        const val KEY_CODE_VERIFIER = "code_verifier"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_ACCESS_TOKEN_EXPIRATION =
            "access_token_expiration"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}