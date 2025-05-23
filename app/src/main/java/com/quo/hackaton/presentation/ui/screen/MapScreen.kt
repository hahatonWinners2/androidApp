package com.quo.hackaton.presentation.ui.screen

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.quo.hackaton.domain.model.Address
import com.quo.hackaton.domain.model.Status
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.quo.hackaton.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    addresses: List<Address>
) {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (!permissionsState.allPermissionsGranted) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Для работы карты нужно разрешение на геолокацию")
        }
        return
    }

    MapViewContainer(addresses)
}

@Composable
private fun MapViewContainer(
    addresses: List<Address>,
) {
    var mapView: MapView? by remember { mutableStateOf(null) }

    val remaining = addresses.filter { it.status == Status.PENDING }
    val cameraPosition = CameraPosition(
        Point(remaining.firstOrNull()?.lat ?: 0.0, remaining.firstOrNull()?.lon ?: 0.0),
        12.0f, 0.0f, 0.0f,
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView?.onStart()
                    MapKitFactory.getInstance().onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView?.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).also { mv ->
                mapView = mv
            }
        },
        update = { mv ->
            mv.mapWindow.map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, 0f),
                null,
            )

            val mapObjects = mv.mapWindow.map.mapObjects.addCollection()
            mapObjects.clear()

            val imageProvider = ImageProvider.fromResource(mv.context, R.drawable.placemark)
            addresses.forEach { address ->
                mapObjects.addPlacemark().apply {
                    geometry = Point(address.lat, address.lon)
                    setIcon(imageProvider)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

