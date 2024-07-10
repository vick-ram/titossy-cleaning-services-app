package com.example.titossycleaningservicesapp.presentation.users.finance.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    var financeId by rememberSaveable { mutableStateOf<String?>(null) }
    val employeeState by employeeViewModel.employeeUiState.collectAsStateWithLifecycle()

    val finance = employeeState.employees?.find { it.id.toString() == financeId }

    LaunchedEffect(key1 = financeId) {
        financeId = mainViewModel.readUserId()
    }

    finance?.let { fin ->
        EmployeesProfile(
            modifier = modifier,
            paddingValues = paddingValues,
            employee = fin
        )
    }

}
