package com.example.titossycleaningservicesapp.presentation.users.supervisor.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.utils.EmployeesProfile

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel
) {
    val context = LocalContext.current

    var supervisorId by rememberSaveable { mutableStateOf<String?>(null) }
    val employeeState by employeeViewModel.employeeUiState.collectAsStateWithLifecycle()
    val supervisor = employeeState.employees?.find { it.id.toString() == supervisorId }

    LaunchedEffect(key1 = supervisorId) {
        supervisorId = mainViewModel.readUserId()
    }

    when {
        employeeState.isLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = { CustomProgressIndicator(isLoading = true) }
        )

        employeeState.employees != null -> {
            supervisor?.let { sup ->
                EmployeesProfile(
                    paddingValues = paddingValues,
                    modifier = modifier,
                    employee = sup
                )
            }
        }

        employeeState.errorMessage.isNotEmpty() -> {
            Toast.makeText(
                context,
                employeeState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}