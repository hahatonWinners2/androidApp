package com.quo.hackaton.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.quo.hackaton.presentation.navigation.NavGraph
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val key = ai.metaData.getString("com.yandex.mapkit.ApiKey")
        MapKitFactory.setApiKey(key!!)
        MapKitFactory.initialize(this)
        setContent {
            MaterialTheme {
                NavGraph()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}