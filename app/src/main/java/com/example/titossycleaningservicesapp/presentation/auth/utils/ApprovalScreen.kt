package com.example.titossycleaningservicesapp.presentation.auth.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

@Composable
fun ApprovalScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(RootNavRoutes.AUTH.route) }) {
            Text(text = "Waiting approval screen")
        }
    }
}