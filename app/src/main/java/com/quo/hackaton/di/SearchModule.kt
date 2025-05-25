package com.quo.hackaton.di

import com.quo.hackaton.data.repository.YandexRepositoryImpl
import com.quo.hackaton.domain.repository.YandexRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {
    @Singleton
    @Provides
    fun provideGeometry() : Geometry =
        Geometry.fromPoint(Point(55.751244, 37.618423))

    @Singleton
    @Provides
    fun provideSearchOptions() : SearchOptions =
        SearchOptions().apply {
            searchTypes = SearchType.GEO.value
            resultPageSize = 5
        }

    @Singleton
    @Provides
    fun provideSearchManager() : SearchManager =
        SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)

    @Singleton
    @Provides
    fun provideYandexRepository(geometry: Geometry, searchOptions: SearchOptions, searchManager: SearchManager) : YandexRepository =
        YandexRepositoryImpl(geometry, searchOptions, searchManager)
}