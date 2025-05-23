package com.quo.hackaton.di

import com.quo.hackaton.data.api.AddressApi
import com.quo.hackaton.data.repository.AddressRepositoryImpl
import com.quo.hackaton.domain.repository.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")  // TODO CHANGE API URL, MOCK URL IS SET
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAddressApi(retrofit: Retrofit): AddressApi =
        retrofit.create(AddressApi::class.java)

    @Provides
    @Singleton
    fun provideAddressRepository(api: AddressApi): AddressRepository =
        AddressRepositoryImpl(api)
}