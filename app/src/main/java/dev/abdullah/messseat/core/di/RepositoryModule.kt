package dev.abdullah.messseat.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.data.api.AuthApi
import dev.abdullah.messseat.data.api.FileApi
import dev.abdullah.messseat.data.api.PropertyApi
import dev.abdullah.messseat.data.repository.AuthRepository
import dev.abdullah.messseat.data.repository.FileRepository
import dev.abdullah.messseat.data.repository.PropertyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun authRepository(api: AuthApi, session: SessionManager) = AuthRepository(api, session)

    @Provides
    @Singleton
    fun fileRepository(api: FileApi, @ApplicationContext context: Context) =
        FileRepository(api, context)

    @Provides
    @Singleton
    fun propertyRepository(api: PropertyApi) = PropertyRepository(api)
}