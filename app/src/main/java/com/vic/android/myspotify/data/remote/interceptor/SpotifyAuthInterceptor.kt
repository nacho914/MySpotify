package com.vic.android.myspotify.data.remote.interceptor

import android.util.Log
import com.vic.android.myspotify.data.auth.SpotifyAuthStorage
import okhttp3.Interceptor
import okhttp3.Response

class SpotifyAuthInterceptor(
    private val storage: SpotifyAuthStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = storage.getAccessToken()

        Log.d(
            "SpotifyAuth",
            "Access token available: ${!accessToken.isNullOrBlank()}"
        )

        val request = chain.request()
            .newBuilder()
            .apply {
                if (!accessToken.isNullOrBlank()) {
                    addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                }
            }
            .build()

        Log.d(
            "SpotifyAuth",
            "Request: ${request.method} ${request.url}"
        )

        return chain.proceed(request)
    }
}