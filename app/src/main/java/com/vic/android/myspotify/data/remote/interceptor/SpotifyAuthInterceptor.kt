package com.vic.android.myspotify.data.remote.interceptor

import com.vic.android.myspotify.data.auth.SpotifyAuthStorage
import okhttp3.Interceptor
import okhttp3.Response

class SpotifyAuthInterceptor(
    private val storage: SpotifyAuthStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = storage.getAccessToken()

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
        return chain.proceed(request)
    }
}