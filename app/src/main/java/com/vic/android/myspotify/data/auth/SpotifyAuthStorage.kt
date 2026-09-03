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
    }
}