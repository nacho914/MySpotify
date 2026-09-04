package com.vic.android.myspotify.di

import com.vic.android.myspotify.data.repository.AlbumRepositoryImpl
import com.vic.android.myspotify.data.repository.ArtistRepositoryImpl
import com.vic.android.myspotify.domain.repository.AlbumRepository
import com.vic.android.myspotify.domain.repository.ArtistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindArtistRepository(
        repositoryImpl: ArtistRepositoryImpl
    ): ArtistRepository

    @Binds
    abstract fun bindAlbumRepository(
        repositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository
}