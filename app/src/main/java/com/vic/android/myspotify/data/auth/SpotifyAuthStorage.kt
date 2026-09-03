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
        return preferences.getString(KEY_CODE_VERIFIER, null)
    }

    fun clearCodeVerifier() {
        preferences.edit {
            remove(KEY_CODE_VERIFIER)
        }
    }

    private companion object {
        const val KEY_CODE_VERIFIER = "code_verifier"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    fun saveAccessToken(accessToken: String) {
        preferences.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
        }
    }

    fun getAccessToken(): String? =
        preferences.getString(KEY_ACCESS_TOKEN, null)

    fun saveRefreshToken(refreshToken: String) {
        preferences.edit {
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun getRefreshToken(): String? =
        preferences.getString(KEY_REFRESH_TOKEN, null)
}