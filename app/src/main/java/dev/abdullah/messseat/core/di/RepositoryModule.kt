package dev.abdullah.messseat.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.data.api.AuthApi
import dev.abdullah.messseat.data.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun authRepository(api: AuthApi, session: SessionManager) = AuthRepository(api, session)
}