package com.quo.hackaton.presentation.ui.screen

import android.Manifest
import android.location.Location
import android.os.Looper
import android.util.Log
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
import com.quo.hackaton.utils.sortedByDistanceTo
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
    companies: List<Company>
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

    MapViewContainer(companies)
}

@Composable
private fun MapViewContainer(
    companies: List<Company>,
) {
    val context = LocalContext.current
    var mapView: MapView? by remember { mutableStateOf(null) }

    val remaining = companies.filter { it.status == Status.PENDING }
    val cameraPosition = CameraPosition(
        Point(remaining.firstOrNull()?.lat ?: 0.0, remaining.firstOrNull()?.lon ?: 0.0),
        12.0f, 0.0f, 0.0f,
    )
    var points = mutableListOf<RequestPoint>()

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
                Animation(Animation.Type.SMOOTH, 0.1f),
                null,
            )

            val mapObjects = mv.mapWindow.map.mapObjects.addCollection()
            mapObjects.clear()

            val imageProvider = ImageProvider.fromResource(mv.context, R.drawable.placemark)
            remaining.forEach { address ->
                mapObjects.addPlacemark().apply {
                    val point = Point(address.lat, address.lon)
                    geometry = point
                    points.add(RequestPoint(point, RequestPointType.WAYPOINT,
                        null, null, null)
                    )
                    setIcon(imageProvider)
                }
            }


            fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val lat = 43.401720
                        val lon = 39.980124
                        Log.d("Location", "Широта: $lat, Долгота: $lon")
                        mapObjects.addPlacemark().apply {
                            geometry = Point(lat, lon)
                            setIcon(ImageProvider.fromResource(mv.context, R.drawable.user_marker))
                        }
                        points = points.sortedByDistanceTo(Point(lat, lon))
                        points.add(0, RequestPoint(
                            Point(lat, lon), RequestPointType.WAYPOINT,
                            null, null, null)
                        )
                    } ?: run {
                        requestLocationUpdates()
                    }
                }

            drawDrivingRoutes(points, mapObjects)
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

private fun drawDrivingRoutes(points: List<RequestPoint>, mapObjects: MapObjectCollection) {
    val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.ONLINE)
    val drivingOptions = DrivingOptions().apply {
        routesCount = points.size
    }
    val vehicleOptions = VehicleOptions()
    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            for (route in drivingRoutes) {
                Log.d("Routes", "${route.routeId} | ${route.routePosition.heading()} | ${route.position.segmentPosition} | ${route.wayPoints.firstOrNull().toString()}")
                mapObjects.addPolyline(route.geometry)
                break
            }
        }

        override fun onDrivingRoutesError(error: Error) {
            Log.d("Driving Routes", "Error: $error")
        }
    }
    val drivingSession = drivingRouter.requestRoutes(
        points,
        drivingOptions,
        vehicleOptions,
        drivingRouteListener
    )
}
