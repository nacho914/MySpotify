package com.vic.android.myspotify.di

import com.vic.android.myspotify.data.auth.SpotifyAuthStorage
import com.vic.android.myspotify.data.remote.SpotifyApiService
import com.vic.android.myspotify.data.remote.SpotifyAuthApiService
import com.vic.android.myspotify.data.remote.interceptor.SpotifyAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("spotifyApiClient")
    fun provideSpotifyApiClient(
        storage: SpotifyAuthStorage
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                SpotifyAuthInterceptor(storage)
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("spotifyAuthClient")
    fun provideSpotifyAuthClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("spotifyApiClient") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyApiService(
        retrofit: Retrofit
    ): SpotifyApiService {
        return retrofit.create(SpotifyApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("spotifyAuthRetrofit")
    fun provideSpotifyAuthRetrofit(
        @Named("spotifyAuthClient") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthApiService(
        @Named("spotifyAuthRetrofit") retrofit: Retrofit
    ): SpotifyAuthApiService {
        return retrofit.create(SpotifyAuthApiService::class.java)
    }
}