package com.example.titossycleaningservicesapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.navigation.rootGraph.RootNavGraph
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.ui.theme.TitossyCleaningServicesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val onBoardingCompleted by viewModel.onBoardingCompleted.collectAsState(initial = false)
            LaunchedEffect(onBoardingCompleted){
                if (onBoardingCompleted){
                    navController.navigate(RootNavRoutes.AUTH.route)
                } else {
                    navController.navigate(RootNavRoutes.ONBOARDING.route)
                }
            }
            TitossyCleaningServicesAppTheme {
                RootNavGraph(navController)
            }
        }
    }
}