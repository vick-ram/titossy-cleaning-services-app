package com.example.titossycleaningservicesapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.navigation.rootGraph.RootNavGraph
import com.example.titossycleaningservicesapp.presentation.ui.theme.TitossyCleaningServicesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreKeys: DataStoreKeys

    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !mainViewModel.isLoading.value
        }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            TitossyCleaningServicesAppTheme {
                val startDestination by mainViewModel.startDestination
                Surface(
                   modifier = Modifier
                       .fillMaxSize()
                       .safeDrawingPadding()
                ) {
                    RootNavGraph(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        startDestination = startDestination,
                        dataStoreKeys = dataStoreKeys
                    )
                }
            }
        }
    }
}
