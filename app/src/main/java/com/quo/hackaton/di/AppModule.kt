package com.quo.hackaton.di

import com.quo.hackaton.data.api.ClientsApi
import com.quo.hackaton.data.repository.ClientsRepositoryImpl
import com.quo.hackaton.domain.repository.ClientsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideClientsApi(retrofit: Retrofit): ClientsApi =
        retrofit.create(ClientsApi::class.java)

    @Provides
    @Singleton
    fun provideClientsRepository(api: ClientsApi): ClientsRepository =
        ClientsRepositoryImpl(api)
}