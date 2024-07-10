package com.example.titossycleaningservicesapp.presentation.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee


@Composable
fun EmployeesProfile(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    employee: Employee?
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier.padding(16.dp),
                text = "My Profile",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        EmployeeInfoField(
            label = "Username:",
            field = employee?.username ?: ""
        )
        HorizontalDivider()
        EmployeeInfoField(
            label = "Name:",
            field = employee?.fullName ?: ""
        )
        HorizontalDivider()
        EmployeeInfoField(
            label = "Email:",
            field = employee?.email ?: ""
        )
        HorizontalDivider()
        EmployeeInfoField(
            label = "Phone:",
            field = employee?.phone ?: ""
        )
        HorizontalDivider()
        EmployeeInfoField(
            label = "Role:",
            field = employee?.role?.name ?: ""
        )
        HorizontalDivider()
    }
}

@Composable
fun EmployeeInfoField(
    modifier: Modifier = Modifier,
    label: String, field: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = style.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = modifier.height(8.dp))

        Text(
            modifier = modifier.padding(start = 16.dp),
            text = field,
            style = style.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(.7f)
            )
        )
    }
}