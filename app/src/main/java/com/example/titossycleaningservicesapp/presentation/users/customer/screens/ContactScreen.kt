package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerViewModel

@Composable
fun ContactScreen(navController: NavHostController, paddingValues: PaddingValues) {
    val viewModel: CustomerViewModel = hiltViewModel()
    //val currentUserId = viewModel.currentUser?.uid
    //val chatViewModel: ChatViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Box(modifier = Modifier.padding(paddingValues)) {

    }
}