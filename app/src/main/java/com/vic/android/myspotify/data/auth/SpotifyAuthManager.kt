package com.vic.android.myspotify.data.auth

import android.content.Intent
import android.net.Uri
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import androidx.core.net.toUri

class SpotifyAuthManager(
    private val config: SpotifyAuthConfig,
    private val storage: SpotifyAuthStorage
) {

    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)

        secureRandom.nextBytes(bytes)

        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    fun generateCodeChallenge(codeVerifier: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(codeVerifier.toByteArray(Charsets.US_ASCII))

        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    fun buildAuthorizationUrl(): String {
        val verifier = generateCodeVerifier()

        storage.saveCodeVerifier(verifier)

        val codeChallenge = generateCodeChallenge(verifier)

        return Uri.Builder()
            .scheme("https")
            .authority("accounts.spotify.com")
            .appendPath("authorize")
            .appendQueryParameter("client_id", config.clientId)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", config.redirectUri)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", codeChallenge)
            .build()
            .toString()
    }

    fun getAuthorizationIntent(): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            buildAuthorizationUrl().toUri()
        )
    }
}