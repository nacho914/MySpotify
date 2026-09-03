package com.vic.android.myspotify.di

import android.content.Context
import com.vic.android.myspotify.R
import com.vic.android.myspotify.data.auth.SpotifyAuthConfig
import com.vic.android.myspotify.data.auth.SpotifyAuthManager
import com.vic.android.myspotify.data.auth.SpotifyAuthStorage
import com.vic.android.myspotify.data.remote.SpotifyAuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifyModule {

    @Provides
    @Singleton
    fun provideSpotifyAuthConfig(
        @ApplicationContext context: Context
    ): SpotifyAuthConfig {
        return SpotifyAuthConfig(
            clientId = context.getString(R.string.spotify_client_id),
            redirectUri = "myspotify://callback"
        )
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthManager(
        config: SpotifyAuthConfig,
        storage: SpotifyAuthStorage,
        spotifyAuthApiService: SpotifyAuthApiService
    ): SpotifyAuthManager {
        return SpotifyAuthManager(
            config = config,
            storage = storage,
            spotifyAuthApiService = spotifyAuthApiService
        )
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthStorage(
        @ApplicationContext context: Context
    ): SpotifyAuthStorage {
        return SpotifyAuthStorage(context)
    }
}