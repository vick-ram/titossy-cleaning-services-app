package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val signOutState = viewModel.signOutState.collectAsState()
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Column {
            Text("Profile Screen", fontSize = MaterialTheme.typography.titleLarge.fontSize)

            Button(onClick = {
                scope.launch {
                    viewModel.signOut()
                }
            }) {
                Text(text = "sign out")
            }
        }
    }
    LaunchedEffect(key1 = signOutState.value.isSuccess, block = {
        if (signOutState.value.isSuccess?.isNotEmpty() == true){
            navController.navigate(RootNavRoutes.AUTH.route){
                navController.popBackStack()
            }
        }
    })

    LaunchedEffect(key1 = signOutState.value.isLoading, block = {
        if (signOutState.value.isLoading){
            Toast.makeText(context, "TAG", Toast.LENGTH_LONG).show()
        }
    })

    LaunchedEffect(key1 = signOutState.value.errorMessage, block = {
        if (signOutState.value.errorMessage?.isNotEmpty() == true){
            Toast.makeText(context, "${signOutState.value.errorMessage}", Toast.LENGTH_LONG).show()
        }
    })

}