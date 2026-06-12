package com.example.plaintext.data.di

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.room.Room
import com.example.plaintext.data.PlainTextDatabase
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
import com.example.plaintext.data.repository.FakePasswordDBStore
import com.example.plaintext.ui.screens.hello.dbSimulator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {

    @Provides
    @Singleton
    fun providePlainTextDatabase(
        @ApplicationContext context: Context
    ): PlainTextDatabase {
        return Room.databaseBuilder(
            context,
            PlainTextDatabase::class.java,
            "plaintext_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePasswordDao(
        database: PlainTextDatabase
    ): PasswordDao = database.passwordDao()

    @Provides
    @Singleton
    fun providePasswordDBStore(
        @ApplicationContext context: Context,
        passwordDao: PasswordDao
    ): PasswordDBStore {
        val isDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return if (isDebug) {
            FakePasswordDBStore()
        } else {
            LocalPasswordDBStore(passwordDao)
        }
    }

    @Provides
    @Singleton
    fun provideDBSimulator(): dbSimulator = dbSimulator()
}