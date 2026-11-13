package dev.example.messseat.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.example.messseat.core.data.SessionManager
import dev.example.messseat.data.api.AuthApi
import dev.example.messseat.data.api.FileApi
import dev.example.messseat.data.api.PropertyApi
import dev.example.messseat.data.api.TenantApi
import dev.example.messseat.data.repository.AuthRepository
import dev.example.messseat.data.repository.FileRepository
import dev.example.messseat.data.repository.PropertyRepository
import dev.example.messseat.data.repository.TenantRepository
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

    @Provides
    @Singleton
    fun tenantRepository(api: TenantApi) = TenantRepository(api)
}