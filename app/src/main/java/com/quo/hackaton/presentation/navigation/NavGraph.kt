package com.quo.hackaton.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quo.hackaton.presentation.ui.screen.MapScreen
import com.quo.hackaton.presentation.ui.screen.AddressListScreen
import com.quo.hackaton.presentation.viewmodel.MainViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()

    NavHost(navController, startDestination = "list") {
        composable("list") {
            val list by viewModel.companies.collectAsState()
            AddressListScreen(
                companies = list,
                onCommentUpdate = { company, comment -> viewModel.onCommentUpdate(company, comment) },
                onShowMap = { navController.navigate("map") }
            )
        }
        composable("map") {
            MapScreen(
                companies = viewModel.companies.collectAsState().value,
                onShowList = { navController.navigate("list") }
            )
        }
    }
}