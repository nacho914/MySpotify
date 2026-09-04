package com.vic.android.myspotify.data.auth

import android.content.Intent
import android.net.Uri
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import androidx.core.net.toUri
import com.vic.android.myspotify.data.remote.SpotifyAuthApiService
import com.vic.android.myspotify.data.remote.model.SpotifyTokenResponse

class SpotifyAuthManager(
    private val config: SpotifyAuthConfig,
    private val storage: SpotifyAuthStorage,
    private val spotifyAuthApiService: SpotifyAuthApiService
) {

    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)

        secureRandom.nextBytes(bytes)

        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or
                    Base64.NO_WRAP or
                    Base64.NO_PADDING
        )
    }

    fun generateCodeChallenge(
        codeVerifier: String
    ): String {
        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(
                codeVerifier.toByteArray(
                    Charsets.US_ASCII
                )
            )

        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or
                    Base64.NO_WRAP or
                    Base64.NO_PADDING
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
            .appendQueryParameter(
                "client_id",
                config.clientId
            )
            .appendQueryParameter(
                "response_type",
                "code"
            )
            .appendQueryParameter(
                "redirect_uri",
                config.redirectUri
            )
            .appendQueryParameter(
                "code_challenge_method",
                "S256"
            )
            .appendQueryParameter(
                "code_challenge",
                codeChallenge
            )
            .build()
            .toString()
    }

    fun getAuthorizationIntent(): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            buildAuthorizationUrl().toUri()
        )
    }

    suspend fun exchangeCodeForToken(
        code: String
    ): SpotifyTokenResponse {

        val codeVerifier = storage.getCodeVerifier()
            ?: throw IllegalStateException(
                "Code verifier not found"
            )

        val tokenResponse =
            spotifyAuthApiService.getAccessToken(
                code = code,
                redirectUri = config.redirectUri,
                clientId = config.clientId,
                codeVerifier = codeVerifier
            )

        storage.saveAccessToken(
            accessToken = tokenResponse.accessToken,
            expiresIn = tokenResponse.expiresIn
        )

        tokenResponse.refreshToken?.let { refreshToken ->
            storage.saveRefreshToken(refreshToken)
        }

        storage.clearCodeVerifier()

        return tokenResponse
    }

    suspend fun refreshAccessToken(): SpotifyTokenResponse {
        val refreshToken = storage.getRefreshToken()
            ?: throw IllegalStateException("Refresh token not found")

        val response = spotifyAuthApiService.refreshAccessToken(
            refreshToken = refreshToken,
            clientId = config.clientId
        )

        storage.saveAccessToken(
            response.accessToken,
            response.expiresIn
        )

        response.refreshToken?.let {
            storage.saveRefreshToken(it)
        }

        return response
    }
}