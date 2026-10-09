package dev.abdullah.messseat.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.abdullah.messseat.data.api.AuthApi
import dev.abdullah.messseat.data.api.ChatApi
import dev.abdullah.messseat.data.api.FileApi
import dev.abdullah.messseat.data.api.NotificationApi
import dev.abdullah.messseat.data.api.PropertyApi
import dev.abdullah.messseat.data.api.RefreshApi
import dev.abdullah.messseat.data.api.TenantApi
import dev.abdullah.messseat.data.api.UserApi
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun authApi(r: Retrofit): AuthApi = r.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun refreshApi(@Named("refresh") r: Retrofit): RefreshApi = r.create(RefreshApi::class.java)

    @Provides
    @Singleton
    fun fileApi(r: Retrofit): FileApi = r.create(FileApi::class.java)

    @Provides
    @Singleton
    fun propertyAPi(r: Retrofit): PropertyApi = r.create(PropertyApi::class.java)

    @Provides
    @Singleton
    fun tenantApi(r: Retrofit): TenantApi = r.create(TenantApi::class.java)

    @Provides
    @Singleton
    fun chatApi(r: Retrofit): ChatApi = r.create(ChatApi::class.java)

    @Provides
    @Singleton
    fun userApi(r: Retrofit): UserApi = r.create(UserApi::class.java)

    @Provides
    @Singleton
    fun notificationApi(r: Retrofit) : NotificationApi = r.create(NotificationApi::class.java)
}