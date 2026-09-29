package dev.abdullah.messseat.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.abdullah.messseat.core.data.PreferencesManager
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.data.appDataStore
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.appDataStore

    @Provides
    @Singleton
    fun provideSessionManager(dataStore: DataStore<Preferences>,json: Json): SessionManager =
        SessionManager(dataStore,json)

    @Provides
    @Singleton
    fun providePreferencesManager(dataStore: DataStore<Preferences>) = PreferencesManager(dataStore)

}