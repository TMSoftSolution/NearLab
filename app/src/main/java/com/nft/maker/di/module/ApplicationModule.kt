package com.nft.maker.di.module

import android.content.Context
import com.nft.maker.utils.NetworkManager
import com.nft.maker.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun getPreferenceHelper(@ApplicationContext context: Context): PreferenceHelper =
        PreferenceHelper(context)

    @Provides
    @Singleton
    fun getNetworkManager(@ApplicationContext context: Context): NetworkManager = NetworkManager(context)
}