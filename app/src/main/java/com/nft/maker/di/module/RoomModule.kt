package com.nft.maker.di.module

import android.content.Context
import androidx.room.Room
import com.nft.maker.db.ApplicationDatabase
import com.nft.maker.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context, ApplicationDatabase::class.java,
            ApplicationDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDAO(applicationDatabase: ApplicationDatabase): UserDao {
        return applicationDatabase.userDao()
    }
}