package com.quo.hackaton.data.repository

import android.util.Log
import com.quo.hackaton.domain.model.Coordinates
import com.quo.hackaton.domain.repository.YandexRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.runtime.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class YandexRepositoryImpl(
    private val geometry: Geometry,
    private val searchOptions: SearchOptions,
    private val searchManager: SearchManager,
) : YandexRepository {
    override suspend fun getCoordinatesByText(name: String): Coordinates =
        withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { cont ->
                val session = searchManager.submit(
                    name,
                    geometry,
                    searchOptions,
                    object : SearchListener {
                        override fun onSearchResponse(response: Response) {
                            val firstGeo =
                                response.collection.children.firstNotNullOfOrNull { it.obj }

                            val point = firstGeo
                                ?.geometry
                                ?.firstOrNull()
                                ?.point

                            if (point != null) {
                                val address = firstGeo.metadataContainer
                                    .getItem(ToponymObjectMetadata::class.java)
                                    ?.address
                                    ?.formattedAddress

                                cont.resume(
                                    Coordinates(
                                        address = address,
                                        point = point,
                                        name = name
                                    )
                                )
                            } else {
                                cont.resumeWithException(
                                    NoSuchElementException("No location found for \"$name\"")
                                )
                            }
                        }

                        override fun onSearchError(error: Error) {
                            cont.resumeWithException(
                                RuntimeException("Yandex Search error: $error")
                            )
                            Log.d("Search", "$error")
                        }
                    }
                )

                cont.invokeOnCancellation { session.cancel() }
            }
        }
}