package com.quo.hackaton.presentation.ui.screen

import android.Manifest
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.quo.hackaton.R
import com.quo.hackaton.utils.haversineDistance
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

private lateinit var fusedLocationClient: FusedLocationProviderClient

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    companies: List<Company>,
    onShowList: () -> Unit,
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

    MapViewContainer(companies, onShowList)
}

@Composable
fun MapViewContainer(
    companies: List<Company>,
    onShowList: () -> Unit,
) {
    val context = LocalContext.current
    var mapView: MapView? by remember { mutableStateOf(null) }

    val remaining = remember(companies) { companies.filter { it.status == Status.PENDING } }

    var userLocation by remember { mutableStateOf<Point?>(null) }

    LaunchedEffect(context) {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        fused.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                userLocation = Point(it.latitude, it.longitude)
            } ?: run {
                requestLocationUpdates()
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
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
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).also { mv ->
                mapView = mv
            }
        },
        update = { mv ->
            val map = mv.mapWindow.map
            remaining.firstOrNull()?.let {

            }

            val mapObjects = map.mapObjects
            mapObjects.clear()
            val companyIcon = ImageProvider.fromResource(context, R.drawable.placemark)
            remaining.forEach { comp ->
                mapObjects.addPlacemark().apply {
                    geometry = Point(comp.lat, comp.lon)
                    setIcon(companyIcon)
                }
            }

            userLocation?.let { userPoint ->
                map.move(
                    CameraPosition(Point(45.025922, 38.968387), 12f, 0f, 0f),  // TODO
                    Animation(Animation.Type.SMOOTH, 0.1f),
                    null
                )

                mapObjects.addPlacemark().apply {
                    geometry = Point(45.025922, 38.968387)  // TODO change
                    setIcon(ImageProvider.fromResource(context, R.drawable.user_marker))
                }

                val points = mutableListOf<RequestPoint>()
                var sorted = remaining.map { Point(it.lat, it.lon) }.toMutableList()
                var currentPoint = Point(45.025922, 38.968387)  // TODO change
                while (sorted.isNotEmpty()) {
                    sorted = sorted
                        .sortedBy { haversineDistance(it, currentPoint) }.toMutableList()
                    sorted.forEach { p ->
                        Log.d("PointSort", "${p.latitude} ${p.longitude}")
                    }
                    val point = sorted.removeFirst()
                    points.add(RequestPoint(point, RequestPointType.VIAPOINT,
                        null, null, null)
                    )
                    currentPoint = point
                }

                points.add(0, RequestPoint(Point(45.025922, 38.968387), RequestPointType.WAYPOINT,  // TODO
                    null, null, null))
                points.lastOrNull()?.let { furthest ->
                    points.removeLast()
                    points.add(RequestPoint(furthest.point, RequestPointType.WAYPOINT,
                        null, null, null))
                }

                points.forEach { p ->
                    Log.d("Point", "${p.point.latitude} ${p.point.longitude} ${p.type}")
                }

                drawDrivingRoutes(points, mapObjects, context, onShowList)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

private fun requestLocationUpdates() {
    val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
        .build()
    fusedLocationClient.requestLocationUpdates(
        request,
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    Log.d("Location", "Новая локация: ${loc.latitude}, ${loc.longitude}")
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        },
        Looper.getMainLooper()
    )
}

private fun drawDrivingRoutes(
    points: List<RequestPoint>,
    mapObjects: MapObjectCollection,
    context: Context,
    onShowList: () -> Unit
) {
    val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.ONLINE)
    val drivingOptions = DrivingOptions().apply {
        routesCount = points.size
    }
    val vehicleOptions = VehicleOptions()
    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            if (drivingRoutes.isNotEmpty()) {
                mapObjects.addPolyline(drivingRoutes.first().geometry)
            } else {
                Toast.makeText(context, "Не удалось построить маршрут", Toast.LENGTH_LONG).show()
                onShowList()
            }
        }

        override fun onDrivingRoutesError(error: Error) {
            Toast.makeText(context, "Не удалось построить маршрут", Toast.LENGTH_LONG).show()
            Log.d("Driving Routes", "Error: ${error.toString()}")
            onShowList()
        }
    }
    drivingRouter.requestRoutes(
        points,
        drivingOptions,
        vehicleOptions,
        drivingRouteListener
    )
}
